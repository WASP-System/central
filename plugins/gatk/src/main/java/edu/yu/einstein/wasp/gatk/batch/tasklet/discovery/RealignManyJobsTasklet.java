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
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.LaunchManyJobsTasklet;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
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
	@Transactional("entityManager")
	public void doExecute() {
		Job job = jobService.getJobByJobId(jobId);
		Assert.assertTrue(job.getId() > 0);
		Map<Sample, FileGroup> mergedSampleFileGroupsIn = new HashMap<>();
		Map<Sample, FileGroup> allFgIn = new HashMap<>();
		LinkedHashSet<FileGroup> temporaryFileSet = new LinkedHashSet<>();
		ExecutionContext jobExecutionContext = this.getStepExecution().getJobExecution().getExecutionContext();
		logger.debug("Getting FileGroup ids passed in from previous step");
		if (jobExecutionContext.containsKey("temporaryFileSet"))
			temporaryFileSet = AbstractGatkTasklet.getFileGroupsFromCommaDelimitedString(jobExecutionContext.getString("temporaryFileSet"), fileService);
		if (jobExecutionContext.containsKey("mergedSampleFgMap"))
			mergedSampleFileGroupsIn = AbstractGatkTasklet.getSampleFgMapFromJsonString(jobExecutionContext.getString("mergedSampleFgMap"), sampleService, fileService);
		allFgIn.putAll(mergedSampleFileGroupsIn);
		if (jobExecutionContext.containsKey("passThroughSampleFgMap"))
			allFgIn.putAll(AbstractGatkTasklet.getSampleFgMapFromJsonString(jobExecutionContext.getString("passThroughSampleFgMap"), sampleService, fileService));
		Map<Sample, FileGroup> sampleFileGroupsForNextStep = new HashMap<>();
		Set<Sample> processedSamples = new HashSet<>();
		// merge, realign and split out again test-control sample pairs
		for (SampleSource samplePair : sampleService.getSamplePairsByJob(job)){
			LinkedHashSet<FileGroup> inputFileGroups = new LinkedHashSet<>();
			LinkedHashSet<FileGroup> outputFileGroups = new LinkedHashSet<>();
			Sample test = sampleService.getTestSample(samplePair);
			Sample control = sampleService.getControlSample(samplePair);
			logger.debug("handling sample pair: test id=" + test.getId() + " and control id=" + control.getId());
			processedSamples.add(test);
			processedSamples.add(control);
			FileGroup testFgIn = fileService.getFileGroupById(allFgIn.get(test).getId());
			FileGroup controlFgIn = fileService.getFileGroupById(allFgIn.get(control).getId());
			inputFileGroups.add(testFgIn);
			inputFileGroups.add(controlFgIn);
						
			String bamOutputMergedPairsTest = fileService.generateUniqueBaseFileName(test) + "gatk_preproc_merged_dedup_pairRealn.bam";
			String baiOutputMergedPairsTest = fileService.generateUniqueBaseFileName(test) + "gatk_preproc_merged_dedup_pairRealn.bai";
			FileGroup bamMergedPairsTestG = new FileGroup();
			FileHandle bamMergedPairsTest = new FileHandle();
			bamMergedPairsTest.setFileName(bamOutputMergedPairsTest);
			bamMergedPairsTestG.setIsActive(0);
			bamMergedPairsTestG.addFileHandle(bamMergedPairsTest);
			bamMergedPairsTestG.setFileType(bamFileType);
			bamMergedPairsTestG.setDescription(bamOutputMergedPairsTest);
			bamMergedPairsTestG.setSoftwareGeneratedById(gatk.getId());
			bamMergedPairsTestG.addDerivedFrom(testFgIn);
			bamMergedPairsTestG = fileService.saveInDiscreteTransaction(bamMergedPairsTestG, bamMergedPairsTest, gatkService.getCompleteGatkPreprocessBamFileAttributeSet());
			outputFileGroups.add(bamMergedPairsTestG);

			FileGroup baiMergedPairsTestG = new FileGroup();
			FileHandle baiMergedPairsTest = new FileHandle();
			baiMergedPairsTest.setFileName(baiOutputMergedPairsTest);
			baiMergedPairsTestG.setIsActive(0);
			baiMergedPairsTestG.addFileHandle(baiMergedPairsTest);
			baiMergedPairsTestG.setFileType(baiFileType);
			baiMergedPairsTestG.setDescription(baiOutputMergedPairsTest);
			baiMergedPairsTestG.setSoftwareGeneratedById(gatk.getId());
			baiMergedPairsTestG.addDerivedFrom(bamMergedPairsTestG);
			baiMergedPairsTestG = fileService.saveInDiscreteTransaction(baiMergedPairsTestG, baiMergedPairsTest);
			outputFileGroups.add(baiMergedPairsTestG);
			
			String bamOutputMergedPairsControl = fileService.generateUniqueBaseFileName(control) + "gatk_preproc_merged_dedup_pairRealn.bam";
			String baiOutputMergedPairsControl = fileService.generateUniqueBaseFileName(control) + "gatk_preproc_merged_dedup_pairRealn.bai";
			FileGroup bamMergedPairsControlG = new FileGroup();
			FileHandle bamMergedPairsControl = new FileHandle();
			bamMergedPairsControl.setFileName(bamOutputMergedPairsControl);
			bamMergedPairsControlG.setIsActive(0);
			bamMergedPairsControlG.addFileHandle(bamMergedPairsControl);
			bamMergedPairsControlG.setFileType(bamFileType);
			bamMergedPairsControlG.setDescription(bamOutputMergedPairsControl);
			bamMergedPairsControlG.setSoftwareGeneratedById(gatk.getId());
			bamMergedPairsControlG.addDerivedFrom(controlFgIn);
			bamMergedPairsControlG = fileService.saveInDiscreteTransaction(bamMergedPairsControlG, bamMergedPairsControl, gatkService.getCompleteGatkPreprocessBamFileAttributeSet());
			outputFileGroups.add(bamMergedPairsControlG);

			FileGroup baiMergedPairsControlG = new FileGroup();
			FileHandle baiMergedPairsControl = new FileHandle();
			baiMergedPairsControl.setFileName(baiOutputMergedPairsControl);
			baiMergedPairsControlG.setIsActive(0);
			baiMergedPairsControlG.addFileHandle(baiMergedPairsControl);
			baiMergedPairsControlG.setFileType(baiFileType);
			baiMergedPairsControlG.setDescription(baiOutputMergedPairsControl);
			baiMergedPairsControlG.setSoftwareGeneratedById(gatk.getId());
			baiMergedPairsControlG.addDerivedFrom(bamMergedPairsControlG);
			baiMergedPairsControlG = fileService.saveInDiscreteTransaction(baiMergedPairsControlG, baiMergedPairsControl);
			outputFileGroups.add(baiMergedPairsControlG);
			
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
			sampleFileGroupsForNextStep.put(test, bamMergedPairsTestG);
			sampleFileGroupsForNextStep.put(control, bamMergedPairsControlG);
		}
		
		// re-align merged filegroups from previous step not in pairs
		for (Sample sample : mergedSampleFileGroupsIn.keySet()){
			if (processedSamples.contains(sample))
				continue;
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
			bamMergedG.setIsActive(0);
			bamMergedG.addFileHandle(bamMerged);
			bamMergedG.setFileType(bamFileType);
			bamMergedG.setDescription(bamOutputMerged);
			bamMergedG.setSoftwareGeneratedById(gatk.getId());
			bamMergedG.addDerivedFrom(mergedBam);
			bamMergedG.setSampleSources(mergedBam.getSampleSources());
			bamMergedG = fileService.saveInDiscreteTransaction(bamMergedG, bamMerged, gatkService.getCompleteGatkPreprocessBamFileAttributeSet());
			outputFileGroups.add(bamMergedG);

			FileGroup baiMergedG = new FileGroup();
			FileHandle baiMerged = new FileHandle();
			baiMerged.setFileName(baiOutputMerged);
			baiMergedG.setIsActive(0);
			baiMergedG.addFileHandle(baiMerged);
			baiMergedG.setFileType(baiFileType);
			baiMergedG.setDescription(baiOutputMerged);
			baiMergedG.setSoftwareGeneratedById(gatk.getId());
			baiMergedG.addDerivedFrom(mergedBam);
			baiMergedG.setSampleSources(mergedBam.getSampleSources());
			baiMergedG = fileService.saveInDiscreteTransaction(baiMergedG, baiMerged);
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
			sampleFileGroupsForNextStep.put(sample, bamMergedG);
		}
		
		// prepare the list of remaining filegroups for samples not processed above.
		for (Sample sample : allFgIn.keySet())
			if (! processedSamples.contains(sample)){
				FileGroup fg = allFgIn.get(sample);
				sampleFileGroupsForNextStep.put(sample, fg);
			}
		
		// put files needed for next step into step execution context to be promoted to job context
		getStepExecution().getExecutionContext().put("sampleFgMap", AbstractGatkTasklet.getSampleFgMapAsJsonString(sampleFileGroupsForNextStep));
		getStepExecution().getExecutionContext().put("temporaryFileSet", AbstractGatkTasklet.getModelIdsAsCommaDelimitedString(temporaryFileSet));
	}
}
