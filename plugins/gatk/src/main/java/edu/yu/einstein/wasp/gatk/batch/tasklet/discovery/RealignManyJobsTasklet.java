package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private GatkService gatkService;
	
	@Autowired
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
		if (getStepExecution().getExecutionContext().containsKey("mergedSampleFgMap"))
			mergedSampleFileGroupsIn = AbstractGatkTasklet.getSampleFgMapFromJsonString(getStepExecution().getExecutionContext().getString("mergedSampleFgMap"), sampleService, fileService);
		allFgIn.putAll(mergedSampleFileGroupsIn);
		if (getStepExecution().getExecutionContext().containsKey("passThroughSampleFgMap"))
			allFgIn.putAll(AbstractGatkTasklet.getSampleFgMapFromJsonString(getStepExecution().getExecutionContext().getString("passThroughSampleFgMap"), sampleService, fileService));
		Map<Sample, FileGroup> sampleFileGroupsForNextStep = new HashMap<>();
		Map<Sample, FileGroup> passThroughSampleFileGroupsForNextStep = new HashMap<>();
		
		// merge, realign and split out again test-control sample pairs
		for (SampleSource samplePair : sampleService.getSamplePairsByJob(job)){
			LinkedHashSet<FileGroup> inputFileGroups = new LinkedHashSet<>();
			LinkedHashSet<FileGroup> outputFileGroups = new LinkedHashSet<>();
			Sample test = sampleService.getTestSample(samplePair);
			Sample control = sampleService.getControlSample(samplePair);
			FileGroup testFgIn = fileService.getFileGroupById(allFgIn.get(test).getId());
			FileGroup controlFgIn = fileService.getFileGroupById(allFgIn.get(control).getId());
			inputFileGroups.add(testFgIn);
			inputFileGroups.add(controlFgIn);
			Set<SampleSource> sampleSources = new HashSet<>();
			sampleSources.addAll(testFgIn.getSampleSources());
			sampleSources.addAll(controlFgIn.getSampleSources());
			
			String bamOutputMerge = testFgIn.getDescription().replace(".bam", "_pairRealn.bam");
			String baiOutputMerge = bamOutputMerge.replace(".bam", ".bai");
			FileGroup bamMergeG = new FileGroup();
			FileHandle bamMerge = new FileHandle();
			bamMerge.setFileName(bamOutputMerge);
			bamMerge = fileService.addFile(bamMerge);
			bamMergeG.setIsActive(0);
			bamMergeG.addFileHandle(bamMerge);
			bamMergeG.setFileType(bamFileType);
			bamMergeG.setDescription(bamOutputMerge);
			bamMergeG.setSoftwareGeneratedById(gatk.getId());
			bamMergeG = fileService.addFileGroup(bamMergeG);
			bamMergeG.addDerivedFrom(testFgIn);
			bamMergeG.addDerivedFrom(controlFgIn);
			bamMergeG.setSampleSources(sampleSources);
			fileTypeService.setAttributes(bamMergeG, gatkService.getCompleteGatkPreprocessBamFileAttributeSet());
			outputFileGroups.add(bamMergeG);

			FileGroup baiMergeG = new FileGroup();
			FileHandle baiMerge = new FileHandle();
			baiMerge.setFileName(baiOutputMerge);
			baiMerge = fileService.addFile(baiMerge);
			baiMergeG.setIsActive(0);
			baiMergeG.addFileHandle(baiMerge);
			baiMergeG.setFileType(baiFileType);
			baiMergeG.setDescription(baiOutputMerge);
			baiMergeG.setSoftwareGeneratedById(gatk.getId());
			baiMergeG = fileService.addFileGroup(baiMergeG);
			baiMergeG.addDerivedFrom(testFgIn);
			baiMergeG.addDerivedFrom(controlFgIn);
			baiMergeG.setSampleSources(sampleSources);
			outputFileGroups.add(baiMergeG);
			
			Map<String, String> jobParameters = new HashMap<>();
			jobParameters.put(WaspSoftwareJobParameters.FILEGROUP_ID_LIST_INPUT, AbstractGatkTasklet.getFileGroupIdsAsCommaDelimitedString(inputFileGroups));
			jobParameters.put(WaspSoftwareJobParameters.FILEGROUP_ID_LIST_OUTPUT, AbstractGatkTasklet.getFileGroupIdsAsCommaDelimitedString(outputFileGroups));
			jobParameters.put(WaspSoftwareJobParameters.JOB_ID, jobId.toString());
			try {
				requestLaunch("gatk.variantDiscovery.hc.realignTestControlPairs.jobFlow", jobParameters);
			} catch (WaspMessageBuildingException e) {
				e.printStackTrace();
			}
			sampleFileGroupsForNextStep.put(test, baiMergeG);
			sampleFileGroupsForNextStep.put(control, baiMergeG);
		}
		
		// re-align merged filegroups from previous step not in pairs
		for (Sample sample : mergedSampleFileGroupsIn.keySet()){
			if (!sampleFileGroupsForNextStep.containsKey(sample)){
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
				
				Map<String, String> jobParameters = new HashMap<>();
				jobParameters.put(WaspSoftwareJobParameters.FILEGROUP_ID_LIST_INPUT, AbstractGatkTasklet.getFileGroupIdsAsCommaDelimitedString(inputFileGroups));
				jobParameters.put(WaspSoftwareJobParameters.FILEGROUP_ID_LIST_OUTPUT, AbstractGatkTasklet.getFileGroupIdsAsCommaDelimitedString(outputFileGroups));
				jobParameters.put(WaspSoftwareJobParameters.JOB_ID, jobId.toString());
				try {
					requestLaunch("gatk.variantDiscovery.hc.realign.jobFlow", jobParameters);
				} catch (WaspMessageBuildingException e) {
					e.printStackTrace();
				}
				sampleFileGroupsForNextStep.put(sample, bamMergedG);
			}
		}
		
		// prepare the list of remaining filegroups for samples not processed above.
		for (Sample sample : allFgIn.keySet())
			if (! sampleFileGroupsForNextStep.containsKey(sample))
				passThroughSampleFileGroupsForNextStep.put(sample, allFgIn.get(sample));
		
		// put files needed for next step into step execution context to be promoted to job context
		getStepExecution().getExecutionContext().put("mergedSampleFgMap", AbstractGatkTasklet.getSampleFgMapAsJsonString(sampleFileGroupsForNextStep));
		getStepExecution().getExecutionContext().put("passThroughSampleFgMap", AbstractGatkTasklet.getSampleFgMapAsJsonString(passThroughSampleFileGroupsForNextStep));
	}
}
