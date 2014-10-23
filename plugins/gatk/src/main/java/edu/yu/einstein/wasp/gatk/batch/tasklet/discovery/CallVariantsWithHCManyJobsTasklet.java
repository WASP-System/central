package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

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
import edu.yu.einstein.wasp.filetype.FileTypeAttribute;
import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.VcfFileTypeAttribute;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;

public class CallVariantsWithHCManyJobsTasklet extends LaunchManyJobsTasklet {
	
	private static Logger logger = LoggerFactory.getLogger(CallVariantsWithHCManyJobsTasklet.class);

	@Autowired
	private JobService jobService;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private FileType vcfFileType;
	
	@Autowired
	private GATKSoftwareComponent gatk;

	private Integer jobId;
	
	public CallVariantsWithHCManyJobsTasklet(Integer jobId) {
		this.jobId = jobId;
	}

	@Override
	@Transactional("entityManager")
	public void doExecute() {
		Job job = jobService.getJobByJobId(jobId);
		Assert.assertTrue(job.getId() > 0);
		Map<Sample, FileGroup> allsampleFgIn = new HashMap<>();
		ExecutionContext jobExecutionContext = this.getStepExecution().getJobExecution().getExecutionContext();
		logger.debug("Getting FileGroup ids passed in from previous step");
		if (jobExecutionContext.containsKey("sampleFgMap"))
			allsampleFgIn.putAll(AbstractGatkTasklet.getSampleFgMapFromJsonString(jobExecutionContext.getString("sampleFgMap"), sampleService, fileService));
		LinkedHashSet<FileGroup> fileGroupsForNextStep = new LinkedHashSet<>();
		for (Sample sample : allsampleFgIn.keySet()){
			FileGroup inputFile = allsampleFgIn.get(sample);
			LinkedHashSet<FileGroup> inputFileGroups = new LinkedHashSet<>();
			LinkedHashSet<FileGroup> outputFileGroups = new LinkedHashSet<>();
			inputFileGroups.add(inputFile);
			String gvcfFileName = fileService.generateUniqueBaseFileName(sample) + "gvcf.vcf";
			FileGroup gvcfG = new FileGroup();
			FileHandle gvcf = new FileHandle();
			gvcf.setFileName(gvcfFileName);
			gvcf.setFileType(vcfFileType);
			gvcfG.setIsActive(0);
			gvcfG.addFileHandle(gvcf);
			gvcfG.setFileType(vcfFileType);
			gvcfG.setDescription(gvcfFileName);
			gvcfG.setSoftwareGeneratedById(gatk.getId());
			gvcfG.addDerivedFrom(inputFile);
			Set<FileTypeAttribute> fta = new HashSet<FileTypeAttribute>();
			fta.add(VcfFileTypeAttribute.GVCF);
			gvcfG = fileService.saveInDiscreteTransaction(gvcfG, fta);
			outputFileGroups.add(gvcfG);
			fileGroupsForNextStep.add(gvcfG);
			Map<String, String> jobParameters = new HashMap<>();
			//jobParameters.put("uniqCode", Long.toString(Calendar.getInstance().getTimeInMillis())); // overcomes limitation of job being run only once
			jobParameters.put("parentJobExecutionId", getStepExecution().getJobExecutionId().toString());
			jobParameters.put(WaspSoftwareJobParameters.FILEGROUP_ID_LIST_INPUT, AbstractGatkTasklet.getModelIdsAsCommaDelimitedString(inputFileGroups));
			jobParameters.put(WaspSoftwareJobParameters.FILEGROUP_ID_LIST_OUTPUT, AbstractGatkTasklet.getModelIdsAsCommaDelimitedString(outputFileGroups));
			jobParameters.put(WaspSoftwareJobParameters.JOB_ID, jobId.toString());
			try {
				requestLaunch("gatk.variantDiscovery.hc.callVariants.jobFlow", jobParameters);
			} catch (WaspMessageBuildingException e) {
				e.printStackTrace();
			}
		}
		// put files needed for next step into step execution context to be promoted to job context
		getStepExecution().getExecutionContext().put("gvcfFgSet", AbstractGatkTasklet.getModelIdsAsCommaDelimitedString(fileGroupsForNextStep));

	}

}
