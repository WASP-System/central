/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.helptagham.batch.tasklet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.helptagham.service.HelptaghamService;
import edu.yu.einstein.wasp.helptagham.software.Helptagham;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * @author 
 * 
 */
public class HelptaghamTasklet extends WaspRemotingTasklet implements StepExecutionListener {
	
	@Autowired
	private JobService jobService;
	
	private Integer jobId;
	private String hpa2CellLibraryIdListAsString;
	private String msp1CellLibraryIdListAsString;
	private List<Integer> hpa2CellLibraryIdList;// treated, such as IP
	private List<Integer> msp1CellLibraryIdList;// contol, such as input
	
	private Integer hpa2SampleId;
	private Integer msp1SampleId;
	private String commandLineCall;
	private String softwareIdUsedListAsString;
	private Integer helptaghamAnalysisFileGroupId;

	private StepExecution stepExecution;

	@Autowired
	private GridHostResolver gridHostResolver;
	
	@Autowired
	private FileType hcountFileType;

	@Autowired
	private FileType wigFileType;

	@Autowired
	private FileType htgAngleFileType;

	@Autowired
	private FileType textFileType;

	@Autowired
	private FileType bedFileType;

	@Autowired
	private SampleService sampleService;
	@Autowired
	private FileService fileService;
	@Autowired
	private GenomeService genomeService;
	@Autowired
	private RunService runService;

	@Autowired
	private Helptagham helptagHAM;

	@Autowired
	private HelptaghamService helptagHAMService;

	public HelptaghamTasklet() {
		// proxy
	}

	public HelptaghamTasklet(String waspJobId, String hpa2CellLibraryIdListAsString, String msp1CellLibraryIdListAsString) {
		Assert.assertTrue(!waspJobId.isEmpty());
		this.jobId = Integer.parseInt(waspJobId);
		Assert.assertTrue(this.jobId > 0);

		this.hpa2CellLibraryIdListAsString = hpa2CellLibraryIdListAsString;
		Assert.assertTrue(!hpa2CellLibraryIdListAsString.isEmpty());
		this.hpa2CellLibraryIdList = WaspSoftwareJobParameters.getCellLibraryIdList(hpa2CellLibraryIdListAsString);// should be all from same job

		Assert.assertTrue(!this.hpa2CellLibraryIdList.isEmpty());

		if (msp1CellLibraryIdListAsString == null || msp1CellLibraryIdListAsString.isEmpty()) {// could be empty!!
			this.msp1CellLibraryIdListAsString = "";
			this.msp1CellLibraryIdList = new ArrayList<Integer>();
		} else {
			this.msp1CellLibraryIdListAsString = msp1CellLibraryIdListAsString;
			this.msp1CellLibraryIdList = WaspSoftwareJobParameters.getCellLibraryIdList(msp1CellLibraryIdListAsString);// may be empty
			Assert.assertTrue(!this.msp1CellLibraryIdList.isEmpty());
		}

		logger.trace("in constructor this.jobId: " + this.jobId);
		logger.trace("in constructor hpa2CellLibraryIdList.size(): " + hpa2CellLibraryIdList.size());
		logger.trace("in constructor msp1CellLibraryIdList.size(): " + msp1CellLibraryIdList.size());
		logger.trace("Ending HAM constructor");
	}
	
