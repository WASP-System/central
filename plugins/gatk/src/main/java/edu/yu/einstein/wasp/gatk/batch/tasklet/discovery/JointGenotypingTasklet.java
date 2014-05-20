package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.filetype.service.FileTypeService;
import edu.yu.einstein.wasp.gatk.service.GatkService;
import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.VcfFileTypeAttribute;
import edu.yu.einstein.wasp.plugin.mps.grid.software.SnpEff;
import edu.yu.einstein.wasp.plugin.mps.grid.software.VcfTools;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;

public class JointGenotypingTasklet extends WaspRemotingTasklet {
	
	private static Logger logger = LoggerFactory.getLogger(JointGenotypingTasklet.class);
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private FileTypeService fileTypeService;
	
	@Autowired
	private FileType vcfFileType;
	
	@Autowired
	private GatkService gatkService;
	
	@Autowired
	private GATKSoftwareComponent gatk;
	
	@Autowired
	private GridHostResolver gridHostResolver;
	
	private Integer jobId;
	
	public JointGenotypingTasklet(Integer jobId) {
		this.jobId = jobId;
	}

	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		LinkedHashSet<FileGroup> inputFileGroups = new LinkedHashSet<>();
		LinkedHashSet<FileGroup> temporaryFileSet = new LinkedHashSet<>();
		ExecutionContext jobExecutionContext = context.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		if (jobExecutionContext.containsKey("gvcfFgSet"))
			inputFileGroups.addAll(AbstractGatkTasklet.getFileGroupsFromCommaDelimitedString(jobExecutionContext.getString("gvcfFgSet"), fileService));
		if (jobExecutionContext.containsKey("temporaryFileSet"))
			temporaryFileSet = AbstractGatkTasklet.getFileGroupsFromCommaDelimitedString(jobExecutionContext.getString("temporaryFileSet"), fileService);
		// clean up temporary files (e.g. intermediate bams) from the server to save space
		// this leaves the records in the database but marks them as inactive and deleted
		for (FileGroup fg : temporaryFileSet) 
			fileService.removeFileGroupFromRemoteServerAndMarkDeleted(fg);
		Build build = null;
		Job job = jobService.getJobByJobId(jobId);
		Assert.assertTrue(job.getId() > 0);
		Set<SampleSource> sampleSources = new HashSet<>();
		for (FileGroup fg : inputFileGroups)
			sampleSources.addAll(fg.getSampleSources());
		String rawVcfOutFileName = fileService.generateUniqueBaseFileName(job) + "raw.vcf";
		FileGroup rawVcfOutG = new FileGroup();
		FileHandle rawVcfOut = new FileHandle();
		rawVcfOut.setFileName(rawVcfOutFileName);
		rawVcfOut = fileService.addFile(rawVcfOut);
		rawVcfOutG.setIsActive(0);
		rawVcfOutG.addFileHandle(rawVcfOut);
		rawVcfOutG.setFileType(vcfFileType);
		rawVcfOutG.setDescription(rawVcfOutFileName);
		rawVcfOutG.setSoftwareGeneratedById(gatk.getId());
		rawVcfOutG = fileService.addFileGroup(rawVcfOutG);
		rawVcfOutG.setDerivedFrom(inputFileGroups);
		rawVcfOutG.setSampleSources(sampleSources);
		
		String annotatedVcfOutFileName = fileService.generateUniqueBaseFileName(job) + "annotatedt.vcf";
		FileGroup annotatedVcfOutG = new FileGroup();
		FileHandle annotatedVcfOut = new FileHandle();
		annotatedVcfOut.setFileName(annotatedVcfOutFileName);
		annotatedVcfOut = fileService.addFile(annotatedVcfOut);
		annotatedVcfOutG.setIsActive(0);
		annotatedVcfOutG.addFileHandle(annotatedVcfOut);
		annotatedVcfOutG.setFileType(vcfFileType);
		annotatedVcfOutG.setDescription(annotatedVcfOutFileName);
		annotatedVcfOutG.setSoftwareGeneratedById(gatk.getId());
		annotatedVcfOutG = fileService.addFileGroup(annotatedVcfOutG);
		fileTypeService.addAttribute(annotatedVcfOutG, VcfFileTypeAttribute.ANNOTATED);
		annotatedVcfOutG.addDerivedFrom(rawVcfOutG);
		annotatedVcfOutG.setSampleSources(sampleSources);
		
		
		WorkUnit w = new WorkUnit();
		w.setMode(ExecutionMode.PROCESS);
		w.setProcessMode(ProcessMode.MAX);
		w.setMemoryRequirements(AbstractGatkTasklet.MEMORY_GB_8);
		w.setProcessorRequirements(AbstractGatkTasklet.THREADS_8);
		w.setSecureResults(true);
		w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		w.setResultsDirectory(fileService.generateJobSoftwareBaseFolderName(job, gatk));
		w.addResultFiles(rawVcfOutG);
		w.addResultFiles(annotatedVcfOutG);
		List<FileHandle> fhlist = new ArrayList<FileHandle>();
		for (FileGroup fg : inputFileGroups){
			if (fhlist.isEmpty()) // first entry not yet entered
				build = gatkService.getBuildForFg(fg);
			fhlist.addAll(fg.getFileHandles());
		}
		w.setRequiredFiles(fhlist);
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		VcfTools vcfTools = (VcfTools) gatk.getSoftwareDependencyByIname("vcfTools");
		SnpEff snpEff = (SnpEff) gatk.getSoftwareDependencyByIname("SnpEff");
		sd.add(gatk);
		sd.add(snpEff);
		w.setSoftwareDependencies(sd);
		LinkedHashSet<String> inputFileNames = new LinkedHashSet<>();
		for (int i=0; i < fhlist.size(); i++)
			inputFileNames.add("${" + WorkUnit.INPUT_FILE + "[" + i + "]}");
		String rawVcfFilename = "${" + WorkUnit.OUTPUT_FILE + "[0]}";
		String annotatedVcfFilename = "${" + WorkUnit.OUTPUT_FILE + "[1]}";
		w.setCommand(gatk.genotypeGVCFs(inputFileNames, rawVcfFilename, build, AbstractGatkTasklet.MEMORY_GB_8, AbstractGatkTasklet.THREADS_8));
		
		// we have already added snp database Ids during haplotype calling. We will now also add indel database ids
		w.setCommand(snpEff.getAnnotateIdsCommand(rawVcfFilename, gatkService.getReferenceIndelsVcfFile(build), annotatedVcfFilename));

		GridResult result = gridHostResolver.execute(w);
		
		context.getStepContext().getStepExecution().getExecutionContext().put("combinedGenotypedVcfFgId", annotatedVcfOutG.getId().toString());
		
		//place the grid result in the step context
		storeStartedResult(context, result);
	}
	

}
