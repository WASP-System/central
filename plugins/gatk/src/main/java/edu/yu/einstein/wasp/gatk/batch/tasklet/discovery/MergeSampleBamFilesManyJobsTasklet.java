package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.LaunchManyJobsTasklet;
import edu.yu.einstein.wasp.exception.SampleTypeException;
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
public class MergeSampleBamFilesManyJobsTasklet extends LaunchManyJobsTasklet {
		
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
	private FileType textFileType;
	
	@Autowired
	private GATKSoftwareComponent gatk;
	
	private Integer jobId;

	public MergeSampleBamFilesManyJobsTasklet(Integer jobId) {
		this.jobId = jobId;
	}
	
	@Override
	@Transactional("EntityManager")
	public void doExecute() {
		Job job = jobService.getJobByJobId(jobId);
		Assert.assertTrue(job.getId() > 0);
		Map<Sample, LinkedHashSet<FileGroup>> sampleFileGroupsForNextStep = new HashMap<>();
		Map<Sample, LinkedHashSet<FileGroup>> sampleFileGroups = new HashMap<>();
		Map<Sample, LinkedHashSet<SampleSource>> sampleCellLibraries = new HashMap<>();
		try {
			for (SampleSource cl: sampleService.getCellLibrariesThatPassedQCForJob(job)){
				Sample sample = sampleService.getLibrary(cl);
				if (sample.getParent() != null)
					sample = sample.getParent();
				for (FileGroup fg : fileService.getFilesForCellLibraryByType(cl, bamFileType, gatkService.getCompleteGatkPreprocessBamFileAttributeSet(), true)){
					if (!sampleFileGroups.containsKey(sample)){
						sampleFileGroups.put(sample, new LinkedHashSet<FileGroup>());
						sampleCellLibraries.put(sample, new LinkedHashSet<SampleSource>());
					}
					sampleFileGroups.get(sample).add(fg);
					sampleCellLibraries.get(sample).add(cl);
				}
			}
		} catch (SampleTypeException e1) {
			e1.printStackTrace();
		}
		for (Sample sample: sampleFileGroups.keySet()){
			if (!sampleFileGroupsForNextStep.containsKey(sample))
				sampleFileGroupsForNextStep.put(sample, new LinkedHashSet<FileGroup>());
			LinkedHashSet<FileGroup> inputFileGroups = new LinkedHashSet<FileGroup>(sampleFileGroups.get(sample));
			LinkedHashSet<FileGroup> outputFileGroups = new LinkedHashSet<>();
			if (inputFileGroups.size() > 1){
				Map<String, String> jobParameters = new HashMap<>();
				String bamOutput = fileService.generateUniqueBaseFileName(sample) + "gatk_preproc_merged_dedup.bam";
				String baiOutput = fileService.generateUniqueBaseFileName(sample) + "gatk_preproc_merged_dedup.bai";
				FileGroup bamG = new FileGroup();
				FileHandle bam = new FileHandle();
				bam.setFileName(bamOutput);
				bam = fileService.addFile(bam);
				bamG.setIsActive(0);
				bamG.addFileHandle(bam);
				bamG.setFileType(bamFileType);
				bamG.setDescription(bamOutput);
				bamG.setSoftwareGeneratedById(gatk.getId());
				bamG = fileService.addFileGroup(bamG);
				bamG.setDerivedFrom(inputFileGroups);
				bamG.setSampleSources(sampleCellLibraries.get(sample));
				fileTypeService.setAttributes(bamG, gatkService.getCompleteGatkPreprocessBamFileAttributeSet());
				outputFileGroups.add(bamG);
				sampleFileGroupsForNextStep.get(sample).add(bamG);
	
				FileGroup baiG = new FileGroup();
				FileHandle bai = new FileHandle();
				bai.setFileName(baiOutput);
				bai = fileService.addFile(bai);
				baiG.setIsActive(0);
				baiG.addFileHandle(bai);
				baiG.setFileType(baiFileType);
				baiG.setDescription(baiOutput);
				baiG.setSoftwareGeneratedById(gatk.getId());
				baiG = fileService.addFileGroup(baiG);
				baiG.setDerivedFrom(inputFileGroups);
				baiG.setSampleSources(sampleCellLibraries.get(sample));
				outputFileGroups.add(baiG);
				
				String metricsOutput = fileService.generateUniqueBaseFileName(sample) + "gatk_preproc_merged_dedupMetrics.txt";
				FileGroup metricsG = new FileGroup();
				FileHandle metrics = new FileHandle();
				metrics.setFileName(metricsOutput);
				metrics.setFileType(textFileType);
				metrics = fileService.addFile(metrics);
				metricsG.setIsActive(0);
				metricsG.addFileHandle(metrics);
				metricsG.setFileType(textFileType);
				metricsG.setDescription(metricsOutput);
				metricsG.setSoftwareGeneratedBy(gatk);
				metricsG = fileService.addFileGroup(metricsG);
				metricsG.setSampleSources(sampleCellLibraries.get(sample));
				outputFileGroups.add(metricsG);

				jobParameters.put(WaspSoftwareJobParameters.FILEGROUP_ID_LIST_INPUT, AbstractGatkTasklet.getFileGroupIdsAsCommaDelimitedString(inputFileGroups));
				jobParameters.put(WaspSoftwareJobParameters.FILEGROUP_ID_LIST_OUTPUT, AbstractGatkTasklet.getFileGroupIdsAsCommaDelimitedString(outputFileGroups));
				jobParameters.put(WaspSoftwareJobParameters.JOB_ID, jobId.toString());
				try {
					requestLaunch("gatk.variantDiscovery.hc.mergeSampleBamFiles.jobFlow", jobParameters);
				} catch (WaspMessageBuildingException e) {
					e.printStackTrace();
				}
			} else {
				sampleFileGroupsForNextStep.get(sample).addAll(sampleFileGroups.get(sample)); // should be no more than 1 entry
			}
			// put files needed for next step into step execution context to be promoted to job context
			getStepExecution().getExecutionContext().put("sampleFgMap", AbstractGatkTasklet.getSampleFgMapAsJsonString(sampleFileGroups));
		}
	}

}
