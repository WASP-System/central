package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;

public class JointGenotypingTasklet extends WaspRemotingTasklet {
	
	private static Logger logger = LoggerFactory.getLogger(JointGenotypingTasklet.class);
	
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
	
	public JointGenotypingTasklet(Integer jobId) {
		this.jobId = jobId;
	}

	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		Job job = jobService.getJobByJobId(jobId);
		Assert.assertTrue(job.getId() > 0);
		LinkedHashSet<FileGroup> inputFileGroups = new LinkedHashSet<>();
		LinkedHashSet<FileGroup> outputFileGroups = new LinkedHashSet<>();
		ExecutionContext jobExecutionContext = context.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		if (jobExecutionContext.containsKey("fgSet"))
			inputFileGroups.addAll(AbstractGatkTasklet.getFileGroupsFromCommaDelimitedString(jobExecutionContext.getString("fgSet"), fileService));
		Set<SampleSource> sampleSources = new HashSet<>();
		for (FileGroup fg : inputFileGroups)
			sampleSources.addAll(fg.getSampleSources());
		String vcfOutFileName = fileService.generateUniqueBaseFileName(job) + "vcfOut.vcf";
		FileGroup vcfOutG = new FileGroup();
		FileHandle vcfOut = new FileHandle();
		vcfOut.setFileName(vcfOutFileName);
		vcfOut = fileService.addFile(vcfOut);
		vcfOutG.setIsActive(0);
		vcfOutG.addFileHandle(vcfOut);
		vcfOutG.setFileType(vcfFileType);
		vcfOutG.setDescription(vcfOutFileName);
		vcfOutG.setSoftwareGeneratedById(gatk.getId());
		vcfOutG = fileService.addFileGroup(vcfOutG);
		vcfOutG.setDerivedFrom(inputFileGroups);
		vcfOutG.setSampleSources(sampleSources);
		outputFileGroups.add(vcfOutG);

	}
	

}
