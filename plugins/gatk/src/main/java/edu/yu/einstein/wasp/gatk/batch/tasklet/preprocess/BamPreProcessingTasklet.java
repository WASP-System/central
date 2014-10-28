package edu.yu.einstein.wasp.gatk.batch.tasklet.preprocess;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.filetype.service.FileTypeService;
import edu.yu.einstein.wasp.gatk.batch.tasklet.discovery.AbstractGatkTasklet;
import edu.yu.einstein.wasp.gatk.service.GatkService;
import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ProcessMode;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.BamFileTypeAttribute;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.genomemetadata.batch.tasklet.TestForGenomeIndexTasklet;
import edu.yu.einstein.wasp.plugin.genomemetadata.plugin.GenomeMetadataPlugin.VCF_TYPE;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * @author jcai
 * @author asmclellan
 */
public class BamPreProcessingTasklet extends TestForGenomeIndexTasklet implements StepExecutionListener {

	private Integer cellLibraryId;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	@Qualifier("bamServiceImpl")
	private FileTypeService fileTypeService;

	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private GridHostResolver gridHostResolver;
	
	@Autowired
	private FileType bamFileType;
	
	@Autowired
	private FileType baiFileType;
	
	@Autowired
	private GenomeService genomeService;
	
	@Autowired
	private GatkService gatkService;
	
	@Autowired
	private GATKSoftwareComponent gatk;
	
	public BamPreProcessingTasklet() {
		// proxy
	}

	public BamPreProcessingTasklet(String cellLibraryIds) {
		List<Integer> cids = WaspSoftwareJobParameters.getCellLibraryIdList(cellLibraryIds);
		Assert.assertTrue(cids.size() == 1);
		this.cellLibraryId = cids.get(0);
	}

	@Override
	@Transactional("entityManager")
	public GridResult doExecute(ChunkContext context) throws Exception {
		return executeWorkUnit(context);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public void doPreFinish(ChunkContext context) throws Exception {
		ExecutionContext stepExecutionContext = context.getStepContext().getStepExecution().getExecutionContext();
		Integer bamGId = stepExecutionContext.getInt("bamGID");
		Integer baiGId = stepExecutionContext.getInt("baiGID");
		
		// register .bam and .bai file groups with cellLib so as to make available to views
		if (bamGId != null)
			fileService.getFileGroupById(bamGId).setIsActive(1);
		if (baiGId != null)
			fileService.getFileGroupById(baiGId).setIsActive(1);
	}
	
	
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return super.afterStep(stepExecution);
	}

	@Override
	@Transactional("entityManager")
	public void beforeStep(StepExecution stepExecution){
		super.beforeStep(stepExecution);
	}

	@Override
	public GenomeIndexStatus getGenomeIndexStatus(StepExecution stepExecution) {
		ExecutionContext stepExecutionContext = getStepExecutionContext(stepExecution);
		try {
			SampleSource cellLib = sampleService.getSampleSourceDao().findById(cellLibraryId);
			Build build = genomeService.getGenomeBuild(cellLib);
			GenomeIndexStatus fq = genomeMetadataService.getFastaStatus(getGridWorkService(stepExecutionContext), build);
			GenomeIndexStatus s1 = genomeMetadataService.getVcfStatus(getGridWorkService(stepExecutionContext), build, genomeMetadataService.getDefaultVcf(build, VCF_TYPE.INDEL));
			GenomeIndexStatus s2 = genomeMetadataService.getVcfStatus(getGridWorkService(stepExecutionContext), build, genomeMetadataService.getDefaultVcf(build, VCF_TYPE.SNP));
			if (! (fq.isAvailable() && s1.isAvailable() && s2.isAvailable()) ) {
				return GenomeIndexStatus.UNBUILDABLE;
			} 
			if (fq.isCurrentlyAvailable() && s1.isCurrentlyAvailable() && s2.isCurrentlyAvailable()) {
				return GenomeIndexStatus.BUILT;
			} else {
				return GenomeIndexStatus.BUILDING;
			}
		} catch (GridUnresolvableHostException | IOException | MetadataException e) {
			String mess = "Unable to determine build or build status " + e.getLocalizedMessage();
			logger.error(mess);
			throw new WaspRuntimeException(mess);
		}
	}

	@Override
	public WorkUnitGridConfiguration configureWorkUnit(StepExecution stepExecution) throws Exception {
		Job job = getJobForCellLibrary();
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		c.setMode(ExecutionMode.PROCESS);
		c.setMemoryRequirements(AbstractGatkTasklet.MEMORY_GB_8);
		c.setProcessMode(ProcessMode.MAX);
		c.setWorkingDirectory(WorkUnitGridConfiguration.SCRATCH_DIR_PLACEHOLDER);
		c.setResultsDirectory(fileService.generateJobSoftwareBaseFolderName(job, gatk));
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(gatk);
		c.setSoftwareDependencies(sd);
		return c;
	}

