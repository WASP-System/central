package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.LaunchManyJobsTasklet;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.filetype.service.FileTypeService;
import edu.yu.einstein.wasp.gatk.service.GatkService;
import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * 
 * @author asmclellan
 *
 */
public class RealignManyJobsTasklet extends LaunchManyJobsTasklet {
	
	private static Logger logger = LoggerFactory.getLogger(RealignManyJobsTasklet.class);
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private GatkService gatkService;
	
	@Autowired
	@Qualifier("fileTypeServiceImpl")
	private FileTypeService fileTypeService;
	
	@Autowired
	private FileType bamFileType;
	
	@Autowired
	private FileType baiFileType;
	
	@Autowired
	private GATKSoftwareComponent gatk;

	private Integer jobId;
	
	public RealignManyJobsTasklet(Integer jobId) {
		this.jobId = jobId;
	}
	
	@Override
	@Transactional("EntityManager")
	public void doExecute() {
		Job job = jobService.getJobByJobId(jobId);
		Assert.assertTrue(job.getId() > 0);
		Map<Sample, FileGroup> mergedSampleFileGroupsIn = new HashMap<>();
		Map<Sample, FileGroup> allFgIn = new HashMap<>();
		LinkedHashSet<FileGroup> temporaryFileSet = new LinkedHashSet<>();
		ExecutionContext jobExecutionContext = this.getStepExecution().getJobExecution().getExecutionContext();
		if (jobExecutionContext.containsKey("temporaryFileSet"))
			temporaryFileSet = AbstractGatkTasklet.getFileGroupsFromCommaDelimitedString(jobExecutionContext.getString("temporaryFileSet"), fileService);
		if (jobExecutionContext.containsKey("mergedSampleFgMap"))
			mergedSampleFileGroupsIn = AbstractGatkTasklet.getSampleFgMapFromJsonString(jobExecutionContext.getString("mergedSampleFgMap"), sampleService, fileService);
		allFgIn.putAll(mergedSampleFileGroupsIn);
		if (jobExecutionContext.containsKey("passThroughSampleFgMap"))
			allFgIn.putAll(AbstractGatkTasklet.getSampleFgMapFromJsonString(jobExecutionContext.getString("passThroughSampleFgMap"), sampleService, fileService));
		Map<FileGroup, Set<Sample>> fileGroupSamplesForNextStep = new HashMap<>();
		Set<Sample> processedSamples = new HashSet<>();
		// merge, realign and split out again test-control sample pairs
		for (SampleSource samplePair : sampleService.getSamplePairsByJob(job)){
			LinkedHashSet<FileGroup> inputFileGroups = new LinkedHashSet<>();
			LinkedHashSet<FileGroup> outputFileGroups = new LinkedHashSet<>();
			Sample test = sampleService.getTestSample(samplePair);
			Sample control = sampleService.getControlSample(samplePair);
			processedSamples.add(test);
			processedSamples.add(control);
			FileGroup testFgIn = fileService.getFileGroupById(allFgIn.get(test).getId());
			FileGroup controlFgIn = fileService.getFileGroupById(allFgIn.get(control).getId());
			inputFileGroups.add(testFgIn);
			inputFileGroups.add(controlFgIn);
			LinkedHashSet<SampleSource> sampleSources = new LinkedHashSet<>();
			sampleSources.addAll(testFgIn.getSampleSources());
			sampleSources.addAll(controlFgIn.getSampleSources());
			
			String bamOutputMergedPairs = testFgIn.getDescription().replace(".bam", "_pairRealn.bam");
			String baiOutputMergedPairs = bamOutputMergedPairs.replace(".bam", ".bai");
			FileGroup bamMergedPairsG = new FileGroup();
			FileHandle bamMergedPairs = new FileHandle();
			bamMergedPairs.setFileName(bamOutputMergedPairs);
			bamMergedPairs = fileService.addFile(bamMergedPairs);
			bamMergedPairsG.setIsActive(0);
			bamMergedPairsG.addFileHandle(bamMergedPairs);
			bamMergedPairsG.setFileType(bamFileType);
			bamMergedPairsG.setDescription(bamOutputMergedPairs);
			bamMergedPairsG.setSoftwareGeneratedById(gatk.getId());
			bamMergedPairsG = fileService.addFileGroup(bamMergedPairsG);
			bamMergedPairsG.addDerivedFrom(testFgIn);
			bamMergedPairsG.addDerivedFrom(controlFgIn);
			bamMergedPairsG.setSampleSources(sampleSources);
			fileTypeService.setAttributes(bamMergedPairsG, gatkService.getCompleteGatkPreprocessBamFileAttributeSet());
			outputFileGroups.add(bamMergedPairsG);

			FileGroup baiMergedPairsG = new FileGroup();
			FileHandle baiMergedPairs = new FileHandle();
			baiMergedPairs.setFileName(baiOutputMergedPairs);
			baiMergedPairs = fileService.addFile(baiMergedPairs);
			baiMergedPairsG.setIsActive(0);
			baiMergedPairsG.addFileHandle(baiMergedPairs);
			baiMergedPairsG.setFileType(baiFileType);
			baiMergedPairsG.setDescription(baiOutputMergedPairs);
			baiMergedPairsG.setSoftwareGeneratedById(gatk.getId());
			baiMergedPairsG = fileService.addFileGroup(baiMergedPairsG);
			baiMergedPairsG.addDerivedFrom(bamMergedPairsG);
			outputFileGroups.add(baiMergedPairsG);
			temporaryFileSet.addAll(outputFileGroups);
			
			Map<String, String> jobParameters = new HashMap<>();
			jobParameters.put(WaspSoftwareJobParameters.FILEGROUP_ID_LIST_INPUT, AbstractGatkTasklet.getModelIdsAsCommaDelimitedString(inputFileGroups));
			jobParameters.put(WaspSoftwareJobParameters.FILEGROUP_ID_LIST_OUTPUT, AbstractGatkTasklet.getModelIdsAsCommaDelimitedString(outputFileGroups));
			jobParameters.put(WaspSoftwareJobParameters.JOB_ID, jobId.toString());
			try {
				requestLaunch("gatk.variantDiscovery.hc.realignTestControlPairs.jobFlow", jobParameters);
			} catch (WaspMessageBuildingException e) {
				e.printStackTrace();
			}
			fileGroupSamplesForNextStep.put(baiMergedPairsG, new HashSet<Sample>());
			fileGroupSamplesForNextStep.get(baiMergedPairsG).add(test);
			fileGroupSamplesForNextStep.get(baiMergedPairsG).add(control);
		}
		
		// re-align merged filegroups from previous step not in pairs
		for (Sample sample : mergedSampleFileGroupsIn.keySet()){
			if (!processedSamples.contains(sample)){
				processedSamples.add(sample);
				LinkedHashSet<FileGroup> inputFileGroups = new LinkedHashSet<>();
				LinkedHashSet<FileGroup> outputFileGroups = new LinkedHashSet<>();
				FileGroup mergedBam = mergedSampleFileGroupsIn.get(sample);
				inputFileGroups.add(mergedBam);
				String bamOutputMerged = mergedBam.getDescription().replace(".bam", "_realn.bam");
				String baiOutputMerged = bamOutputMerged.replace(".bam", ".bai");
				FileGroup bamMergedG = new FileGroup();
				FileHandle bamMerged = new FileHandle();
				bamMerged.setFileName(bamOutputMerged);
				bamMerged = fileService.addFile(bamMerged);
				bamMergedG.setIsActive(0);
				bamMergedG.addFileHandle(bamMerged);
				bamMergedG.setFileType(bamFileType);
				bamMergedG.setDescription(bamOutputMerged);
				bamMergedG.setSoftwareGeneratedById(gatk.getId());
				bamMergedG = fileService.addFileGroup(bamMergedG);
				bamMergedG.addDerivedFrom(mergedBam);
				bamMergedG.setSampleSources(mergedBam.getSampleSources());
				fileTypeService.setAttributes(bamMergedG, gatkService.getCompleteGatkPreprocessBamFileAttributeSet());
				outputFileGroups.add(bamMergedG);

				FileGroup baiMergedG = new FileGroup();
				FileHandle baiMerged = new FileHandle();
				baiMerged.setFileName(baiOutputMerged);
				baiMerged = fileService.addFile(baiMerged);
				baiMergedG.setIsActive(0);
				baiMergedG.addFileHandle(baiMerged);
				baiMergedG.setFileType(baiFileType);
				baiMergedG.setDescription(baiOutputMerged);
				baiMergedG.setSoftwareGeneratedById(gatk.getId());
				baiMergedG = fileService.addFileGroup(baiMergedG);
				baiMergedG.addDerivedFrom(mergedBam);
				baiMergedG.setSampleSources(mergedBam.getSampleSources());
				outputFileGroups.add(baiMergedG);
				temporaryFileSet.addAll(outputFileGroups);
				
				Map<String, String> jobParameters = new HashMap<>();
				jobParameters.put("uniqCode", Long.toString(Calendar.getInstance().getTimeInMillis())); // overcomes limitation of job being run only once
				jobParameters.put(WaspSoftwareJobParameters.FILEGROUP_ID_LIST_INPUT, AbstractGatkTasklet.getModelIdsAsCommaDelimitedString(inputFileGroups));
				jobParameters.put(WaspSoftwareJobParameters.FILEGROUP_ID_LIST_OUTPUT, AbstractGatkTasklet.getModelIdsAsCommaDelimitedString(outputFileGroups));
				jobParameters.put(WaspSoftwareJobParameters.JOB_ID, jobId.toString());
				try {
					requestLaunch("gatk.variantDiscovery.hc.realign.jobFlow", jobParameters);
				} catch (WaspMessageBuildingException e) {
					e.printStackTrace();
				}
				fileGroupSamplesForNextStep.put(bamMergedG, new HashSet<Sample>());
				fileGroupSamplesForNextStep.get(bamMergedG).add(sample);
			}
		}
		
		// prepare the list of remaining filegroups for samples not processed above.
		for (Sample sample : allFgIn.keySet())
			if (! processedSamples.contains(sample)){
				FileGroup fg = allFgIn.get(sample);
				fileGroupSamplesForNextStep.put(fg, new HashSet<Sample>());
				fileGroupSamplesForNextStep.get(fg).add(sample);
			}
		
		// put files needed for next step into step execution context to be promoted to job context
		getStepExecution().getExecutionContext().put("fgSamplesMap", AbstractGatkTasklet.getFgSamplesMapAsJsonString(fileGroupSamplesForNextStep));
		getStepExecution().getExecutionContext().put("temporaryFileSet", AbstractGatkTasklet.getModelIdsAsCommaDelimitedString(temporaryFileSet));
	}
}
