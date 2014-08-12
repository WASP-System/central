package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.LaunchManyJobsTasklet;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.filetype.FileTypeAttribute;
import edu.yu.einstein.wasp.filetype.service.FileTypeService;
import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.VcfFileTypeAttribute;
import edu.yu.einstein.wasp.plugin.mps.grid.software.SnpEff;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * 
 * @author asmclellan
 *
 */
public class SplitAndAnnotateVcfManyJobsTasklet extends LaunchManyJobsTasklet {
	
	private static Logger logger = LoggerFactory.getLogger(SplitAndAnnotateVcfManyJobsTasklet.class);
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	@Qualifier("fileTypeServiceImpl")
	private FileTypeService fileTypeService;
	
	@Autowired
	private FileType vcfFileType;
	
	@Autowired
	private FileType htmlFileType;
	
	@Autowired
	private FileType tsvFileType;
	
	@Autowired
	private GATKSoftwareComponent gatk;

	private Integer jobId;
	
	public SplitAndAnnotateVcfManyJobsTasklet(Integer jobId) {
		this.jobId = jobId;
	}
	
	@Override
	@Transactional("entityManager")
	public void doExecute() {
		Job job = jobService.getJobByJobId(jobId);
		Assert.assertTrue(job.getId() > 0);
		FileGroup filteredSnpsVcfFg = null;
		FileGroup filteredIndelsVcfFg = null;
		ExecutionContext jobExecutionContext = this.getStepExecution().getJobExecution().getExecutionContext();
		if (jobExecutionContext.containsKey("filteredSnpsVcfFgId"))
			filteredSnpsVcfFg = fileService.getFileGroupById(Integer.parseInt(jobExecutionContext.getString("filteredSnpsVcfFgId")));
		if (jobExecutionContext.containsKey("filteredIndelsVcfFgId"))
			filteredIndelsVcfFg = fileService.getFileGroupById(Integer.parseInt(jobExecutionContext.getString("filteredIndelsVcfFgId")));
		
		// a set of bam filegroups was previously registered corresponding to the inputs of the variant calling step. We now need to obtain a vcf file
		// corresponding to the result of haplotype calling and combined genotyping for each bam file input 
		// i.e. split out the merged vcf which was the output of cohort-level genotyping.
		Map<Sample,FileGroup> samplesFgMapUsedForVarCalling = new LinkedHashMap<>();
		logger.debug("Getting FileGroup ids passed in from previous step");
		if (jobExecutionContext.containsKey("sampleFgMap"))
			samplesFgMapUsedForVarCalling.putAll(AbstractGatkTasklet.getSampleFgMapFromJsonString(jobExecutionContext.getString("sampleFgMap"), sampleService, fileService));
	
		// split and annotate per sample or per sample pair as appropriate
		
		Set<Sample> processedSamples = new HashSet<>();
		for (SampleSource samplePair : sampleService.getSamplePairsByJob(job)){
			LinkedHashSet<UUID> sampleIdentifierSet = new LinkedHashSet<>(); 
			Sample test = sampleService.getTestSample(samplePair);
			Sample control = sampleService.getControlSample(samplePair);
			logger.debug("handling sample pair: test id=" + test.getId() + " and control id=" + control.getId());
			processedSamples.add(test);
			processedSamples.add(control);
			sampleIdentifierSet.add(test.getUUID());
			sampleIdentifierSet.add(control.getUUID());
			String outFileNamePrefix = fileService.generateUniqueBaseFileName(test) + fileService.generateUniqueBaseFileName(control);
			prepareOutFilesAndLaunchJob(filteredSnpsVcfFg, outFileNamePrefix + "snps.filtered.", sampleIdentifierSet);
			prepareOutFilesAndLaunchJob(filteredIndelsVcfFg, outFileNamePrefix + "indels.filtered.", sampleIdentifierSet);
		}
		for (Sample sample : samplesFgMapUsedForVarCalling.keySet()){
			if (processedSamples.contains(sample))
				continue;
			processedSamples.add(sample);
			LinkedHashSet<UUID> sampleIdentifierSet = new LinkedHashSet<>(); 
			sampleIdentifierSet.add(sample.getUUID());
			String outFileNamePrefix = fileService.generateUniqueBaseFileName(sample);
			prepareOutFilesAndLaunchJob(filteredSnpsVcfFg, outFileNamePrefix + "snps.filtered.", sampleIdentifierSet);
			prepareOutFilesAndLaunchJob(filteredIndelsVcfFg, outFileNamePrefix + "indels.filtered.", sampleIdentifierSet);
		}
	}
	
