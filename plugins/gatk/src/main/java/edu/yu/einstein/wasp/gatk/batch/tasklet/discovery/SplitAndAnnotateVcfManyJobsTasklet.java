package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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
		FileGroup combinedGenotypedVcfFg = null;
		ExecutionContext jobExecutionContext = this.getStepExecution().getJobExecution().getExecutionContext();
		if (jobExecutionContext.containsKey("combinedGenotypedVcfFgId"))
			combinedGenotypedVcfFg = fileService.getFileGroupById(Integer.parseInt(jobExecutionContext.getString("combinedGenotypedVcfFgId")));
		
		// a set of bam filegroups was previously registered corresponding to the inputs of the variant calling step. We now need to obtain a vcf file
		// corresponding to the result of haplotype calling and combined genotyping for each bam file input 
		// i.e. split out the merged vcf which was the output of cohort-level genotyping.
		Map<Sample,FileGroup> samplesFgMapUsedForVarCalling = new LinkedHashMap<>();
		logger.debug("Getting FileGroup ids passed in from previous step");
		if (jobExecutionContext.containsKey("sampleFgMap"))
			samplesFgMapUsedForVarCalling.putAll(AbstractGatkTasklet.getSampleFgMapFromJsonString(jobExecutionContext.getString("sampleFgMap"), sampleService, fileService));
	
		// split and annotate per sample or per sample pair as appropriate
		LinkedHashSet<FileGroup> inputFileGroups = new LinkedHashSet<>();
		inputFileGroups.add(combinedGenotypedVcfFg);
		Set<Sample> processedSamples = new HashSet<>();
		for (SampleSource samplePair : sampleService.getSamplePairsByJob(job)){
			LinkedHashSet<String> sampleIdentifierSet = new LinkedHashSet<>(); 
			Sample test = sampleService.getTestSample(samplePair);
			Sample control = sampleService.getControlSample(samplePair);
			logger.debug("handling sample pair: test id=" + test.getId() + " and control id=" + control.getId());
			processedSamples.add(test);
			processedSamples.add(control);
			sampleIdentifierSet.add(test.getUUID().toString());
			sampleIdentifierSet.add(control.getUUID().toString());
			String outFileNamePrefix = fileService.generateUniqueBaseFileName(test) + fileService.generateUniqueBaseFileName(control);
			prepareOutFilesAndLaunchJob(inputFileGroups, outFileNamePrefix, sampleIdentifierSet);	
		}
		for (Sample sample : samplesFgMapUsedForVarCalling.keySet()){
			if (processedSamples.contains(sample))
				continue;
			processedSamples.add(sample);
			LinkedHashSet<String> sampleIdentifierSet = new LinkedHashSet<>(); 
			sampleIdentifierSet.add(sample.getUUID().toString());
			String outFileNamePrefix = fileService.generateUniqueBaseFileName(sample);
			prepareOutFilesAndLaunchJob(inputFileGroups, outFileNamePrefix, sampleIdentifierSet);	
		}
	}
	
	private void prepareOutFilesAndLaunchJob(LinkedHashSet<FileGroup> inputFileGroups, String outFileNamePrefix, LinkedHashSet<String> sampleIdentifierSet){
		LinkedHashSet<FileGroup> outputFileGroups = new LinkedHashSet<>();
		String vcfFileName = outFileNamePrefix + "annotated.vcf";
		FileGroup vcfG = new FileGroup();
		FileHandle vcf = new FileHandle();
		vcf.setFileName(vcfFileName);
		vcf = fileService.addFileInDiscreteTransaction(vcf);
		vcfG.setIsActive(0);
		vcfG.addFileHandle(vcf);
		vcfG.setFileType(vcfFileType);
		vcfG.setDescription(vcfFileName);
		vcfG.setSoftwareGeneratedById(gatk.getId());
		vcfG.setDerivedFrom(inputFileGroups);
		vcfG = fileService.addFileGroupInDiscreteTransaction(vcfG);
		fileTypeService.addAttribute(vcfG, VcfFileTypeAttribute.ANNOTATED);
		outputFileGroups.add(vcfG);
		
		String summaryHtmlFileName = outFileNamePrefix + "snpEff_summary.htm";
		FileGroup summaryHtmlG = new FileGroup();
		FileHandle summaryHtml = new FileHandle();
		summaryHtml.setFileName(summaryHtmlFileName);
		summaryHtml = fileService.addFileInDiscreteTransaction(summaryHtml);
		summaryHtmlG.setIsActive(0);
		summaryHtmlG.addFileHandle(summaryHtml);
		summaryHtmlG.setFileType(htmlFileType);
		summaryHtmlG.setDescription(summaryHtmlFileName);
		summaryHtmlG.setSoftwareGeneratedById(gatk.getId());
		summaryHtmlG.setDerivedFrom(inputFileGroups);
		summaryHtmlG = fileService.addFileGroupInDiscreteTransaction(summaryHtmlG);
		outputFileGroups.add(summaryHtmlG);
		
		Map<String, String> jobParameters = new HashMap<>();
		jobParameters.put("uniqCode", Long.toString(Calendar.getInstance().getTimeInMillis())); // overcomes limitation of job being run only once
		jobParameters.put(WaspSoftwareJobParameters.FILEGROUP_ID_LIST_INPUT, AbstractGatkTasklet.getModelIdsAsCommaDelimitedString(inputFileGroups));
		jobParameters.put(WaspSoftwareJobParameters.FILEGROUP_ID_LIST_OUTPUT, AbstractGatkTasklet.getModelIdsAsCommaDelimitedString(outputFileGroups));
		jobParameters.put("sampleIdentifierSet", StringUtils.collectionToCommaDelimitedString(sampleIdentifierSet));
		jobParameters.put(WaspSoftwareJobParameters.JOB_ID, jobId.toString());
		try {
			requestLaunch("gatk.variantDiscovery.hc.splitAndAnnotateVariants.jobFlow", jobParameters);
		} catch (WaspMessageBuildingException e) {
			e.printStackTrace();
		}
	}
}