	/**
	 * Setup work to be run remotely. This method is called during execution of the super.execute(contrib, context) method. 
	 * You must either call the super.execute method from the locally overridden method (as is the default below) or remove the local method 
	 * below to use the method in the parent class.
	 * @param context
	 * @throws Exception
	 */
	@Override
	@Transactional("entityManager") // transactional for wasp system entities (e.g. Job)
	public GridResult doExecute(ChunkContext context) throws Exception {
		logger.debug("Starting HAMTasklet execute");
		logger.debug("in doExecute this.jobId: " + this.jobId);
		logger.debug("in doExecute hpa2CellLibraryIdList.size(): " + hpa2CellLibraryIdList.size());
		logger.debug("in doExecute msp1CellLibraryIdList.size(): " + msp1CellLibraryIdList.size());
		
		// get step execution for this tasklet step
		this.stepExecution = context.getStepContext().getStepExecution();
		
		// get managed entity object for a wasp job by id 
		// Note: class or method must be public and be annotated with @Transactional("entityManager") otherwise
		// it will not be possible to inflate linked entities.
		Job job = jobService.getJobByJobId(jobId);
		
		// configure work unit. Configuration information may be used to select a resource best suited to running the 
		// batch job
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		c.setResultsDirectory(WorkUnitGridConfiguration.RESULTS_DIR_PLACEHOLDER + "/" + jobId);
		

		Map<String, Object> jobParametersMap = context.getStepContext().getJobParameters();
		for (String key : jobParametersMap.keySet()) {
			logger.debug("jobParameters Key: " + key + " Value: " + jobParametersMap.get(key).toString());
		}
		Map<String, Object> jobExecutionContextMap = context.getStepContext().getJobExecutionContext();
		for (String key : jobExecutionContextMap.keySet()) {
			logger.debug("jobExecutionContextMap Key: " + key + " Value: " + jobExecutionContextMap.get(key).toString());
		}

		SampleSource firstHpa2CellLibrary = sampleService.getCellLibraryBySampleSourceId(this.hpa2CellLibraryIdList.get(0));
		Sample hpa2Sample = sampleService.getLibrary(firstHpa2CellLibrary);// all these cellLibraries are from the same library or macromoleucle
		while (hpa2Sample.getParentId() != null) {
			hpa2Sample = sampleService.getSampleById(hpa2Sample.getParentId());
		}
		logger.debug("hpa2Sample.name = " + hpa2Sample.getName());
		this.hpa2SampleId = hpa2Sample.getId();

		Set<FileGroup> derrivedFromFileGroups = new HashSet<FileGroup>();

		List<FileHandle> hpa2FileHandleList = new ArrayList<FileHandle>();
		for (Integer id : this.hpa2CellLibraryIdList) {
			SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(id);
			// setOfCellLibrariesForDerivedFrom.add(cellLibrary);
			Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLibrary, hcountFileType);
			derrivedFromFileGroups.addAll(fileGroups);
			logger.debug("hpa2 fileGroups size = " + fileGroups.size());
			for (FileGroup fileGroup : fileGroups) {
				for (FileHandle fileHandle : fileGroup.getFileHandles()) {
					hpa2FileHandleList.add(fileHandle);
					logger.debug("hpa2 fileHandle = " + fileHandle.getFileName());
				}
			}
		}
		Assert.assertTrue(!hpa2FileHandleList.isEmpty());
		logger.debug("hpa2 hcount files size = " + hpa2FileHandleList.size());

		Sample msp1Sample = null;
		if (!msp1CellLibraryIdList.isEmpty()) {
			msp1Sample = sampleService.getLibrary(sampleService.getCellLibraryBySampleSourceId(msp1CellLibraryIdList.get(0)));// all these cellLibraries are
																															  // from the same library or
																															  // macromoleucle
			while (msp1Sample.getParentId() != null) {
				msp1Sample = sampleService.getSampleById(msp1Sample.getParentId());// msp1Sample.getParent();
			}
		}
		if (msp1Sample == null) {
			logger.debug("msp1Sample IS NULL so become Standard reference");
			this.msp1SampleId = 0;
		} else {
			logger.debug("msp1Sample.name = " + msp1Sample.getName());
			this.msp1SampleId = msp1Sample.getId();
		}