	private void prepareOutFilesAndLaunchJob(FileGroup inputFileGroup, String outFileNamePrefix, LinkedHashSet<UUID> sampleIdentifierSet){
		Set<Sample> sampleSet = new HashSet<Sample>();
		for (UUID sIdStr : sampleIdentifierSet)
			sampleSet.add(sampleService.getSampleDao().getByUUID(sIdStr));
		LinkedHashSet<FileGroup> inputFileGroups = new LinkedHashSet<>();
		inputFileGroups.add(inputFileGroup);
		SnpEff snpEff = (SnpEff) gatk.getSoftwareDependencyByIname("snpEff");
		LinkedHashSet<FileGroup> outputFileGroups = new LinkedHashSet<>();
		String vcfFileName = outFileNamePrefix + "annotated.vcf";
		FileGroup vcfG = new FileGroup();
		FileHandle vcf = new FileHandle();
		vcf.setFileName(vcfFileName);
		vcfG.setIsActive(0);
		vcfG.addFileHandle(vcf);
		vcfG.setFileType(vcfFileType);
		vcfG.setDescription(vcfFileName);
		vcfG.setSoftwareGeneratedById(snpEff.getId());
		vcfG.setDerivedFrom(inputFileGroups);
		vcfG.setSamples(sampleSet);
		Set<FileTypeAttribute> fta = new HashSet<>(fileTypeService.getAttributes(inputFileGroup));
		fta.add(VcfFileTypeAttribute.ANNOTATED);
		vcfG = fileService.saveInDiscreteTransaction(vcfG, fta);
		outputFileGroups.add(vcfG);
		
		String summaryHtmlFileName = outFileNamePrefix + "snpEff_summary.htm";
		FileGroup summaryHtmlG = new FileGroup();
		FileHandle summaryHtml = new FileHandle();
		summaryHtml.setFileName(summaryHtmlFileName);
		summaryHtmlG.setIsActive(0);
		summaryHtmlG.addFileHandle(summaryHtml);
		summaryHtmlG.setFileType(htmlFileType);
		summaryHtmlG.setDescription(summaryHtmlFileName);
		summaryHtmlG.setSoftwareGeneratedById(snpEff.getId());
		summaryHtmlG.setDerivedFrom(inputFileGroups);
		summaryHtmlG.setSamples(sampleSet);
		summaryHtmlG = fileService.saveInDiscreteTransaction(summaryHtmlG, null);
		outputFileGroups.add(summaryHtmlG);
		
		String summaryGeneFileName = outFileNamePrefix + "snpEff_geneSummary.tsv";
		FileGroup summaryGeneG = new FileGroup();
		FileHandle summaryGene = new FileHandle();
		summaryGene.setFileName(summaryGeneFileName);
		summaryGeneG.setIsActive(0);
		summaryGeneG.addFileHandle(summaryGene);
		summaryGeneG.setFileType(tsvFileType);
		summaryGeneG.setDescription(summaryGeneFileName);
		summaryGeneG.setSoftwareGeneratedById(snpEff.getId());
		summaryGeneG.setDerivedFrom(inputFileGroups);
		summaryGeneG.setSamples(sampleSet);
		summaryGeneG = fileService.saveInDiscreteTransaction(summaryGeneG, null);
		outputFileGroups.add(summaryGeneG);
		
		Map<String, String> jobParameters = new HashMap<>();
		//jobParameters.put("uniqCode", Long.toString(Calendar.getInstance().getTimeInMillis())); // overcomes limitation of job being run only once
		jobParameters.put(WaspSoftwareJobParameters.FILEGROUP_ID_LIST_INPUT, AbstractGatkTasklet.getModelIdsAsCommaDelimitedString(inputFileGroups));
		jobParameters.put(WaspSoftwareJobParameters.FILEGROUP_ID_LIST_OUTPUT, AbstractGatkTasklet.getModelIdsAsCommaDelimitedString(outputFileGroups));
		jobParameters.put("sampleIdentifierSet", StringUtils.collectionToCommaDelimitedString(sampleIdentifierSet));
		jobParameters.put(WaspSoftwareJobParameters.JOB_ID, jobId.toString());
		try {
			requestLaunch("gatk.variantDiscovery.splitAndAnnotateVariants.jobFlow", jobParameters);
		} catch (WaspMessageBuildingException e) {
			e.printStackTrace();
		}
	}
}
