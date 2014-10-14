package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.filetype.FileTypeAttribute;
import edu.yu.einstein.wasp.gatk.service.GatkService;
import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.VcfFileTypeAttribute;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.genomemetadata.batch.tasklet.TestForGenomeIndexTasklet;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;

public class JointGenotypingTasklet extends TestForGenomeIndexTasklet {
	
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
	private GatkService gatkService;
	
	@Autowired
	protected GenomeService genomeService;
	
	@Autowired
	private GATKSoftwareComponent gatk;
	
	private Integer jobId;
	
	private Build build = null;
	
	public JointGenotypingTasklet(Integer jobId) {
		this.jobId = jobId;
	}

	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		
		
	
		GridResult result = executeWorkUnit(context);
		
		//place the grid result in the step context
		saveGridResult(context, result);
	}
	
	@Override
	@Transactional("entityManager")
	public void beforeStep(StepExecution stepExecution) {
		super.beforeStep(stepExecution);
	}

	@Transactional("entityManager")
	@Override
	public void doPreFinish(ChunkContext context) throws Exception {
		ExecutionContext stepExecutionContext = getStepExecutionContext(context);
		if (stepExecutionContext.containsKey("combinedGenotypedVcfFgId")){
			Integer rawVcfFgId = Integer.parseInt(stepExecutionContext.getString("combinedGenotypedVcfFgId"));
			logger.debug("Setting as active FileGroup with id=: " + rawVcfFgId);
			fileService.getFileGroupById(rawVcfFgId).setIsActive(1);
		}
	}

	@Override
	@Transactional("entityManager")
	public GenomeIndexStatus getGenomeIndexStatus(StepExecution stepExecution) {
		try {
			return genomeMetadataService.getFastaStatus(getGridWorkService(getStepExecutionContext(stepExecution)), build);
		} catch (GridUnresolvableHostException | IOException e) {
			String mess = "Unable to determine build or build status " + e.getLocalizedMessage();
			logger.error(mess);
			throw new WaspRuntimeException(mess);
		}
	}

	@Override
	@Transactional("entityManager")
	public WorkUnit prepareWorkUnit(StepExecution stepExecution) throws Exception {
		LinkedHashSet<FileGroup> inputFileGroups = new LinkedHashSet<>();
		LinkedHashSet<FileGroup> temporaryFileSet = new LinkedHashSet<>();
		ExecutionContext jobExecutionContext = getJobExecutionContext(stepExecution);
		ExecutionContext stepExecutionContext = getStepExecutionContext(stepExecution);
		if (jobExecutionContext.containsKey("gvcfFgSet"))
			inputFileGroups.addAll(AbstractGatkTasklet.getFileGroupsFromCommaDelimitedString(jobExecutionContext.getString("gvcfFgSet"), fileService));
		if (jobExecutionContext.containsKey("temporaryFileSet"))
			temporaryFileSet = AbstractGatkTasklet.getFileGroupsFromCommaDelimitedString(jobExecutionContext.getString("temporaryFileSet"), fileService);
		// clean up temporary files (e.g. intermediate bams) from the server to save space
		// this leaves the records in the database but marks them as inactive and deleted
		for (FileGroup fg : temporaryFileSet) 
			fileService.removeFileGroupFromRemoteServerAndMarkDeleted(fg);
		Build build = null;
		
		LinkedHashSet<FileHandle> outFiles = new LinkedHashSet<FileHandle>();
		
		Job job = jobService.getJobByJobId(jobId);
		String rawVcfOutFileName = fileService.generateUniqueBaseFileName(job) + "raw.vcf";
		FileGroup rawVcfOutG = new FileGroup();
		FileHandle rawVcfOut = new FileHandle();
		rawVcfOut.setFileName(rawVcfOutFileName);
		rawVcfOutG.setIsActive(0);
		rawVcfOutG.addFileHandle(rawVcfOut);
		outFiles.add(rawVcfOut);
		rawVcfOutG.setFileType(vcfFileType);
		rawVcfOutG.setDescription(rawVcfOutFileName);
		rawVcfOutG.setSoftwareGeneratedById(gatk.getId());
		rawVcfOutG.setDerivedFrom(inputFileGroups);
		Set<FileTypeAttribute> fta = new HashSet<FileTypeAttribute>();
		fta.add(VcfFileTypeAttribute.ANNOTATED);
		rawVcfOutG = fileService.saveInDiscreteTransaction(rawVcfOutG, fta);
		stepExecutionContext.putString("combinedGenotypedVcfFgId", rawVcfOutG.getId().toString());
				
		WorkUnit w = new WorkUnit();
		w.setMode(ExecutionMode.PROCESS);
		w.setProcessMode(ProcessMode.MAX);
		w.setMemoryRequirements(AbstractGatkTasklet.MEMORY_GB_16);
		w.setSecureResults(true);
		w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		w.setResultsDirectory(fileService.generateJobSoftwareBaseFolderName(job, gatk));
		w.setResultFiles(outFiles);
		List<FileHandle> fhlist = new ArrayList<FileHandle>();
		for (FileGroup fg : inputFileGroups){
			if (fhlist.isEmpty()) // first entry not yet entered
				build = gatkService.getBuildForFg(fg);
			fhlist.addAll(fg.getFileHandles());
		}
		w.setRequiredFiles(fhlist);
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(gatk);
		w.setSoftwareDependencies(sd);
		LinkedHashSet<String> inputFileNames = new LinkedHashSet<>();
		for (int i=0; i < fhlist.size(); i++)
			inputFileNames.add("${" + WorkUnit.INPUT_FILE + "[" + i + "]}");
		String rawVcfFilename = "${" + WorkUnit.OUTPUT_FILE + "[0]}";
		String referenceGenomeFile = genomeMetadataService.getRemoteGenomeFastaPath(getGridWorkService(stepExecutionContext), build);
		w.setCommand(gatk.genotypeGVCFs(inputFileNames, rawVcfFilename, referenceGenomeFile, AbstractGatkTasklet.MEMORY_GB_16));
		return w;
	}
	

}