		List<FileHandle> msp1FileHandleList = new ArrayList<FileHandle>();
		for (Integer id : this.msp1CellLibraryIdList) {
			SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(id);
			// setOfCellLibrariesForDerivedFrom.add(cellLibrary);
			Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLibrary, hcountFileType);
			derrivedFromFileGroups.addAll(fileGroups);
			logger.debug("msp1 fileGroups size = " + fileGroups.size());
			for (FileGroup fileGroup : fileGroups) {
				for (FileHandle fileHandle : fileGroup.getFileHandles()) {
					msp1FileHandleList.add(fileHandle);// can be empty
					logger.debug("msp1 fileHandle = " + fileHandle.getFileName());
				}
			}
		}
		logger.debug("msp1FileHandleList.size = " + msp1FileHandleList.size());

		Date dateNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
		// new
		String prefixForFileName = ft.format(dateNow) + "_HPA_" + hpa2Sample.getName();
		if (msp1Sample != null) {
			prefixForFileName = prefixForFileName + "_MSP_" + msp1Sample.getName();
		} else {
			prefixForFileName = prefixForFileName + "_MSP_STDREF";
		}
		prefixForFileName = fileService.getSanitizedName(prefixForFileName);
		logger.debug("prefixForFileName = " + prefixForFileName);
		logger.debug("preparing to generate workunit");

		/*
		 * due to possible model building problem, move this elsewhere String modelFileName = prefixForFileName + "_model.r"; String pdfFileName =
		 * modelFileName.replaceAll(".r$", ".pdf");//abc_model.r will be used to generate abc_model.pdf String pngFileName = modelFileName.replaceAll(".r$",
		 * ".png");//abc_model.pdf will be used to generate abc_model.png
		 */

		Build build = genomeService.getBuild(hpa2Sample);
		String speciesName = build.getGenome().getOrganism().getName();// Homo sapiens
		String speciesCode = "";// for Homo sapiens, species code will be hs
		String[] stringArray = speciesName.split("\\s+");
		if (stringArray.length == 2) {
			speciesCode = stringArray[0].substring(0, 1).toLowerCase();
			speciesCode += stringArray[1].substring(0, 1).toLowerCase();
		}
		if (speciesCode.length() != 2) {
			speciesCode = "";
		}

		// prepare work unit (unit of work to be executed on configured resources e.g. a local cluster)
		WorkUnit w = helptagHAM.getAngleMaker(build.getGenome().getAlias(), prefixForFileName, hpa2FileHandleList, msp1FileHandleList);
		logger.debug("OK, workunit has been generated");
		this.commandLineCall = w.getCommand();
		this.commandLineCall = this.commandLineCall.replaceAll("\\n", "<br /><br />");// the workunit tagged on a newline at the end of the command; so remove
																					  // it for db storage and replace with <br /> for display purposes

		List<String> listOfFileHandleNames = new ArrayList<String>();
		LinkedHashSet<FileHandle> files = new LinkedHashSet<FileHandle>();
		Set<FileGroup> helptagHAMFileGroups = new LinkedHashSet<FileGroup>();

		FileHandle wigFileHandle = helptagHAMService.createAndSaveInnerFileHandle(prefixForFileName + ".wig", wigFileType);
		listOfFileHandleNames.add(wigFileHandle.getFileName());
		files.add(wigFileHandle);
		FileGroup wigFileGroup = helptagHAMService
				.createAndSaveInnerFileGroup(wigFileHandle, helptagHAM,
											 "HELP-tagging pipeline generated wiggle track file showing methylation histograms on hpaii loci");
		helptagHAMFileGroups.add(wigFileGroup);

		FileHandle angleFileHandle = helptagHAMService.createAndSaveInnerFileHandle(prefixForFileName + ".angle", htgAngleFileType);
		listOfFileHandleNames.add(angleFileHandle.getFileName());
		files.add(angleFileHandle);
		FileGroup angleFileGroup = helptagHAMService
				.createAndSaveInnerFileGroup(angleFileHandle, helptagHAM,
											 "HELP-tagging pipeline generated tab-delimited file storing methylation scores on hpaii loci");
		helptagHAMFileGroups.add(angleFileGroup);

		FileHandle confInfoFileHandle = helptagHAMService.createAndSaveInnerFileHandle(prefixForFileName + ".conf.txt", textFileType);
		listOfFileHandleNames.add(confInfoFileHandle.getFileName());
		files.add(confInfoFileHandle);
		FileGroup confInfoFileGroup = helptagHAMService
				.createAndSaveInnerFileGroup(confInfoFileHandle, helptagHAM,
											 "HELP-tagging pipeline generated plain text file storing confidence scores' distribution information");
		helptagHAMFileGroups.add(confInfoFileGroup);

		FileHandle bedFileHandle = helptagHAMService.createAndSaveInnerFileHandle(prefixForFileName + ".conf.bed", bedFileType);
		listOfFileHandleNames.add(bedFileHandle.getFileName());
		files.add(bedFileHandle);
		FileGroup bedFileGroup = helptagHAMService
				.createAndSaveInnerFileGroup(bedFileHandle, helptagHAM,
											 "HELP-tagging pipeline generated bed track showing the trimmed reads aligned to hpaii loci");
		helptagHAMFileGroups.add(bedFileGroup);

		FileHandle hcWigFileHandle = helptagHAMService.createAndSaveInnerFileHandle(prefixForFileName + ".hc.wig", wigFileType);
		listOfFileHandleNames.add(hcWigFileHandle.getFileName());
		files.add(hcWigFileHandle);
		FileGroup hcWigFileGroup = helptagHAMService
				.createAndSaveInnerFileGroup(hcWigFileHandle, helptagHAM,
											 "HELP-tagging pipeline generated wiggle track only showing the hpaii loci with high confidence scores");
		helptagHAMFileGroups.add(hcWigFileGroup);

		FileHandle mcWigFileHandle = helptagHAMService.createAndSaveInnerFileHandle(prefixForFileName + ".mc.wig", wigFileType);
		listOfFileHandleNames.add(mcWigFileHandle.getFileName());
		files.add(mcWigFileHandle);
		FileGroup mcWigFileGroup = helptagHAMService
				.createAndSaveInnerFileGroup(mcWigFileHandle, helptagHAM,
											 "HELP-tagging pipeline generated wiggle track only showing the hpaii loci with medium confidence scores");
		helptagHAMFileGroups.add(mcWigFileGroup);

		FileHandle lcWigFileHandle = helptagHAMService.createAndSaveInnerFileHandle(prefixForFileName + ".lc.wig", wigFileType);
		listOfFileHandleNames.add(lcWigFileHandle.getFileName());
		files.add(lcWigFileHandle);
		FileGroup lcWigFileGroup = helptagHAMService
				.createAndSaveInnerFileGroup(lcWigFileHandle, helptagHAM,
											 "HELP-tagging pipeline generated wiggle track only showing the hpaii loci with low confidence scores");
		helptagHAMFileGroups.add(lcWigFileGroup);

		// will create enclosing fileGroup, save it to db, and
		// also set/save parent for each enclosed child fileGroup;
		// it will set it's own filetype
		FileGroup helptaghamAnalysisFileGroup = fileService.createFileGroupCollection(helptagHAMFileGroups);

		helptaghamAnalysisFileGroup.setDescription(prefixForFileName);
		helptaghamAnalysisFileGroup.setSoftwareGeneratedBy(helptagHAM);
		helptaghamAnalysisFileGroup.setDerivedFrom(derrivedFromFileGroups);
		helptaghamAnalysisFileGroup.setIsActive(0);
		helptaghamAnalysisFileGroup = fileService.addFileGroup(helptaghamAnalysisFileGroup);

		this.helptaghamAnalysisFileGroupId = helptaghamAnalysisFileGroup.getId();
		logger.debug("new ------- recorded all encompassing fileGroup helptaghamAnalysisFileGroup as a container for files outputted by helptagham");

		logger.debug("recorded fileGroups and fileHandles for helptagham files in HelptaghamTasklet.doExecute()");

		// get execution context for the step. Store key / value entries in here to maintain state in db
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
		// place in the step context in case of crash
		stepContext.put("jobId", this.jobId);
		stepContext.put("hpa2CellLibraryIdListAsString", this.hpa2CellLibraryIdListAsString);
		stepContext.put("msp1CellLibraryIdListAsString", this.msp1CellLibraryIdListAsString);
		stepContext.put("hpa2SampleId", this.hpa2SampleId);
		stepContext.put("msp1SampleId", this.msp1SampleId);
		stepContext.put("helptaghamAnalysisFileGroupId", this.helptaghamAnalysisFileGroupId);

		stepContext.put("commandLineCall", this.commandLineCall);
		logger.debug("saved variables in stepContext in case of crash in HelptaghamTasklet.doExecute()");

		w.setResultFiles(files);

		logger.debug("executed w.getResultFiles().add(x) for " + files.size() + " FileHandles");

		w.getConfiguration().setResultsDirectory(fileService.generateJobSoftwareBaseFolderName(job, helptagHAM));

		int counter = 0;
		for (String fileName : listOfFileHandleNames) {// need to make these symbolic links in order to properly copy files
			w.addCommand("ln -sf " + fileName + " ${" + WorkUnit.OUTPUT_FILE + "[" + counter + "]}");
			logger.debug("add command: " + "ln -sf " + fileName + " ${" + WorkUnit.OUTPUT_FILE + "[" + counter + "]}");
			counter++;
		}

		logger.debug("executed w.setResultsDirectory(a/jobId) in HelptaghamTasklet.doExecute()");

		// get grid result which can be used to determine the state of an executing job 
		GridResult result = gridHostResolver.execute(w);

		logger.info("Batch job execution submitted with id=" + result.getGridJobId() + " on host '" + result.getHostname() + "' from step (name='"
					+ stepExecution.getStepName() + "', id=" + stepExecution.getId() + ")");

		stepContext.put(HelptaghamService.JOB_ID_AS_STRING, jobId.toString());// promote; may not actually be required
		stepContext.put(HelptaghamService.PREFIX_FOR_FILE_NAME, prefixForFileName);// promote
		stepContext.put(HelptaghamService.HELPTAGHAM_ANALYSIS_FILEGROUP_ID_AS_STRING, helptaghamAnalysisFileGroupId.toString());// promote
		stepContext.put(HelptaghamService.WORKING_DIRECTORY, result.getWorkingDirectory());// promote
		stepContext.put(HelptaghamService.RESULTS_DIRECTORY, result.getResultsDirectory());// promote
		
		return result;
	}
	
	/**
	 * After remote task is finished you may need to execute some further business logic. Such work is specified here.
	 */
	@Override
	@Transactional("entityManager")
	public void doPreFinish(ChunkContext context) throws Exception {
		logger.debug("starting doPreFinish() in HAMTasklet");

		this.stepExecution = context.getStepContext().getStepExecution();
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
		this.jobId = (Integer) stepContext.get("jobId");// currently, not really needed here
		this.hpa2CellLibraryIdListAsString = (String) stepContext.get("hpa2CellLibraryIdListAsString");
		this.msp1CellLibraryIdListAsString = (String) stepContext.get("msp1CellLibraryIdListAsString");

		this.hpa2SampleId = (Integer) stepContext.get("hpa2SampleId");
		this.msp1SampleId = (Integer) stepContext.get("msp1SampleId");
		this.commandLineCall = (String) stepContext.get("commandLineCall");

		this.helptaghamAnalysisFileGroupId = (Integer) stepContext.get("helptaghamAnalysisFileGroupId");
		this.softwareIdUsedListAsString = (String) stepContext.get("softwareIdUsedListAsString");

		// associate test sample with the new file groups
		Sample hpa2Sample = sampleService.getSampleById(hpa2SampleId);

		logger.debug("getting ready to save hpa2Sample metadata  in HelptaghamTasklet");

		// register commandLineCall, testCellLibraryIdList, controlCellLibraryIdList and controlId and testId with fileGroupMeta
		// and record totalCountMappedReads, totalCountMappedReadsInPeaks, [FRIP statistic - will be derived from totalCountMappedReadsInPeaks /
		// totalCountMappedReadsInPeaks]
		// new 6-18-14
		if (this.helptaghamAnalysisFileGroupId != null && hpa2Sample.getId() != 0) {
			FileGroup enclosingFG = fileService.getFileGroupById(this.helptaghamAnalysisFileGroupId);
			enclosingFG.setIsActive(1);
			fileService.addFileGroup(enclosingFG);// save new active setting
			for (FileGroup childFG : enclosingFG.getChildren()) {
				childFG.setIsActive(1);
				fileService.addFileGroup(childFG);// save new active setting
			}
			fileService.setSampleFile(enclosingFG, hpa2Sample);// may be executed through derivedFrom (but just to be safe)

			List<FileGroupMeta> fgmList = new ArrayList<FileGroupMeta>();

			FileGroupMeta fgm = new FileGroupMeta();
			fgm.setK("helptaghamAnalysis.msp1Id");// could be gotten from the derivedFrom data; stored but never used
			fgm.setV(this.msp1SampleId.toString());
			fgm.setFileGroupId(enclosingFG.getId());
			fgmList.add(fgm);

			FileGroupMeta fgm2 = new FileGroupMeta();
			fgm2.setK("helptaghamAnalysis.hpa2Id");// sample --> sampleFileGroup table; but could be gotten from the derivedFrom data;
			fgm2.setV(hpa2Sample.getId().toString());
			fgm2.setFileGroupId(enclosingFG.getId());
			fgmList.add(fgm2);

			FileGroupMeta fgm3 = new FileGroupMeta();
			fgm3.setK("helptaghamAnalysis.hpa2CellLibraryIdList");// these may be now really stored in derivedFrom, as list of Bam files;
			fgm3.setV(this.hpa2CellLibraryIdListAsString);
			fgm3.setFileGroupId(enclosingFG.getId());
			fgmList.add(fgm3);

			FileGroupMeta fgm4 = new FileGroupMeta();
			fgm4.setK("helptaghamAnalysis.msp1CellLibraryIdList");// these may be now really stored in derivedFrom, as list of Bam files;
			fgm4.setV(this.msp1CellLibraryIdListAsString);
			fgm4.setFileGroupId(enclosingFG.getId());
			fgmList.add(fgm4);

			FileGroupMeta fgm5 = new FileGroupMeta();
			fgm5.setK("helptaghamAnalysis.commandLineCall");// used
			fgm5.setV(this.commandLineCall);
			fgm5.setFileGroupId(enclosingFG.getId());
			fgmList.add(fgm5);

			FileGroupMeta fgm6 = new FileGroupMeta();
			fgm6.setK("helptaghamAnalysis.softwareIdUsedListAsString");// used; as of 9/16, should be able to get this from the fileGroup children
			fgm6.setV(this.softwareIdUsedListAsString);
			fgm6.setFileGroupId(enclosingFG.getId());
			fgmList.add(fgm6);

			fileService.saveFileGroupMeta(fgmList, enclosingFG);
		}

		logger.debug("ending doPreFinish() in HelptaghamTasklet");
	}
	
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void beforeStep(StepExecution stepExecution){
		super.beforeStep(stepExecution);

		logger.debug("*****  StepExecutionListener beforeStep saving StepExecution in HelptaghamTasklet.beforeStep");
		this.stepExecution = stepExecution;

		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
		// in case of crash
		if (this.jobId == null) {// set initially in constructor
			this.jobId = (Integer) stepContext.get("jobId");
		}
		if (this.hpa2CellLibraryIdListAsString == null) {// set initially in constructor
			this.hpa2CellLibraryIdListAsString = (String) stepContext.get("hpa2CellLibraryIdListAsString");
		}
		if (this.msp1CellLibraryIdListAsString == null) {// set initially in constructor
			this.msp1CellLibraryIdListAsString = (String) stepContext.get("msp1CellLibraryIdListAsString");
		}

		this.hpa2SampleId = (Integer) stepContext.get("hpa2SampleId");
		this.msp1SampleId = (Integer) stepContext.get("msp1SampleId");
		this.commandLineCall = (String) stepContext.get("commandLineCall");

		this.helptaghamAnalysisFileGroupId = (Integer) stepContext.get("helptaghamAnalysisFileGroupId");
		this.softwareIdUsedListAsString = (String) stepContext.get("softwareIdUsedListAsString");
		
		// here we can print out some information about the batch job to the info log
		if (logger.isInfoEnabled()){ // no point looking in here otherwise
			Map<String, JobParameter> jobParameters = stepExecution.getJobExecution().getJobParameters().getParameters();
			JobExecution batchJobExec = stepExecution.getJobExecution();
			
			logger.info("Starting batch job (name='" + batchJobExec.getJobInstance().getJobName() +
					"', id=" +  batchJobExec.getId() + "), step (name='" + stepExecution.getStepName() +
							"', id=" +  stepExecution.getId() + ")");
			
			for (String key : jobParameters.keySet()) {
				logger.info("Parameter for batch job id= " + batchJobExec.getId() + 
						": " + key + "=" + jobParameters.get(key).getValue().toString());
			}
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		ExitStatus exitStatus = super.afterStep(stepExecution);
		// TODO: any post-step logic goes here
	
		// here we can print out some information about the batch job status to the info log
		logger.info("Finished executing step (name='" + stepExecution.getStepName() +
							"', id=" +  stepExecution.getId() + ") with ExitStatus=" + exitStatus);
		return exitStatus;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void doCleanupBeforeRestart(StepExecution stepExecution) throws Exception {
		// TODO: implement any code here for execution prior to restarting a failed step execution
		
	}
	
}