	@Override
	@Transactional("entityManager")
	public WorkUnit buildWorkUnit(StepExecution stepExecution) throws Exception {
		ExecutionContext stepExecutionContext = getStepExecutionContext(stepExecution);
		SampleSource cellLib = sampleService.getSampleSourceDao().findById(cellLibraryId);
		Build build = genomeService.getGenomeBuild(cellLib);
		
		Job job = getJobForCellLibrary();
		
		logger.debug("Beginning GATK preprocessing for cellLibrary " + cellLib.getId() + " from Wasp job " + job.getId());
		
		Set<BamFileTypeAttribute> attributes = new HashSet<>();
		attributes.add(BamFileTypeAttribute.SORTED);
		Set<FileGroup> sourceBamFileGroups = fileService.getFilesForCellLibraryByType(cellLib, bamFileType, attributes, false);
		logger.debug("# bam FileGroups (sorted) for cell library id=" + cellLib.getId() + " is " +sourceBamFileGroups.size());
		Assert.assertTrue(sourceBamFileGroups.size() == 1, "The number of filegroups (" + sourceBamFileGroups.size() + ") is not equal to 1");
		FileGroup fg = sourceBamFileGroups.iterator().next();
		List<FileHandle> fhlist = new ArrayList<FileHandle>();
		fhlist.addAll(fg.getFileHandles());
		
		logger.debug("Bam File group: " + fg.getId() + ": " + fg.getDescription());
		boolean isDedup = false;
		if (fileTypeService.hasAttribute(fg, BamFileTypeAttribute.DEDUP))
			isDedup = true;
		WorkUnit w = new WorkUnit(configureWorkUnit(stepExecution));
		LinkedHashSet<FileHandle> files = new LinkedHashSet<FileHandle>();
		
		w.setRequiredFiles(fhlist);
		w.setSecureResults(true);
		String fileNameSuffix = "gatk_realn_recal";
		if (isDedup)
			fileNameSuffix = "gatk_dedup_realn_recal";
		String bamOutput = fileService.generateUniqueBaseFileName(cellLib) + fileNameSuffix + ".bam";
		String baiOutput = fileService.generateUniqueBaseFileName(cellLib) + fileNameSuffix + ".bai";
		FileGroup bamG = new FileGroup();
		FileHandle bam = new FileHandle();
		bam.setFileName(bamOutput);
		bam.setFileType(bamFileType);
		bam = fileService.addFile(bam);
		bamG.addFileHandle(bam);
		files.add(bam);
		bamG.setFileType(bamFileType);
		bamG.setDescription(bamOutput);
		bamG.setSoftwareGeneratedById(gatk.getId());
		bamG.addDerivedFrom(fg);
		bamG = fileService.addFileGroup(bamG);
		fileTypeService.setAttributes(bamG, gatkService.getCompleteGatkPreprocessBamFileAttributeSet(isDedup));
		Integer bamGId = bamG.getId();
		// save in step context  for use later
		stepExecutionContext.put("bamGID", bamGId);
		
		FileGroup baiG = new FileGroup();
		FileHandle bai = new FileHandle();
		bai.setFileName(baiOutput);
		bai.setFileType(baiFileType);
		bai = fileService.addFile(bai);
		baiG.addFileHandle(bai);
		files.add(bai);
		baiG.setFileType(baiFileType);
		baiG.setDescription(baiOutput);
		baiG.setSoftwareGeneratedById(gatk.getId());
		baiG = fileService.addFileGroup(baiG);
		baiG.addDerivedFrom(bamG);
		Integer baiGId = baiG.getId();
		// save in step context for use later
		stepExecutionContext.put("baiGID", baiGId);
		
		w.setResultFiles(files);

		String inputBamFilename = "${" + WorkUnit.INPUT_FILE + "}";
		String intervalFileName = "gatk.${" + WorkUnit.OUTPUT_FILE + "}.realign.intervals";
		String realignBamFilename = "gatk.${" + WorkUnit.OUTPUT_FILE + "}.realign.bam";
		String recaliGrpFilename = "gatk.${" + WorkUnit.OUTPUT_FILE + "}.recali.grp";
		String recaliBamFilename = "${" + WorkUnit.OUTPUT_FILE + "[0]}";
		String recaliBaiFilename = "${" + WorkUnit.OUTPUT_FILE + "[1]}";
		Set<String> inputFilenames = new HashSet<>();
		inputFilenames.add(inputBamFilename);
		w.addCommand(gatk.getCreateTargetCmd(getGridWorkService(stepExecutionContext), build, inputFilenames, intervalFileName, AbstractGatkTasklet.MEMORY_GB_8));
		w.addCommand(gatk.getLocalAlignCmd(getGridWorkService(stepExecutionContext), build, inputFilenames, intervalFileName, realignBamFilename, null, AbstractGatkTasklet.MEMORY_GB_8));
		w.addCommand(gatk.getRecaliTableCmd(getGridWorkService(stepExecutionContext), build, realignBamFilename, recaliGrpFilename, AbstractGatkTasklet.MEMORY_GB_8));
		w.addCommand(gatk.getPrintRecaliCmd(getGridWorkService(stepExecutionContext), build, realignBamFilename, recaliGrpFilename, recaliBamFilename, recaliBaiFilename, AbstractGatkTasklet.MEMORY_GB_8));
		return w;
	}
	
	private Job getJobForCellLibrary(){
		SampleSource cellLib = sampleService.getSampleSourceDao().findById(cellLibraryId);
		return sampleService.getJobOfLibraryOnCell(cellLib);
	}

	@Override
	public void doCleanupBeforeRestart(ChunkContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
