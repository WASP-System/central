package edu.yu.einstein.wasp.helptag.batch.tasklet;

/**
 * AJ Jing
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.LaunchManyJobsTasklet;
import edu.yu.einstein.wasp.exception.SoftwareConfigurationException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.helptag.integration.messages.HelptagSoftwareJobParameters;
import edu.yu.einstein.wasp.helptag.service.HelptagService;
import edu.yu.einstein.wasp.helptag.software.Helptag;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.interfacing.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.plugin.mps.service.SequencingService;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.WaspJobContext;

@Transactional("entityManager")
public class AggrAngleMakerTasklet extends LaunchManyJobsTasklet {
	
	@Autowired
	private HelptagService helptagService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private SequencingService sequencingService;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private GenomeService genomeService;
	
	@Autowired
	private WaspPluginRegistry waspPluginRegistry;

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
	private Helptag helptag;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private Integer jobId;
	private String testCellLibraryIdListAsString;
	private String controlCellLibraryIdListAsString;
	private List<Integer> testCellLibraryIdList;// treated, such as IP
	private List<Integer> controlCellLibraryIdList;// contol, such as input

	private Integer testSampleId;
	private Integer controlSampleId;
	private String commandLineCall;

	private Integer helptagAnalysisFileGroupId;

	private StepExecution stepExecution;

	public AggrAngleMakerTasklet() {
		// proxy
	}

	@Override
	@Transactional("entityManager")
	public void doExecute() {
		Map<String,JobParameter> jobParametersMap = getStepExecution().getJobParameters().getParameters();	
		Integer jobIdFromJobParameter = null;
		for (String key : getStepExecution().getJobParameters().getParameters().keySet()) {
			if(key.equalsIgnoreCase(WaspJobParameters.JOB_ID)){
				JobParameter jp = jobParametersMap.get(key);
				jobIdFromJobParameter = new Integer(jp.toString());
			}
		}
		
		Assert.assertTrue(jobIdFromJobParameter>0);
		Job job = jobService.getJobByJobId(jobIdFromJobParameter);
		Assert.assertTrue(job.getId()>0);
		List<SampleSource> approvedCellLibraryList = null;
		try{
			approvedCellLibraryList = sampleService.getCellLibrariesPassQCAndNoAggregateAnalysis(job);	
		}catch(Exception e){
			logger.debug("unable to obtain approvedCellLibraryList in AngleMakerTasklet; message = " + e.getMessage());
		}
		Assert.assertTrue(approvedCellLibraryList != null && !approvedCellLibraryList.isEmpty());
		Assert.assertTrue(helptagService.confirmCellLibrariesAssociatedWithHcountFiles(approvedCellLibraryList));

		Map<Sample, List<SampleSource>> approvedSampleApprovedCellLibraryListMap = sampleService.associateUppermostSampleWithCellLibraries(approvedCellLibraryList);
		Set<Sample> setOfApprovedSamples = new HashSet<Sample>();//for a specific job (note: this really could have been a list)
		for (Sample approvedSample : approvedSampleApprovedCellLibraryListMap.keySet()) {
			setOfApprovedSamples.add(approvedSample);
		}
		
		Set<Sample> sampleSetOfHpaIIPairedWithMspI = new HashSet<Sample>();
		for(Sample approvedSample : setOfApprovedSamples){
			logger.debug("approvedSample : " + approvedSample.getName());
			if (helptagService.isHpaII(approvedSample.getId())) {
				// only make angles for HpaII samples
				List<SampleSource> cellLibraryListForHpaII = approvedSampleApprovedCellLibraryListMap.get(approvedSample);

				for(SampleSource ss : sampleService.getSamplePairsByJob(job)){
					Sample hpaSample = ss.getSample();
    				Sample mspSample = ss.getSourceSample();
    				if (approvedSample.getId().intValue() == hpaSample.getId().intValue()) {
						if (setOfApprovedSamples.contains(mspSample)) {
							Assert.assertTrue(sampleService.confirmSamplePairIsOfSameSpecies(hpaSample, mspSample));
							List<SampleSource> cellLibraryListForMspI = approvedSampleApprovedCellLibraryListMap.get(mspSample);
							sampleSetOfHpaIIPairedWithMspI.add(approvedSample);
							prepareAndLaunchMessage(job, sampleService.convertCellLibraryListToIdList(cellLibraryListForHpaII),
													sampleService.convertCellLibraryListToIdList(cellLibraryListForMspI));
						}
					}
				}
				if (!sampleSetOfHpaIIPairedWithMspI.contains(approvedSample)) {
					// not on samplePair List, or it is on samplePair List but there are no BAM files for the requested mspI
					prepareAndLaunchMessage(job, sampleService.convertCellLibraryListToIdList(cellLibraryListForHpaII), new ArrayList<Integer>());
				}
			}
		}		
	}

	private void prepareAndLaunchMessage(Job job, List<Integer> testCellLibraryIdList, List<Integer> controlCellLibraryIdList) {
		try{
			//this used to work in next line, but it no longer functions; instead we get error: lazy load problem    new WaspJobContext(jobId, jobService)
			// logger.debug("9	in prepareAndLaunchMessage() of AngleMakerTasklet	");
			WaspJobContext waspJobContext = new WaspJobContext(jobService.getJobAndSoftware(job));
			SoftwareConfiguration softwareConfig = waspJobContext.getConfiguredSoftware(this.softwareResourceType);
			if (softwareConfig == null){
				throw new SoftwareConfigurationException("No software could be configured for jobId=" + job.getId() + " with resourceType iname=" + softwareResourceType.getIName());
			}
			BatchJobProviding softwarePlugin = waspPluginRegistry.getPlugin(softwareConfig.getSoftware().getIName(), BatchJobProviding.class);
			String flowName = softwarePlugin.getBatchJobName(BatchJobTask.GENERIC);
			if (flowName == null){
				logger.warn("No generic flow found for plugin so cannot launch software : " + softwareConfig.getSoftware().getIName());
			}
			Assert.assertTrue(flowName != null && !flowName.isEmpty());
			logger.warn("Flowname : " + flowName);//for macstwo, flowname will be: edu.yu.einstein.wasp.macstwo.mainFlow
			Map<String, String> jobParameters = softwareConfig.getParameters();
			jobParameters.put(HelptagSoftwareJobParameters.TEST_LIBRARY_CELL_ID_LIST, WaspSoftwareJobParameters.getCellLibraryListAsParameterValue(testCellLibraryIdList));
			jobParameters.put(HelptagSoftwareJobParameters.CONTROL_LIBRARY_CELL_ID_LIST, WaspSoftwareJobParameters.getCellLibraryListAsParameterValue(controlCellLibraryIdList));
			jobParameters.put(HelptagSoftwareJobParameters.JOB_ID, job.getId().toString());
			// jobParameters.put(HelptagSoftwareJobParameters.PEAK_TYPE, peakType);
			
			requestLaunch(flowName, jobParameters);
		}	
		catch(Exception e){
			logger.warn("e.message()   =   " + e.getMessage());
			throw new MessagingException(e.getLocalizedMessage(), e);
		}
	}

	private GridResult launchAngleMaker() throws Exception {
		Job job = jobService.getJobByJobId(jobId);

		SampleSource firstTestCellLibrary = sampleService.getCellLibraryBySampleSourceId(this.testCellLibraryIdList.get(0));
		Sample testSample = sampleService.getLibrary(firstTestCellLibrary);// all these cellLibraries are from the same library or macromoleucle
		while (testSample.getParentId() != null) {
			testSample = sampleService.getSampleById(testSample.getParentId());
		}
		for (SampleMeta sm : testSample.getSampleMeta()) {
			if (sm.getK().toLowerCase().contains("antibody")) {
			}
		}
		logger.debug("testSample.name = " + testSample.getName());
		this.testSampleId = testSample.getId();

		Set<FileGroup> derrivedFromFileGroups = new HashSet<FileGroup>();

		List<FileHandle> testFileHandleList = new ArrayList<FileHandle>();
		for (Integer id : this.testCellLibraryIdList) {
			SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(id);
			// setOfCellLibrariesForDerivedFrom.add(cellLibrary);
			Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLibrary, hcountFileType);
			derrivedFromFileGroups.addAll(fileGroups);
			logger.debug("test fileGroups size = " + fileGroups.size());
			for (FileGroup fileGroup : fileGroups) {
				for (FileHandle fileHandle : fileGroup.getFileHandles()) {
					testFileHandleList.add(fileHandle);
					logger.debug("test fileHandle = " + fileHandle.getFileName());
				}
			}
		}
		Assert.assertTrue(!testFileHandleList.isEmpty());
		logger.debug("test hcount files size = " + testFileHandleList.size());

		Sample controlSample = null;
		if (!controlCellLibraryIdList.isEmpty()) {
			controlSample = sampleService.getLibrary(sampleService.getCellLibraryBySampleSourceId(controlCellLibraryIdList.get(0)));// all these cellLibraries
																																	// are from the same library
																																	// or macromoleucle
			while (controlSample.getParentId() != null) {
				controlSample = sampleService.getSampleById(controlSample.getParentId());// controlSample.getParent();
			}
		}
		if (controlSample == null) {
			logger.debug("controlSample IS NULL");
			this.controlSampleId = 0;
		} else {
			logger.debug("controlSample.name = " + controlSample.getName());
			this.controlSampleId = controlSample.getId();
		}

		List<FileHandle> controlFileHandleList = new ArrayList<FileHandle>();
		for (Integer id : this.controlCellLibraryIdList) {
			SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(id);
			// setOfCellLibrariesForDerivedFrom.add(cellLibrary);
			Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLibrary, hcountFileType);
			derrivedFromFileGroups.addAll(fileGroups);
			logger.debug("control fileGroups size = " + fileGroups.size());
			for (FileGroup fileGroup : fileGroups) {
				for (FileHandle fileHandle : fileGroup.getFileHandles()) {
					controlFileHandleList.add(fileHandle);// can be empty
					logger.debug("control fileHandle = " + fileHandle.getFileName());
				}
			}
		}
		logger.debug("controlFileHandleList.size = " + controlFileHandleList.size());

		Date dateNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
		// new
		String prefixForFileName = ft.format(dateNow) + "_HPA_" + testSample.getName();
		if (controlSample != null) {
			prefixForFileName = prefixForFileName + "_MSP_" + controlSample.getName();
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

		Build build = genomeService.getBuild(testSample);
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

		WorkUnit w = helptag.getAngleMaker(build.getGenome().getAlias(), prefixForFileName, testFileHandleList, controlFileHandleList);
		logger.debug("OK, workunit has been generated");
		this.commandLineCall = w.getCommand();
		this.commandLineCall = this.commandLineCall.replaceAll("\\n", "<br /><br />");// the workunit tagged on a newline at the end of the command; so remove
																					  // it for db storage and replace with <br /> for display purposes

		List<String> listOfFileHandleNames = new ArrayList<String>();

		LinkedHashSet<FileHandle> files = new LinkedHashSet<FileHandle>();
		Set<FileGroup> helptagFileGroups = new LinkedHashSet<FileGroup>();

		FileHandle wigFileHandle = helptagService.createAndSaveInnerFileHandle(prefixForFileName + ".wig", wigFileType);
		listOfFileHandleNames.add(wigFileHandle.getFileName());
		files.add(wigFileHandle);
		FileGroup wigFileGroup = helptagService
				.createAndSaveInnerFileGroup(wigFileHandle, helptag,
											 "HELP-tagging pipeline generated wiggle track file showing methylation histograms on hpaii loci");
		helptagFileGroups.add(wigFileGroup);

		FileHandle angleFileHandle = helptagService.createAndSaveInnerFileHandle(prefixForFileName + ".angle", htgAngleFileType);
		listOfFileHandleNames.add(angleFileHandle.getFileName());
		files.add(angleFileHandle);
		FileGroup angleFileGroup = helptagService
				.createAndSaveInnerFileGroup(angleFileHandle, helptag,
											 "HELP-tagging pipeline generated tab-delimited file storing methylation scores on hpaii loci");
		helptagFileGroups.add(angleFileGroup);

		FileHandle confInfoFileHandle = helptagService.createAndSaveInnerFileHandle(prefixForFileName + ".conf.txt", textFileType);
		listOfFileHandleNames.add(confInfoFileHandle.getFileName());
		files.add(confInfoFileHandle);
		FileGroup confInfoFileGroup = helptagService
				.createAndSaveInnerFileGroup(confInfoFileHandle, helptag,
											 "HELP-tagging pipeline generated plain text file storing confidence scores' distribution information");
		helptagFileGroups.add(confInfoFileGroup);

		FileHandle bedFileHandle = helptagService.createAndSaveInnerFileHandle(prefixForFileName + ".conf.bed", bedFileType);
		listOfFileHandleNames.add(bedFileHandle.getFileName());
		files.add(bedFileHandle);
		FileGroup bedFileGroup = helptagService
				.createAndSaveInnerFileGroup(bedFileHandle, helptag,
											 "HELP-tagging pipeline generated bed track showing the trimmed reads aligned to hpaii loci");
		helptagFileGroups.add(bedFileGroup);

		FileHandle hcWigFileHandle = helptagService.createAndSaveInnerFileHandle(prefixForFileName + ".hc.wig", wigFileType);
		listOfFileHandleNames.add(hcWigFileHandle.getFileName());
		files.add(hcWigFileHandle);
		FileGroup hcWigFileGroup = helptagService
				.createAndSaveInnerFileGroup(hcWigFileHandle, helptag,
											 "HELP-tagging pipeline generated wiggle track only showing the hpaii loci with high confidence scores");
		helptagFileGroups.add(hcWigFileGroup);

		FileHandle mcWigFileHandle = helptagService.createAndSaveInnerFileHandle(prefixForFileName + ".mc.wig", wigFileType);
		listOfFileHandleNames.add(mcWigFileHandle.getFileName());
		files.add(mcWigFileHandle);
		FileGroup mcWigFileGroup = helptagService
				.createAndSaveInnerFileGroup(mcWigFileHandle, helptag,
											 "HELP-tagging pipeline generated wiggle track only showing the hpaii loci with medium confidence scores");
		helptagFileGroups.add(mcWigFileGroup);

		FileHandle lcWigFileHandle = helptagService.createAndSaveInnerFileHandle(prefixForFileName + ".lc.wig", wigFileType);
		listOfFileHandleNames.add(lcWigFileHandle.getFileName());
		files.add(lcWigFileHandle);
		FileGroup lcWigFileGroup = helptagService
				.createAndSaveInnerFileGroup(lcWigFileHandle, helptag,
											 "HELP-tagging pipeline generated wiggle track only showing the hpaii loci with low confidence scores");
		helptagFileGroups.add(lcWigFileGroup);

		FileGroup helptagAnalysisFileGroup = fileService.createFileGroupCollection(helptagFileGroups);// will create enclosing fileGroup, save it to db, and
																									  // also set/save parent for each enclosed child fileGroup;
																									  // it will set it's own filetype
		helptagAnalysisFileGroup.setDescription(prefixForFileName);
		helptagAnalysisFileGroup.setSoftwareGeneratedBy(helptag);
		helptagAnalysisFileGroup.setDerivedFrom(derrivedFromFileGroups);// this is actually adding reference to samplesourcefilegroup and I think,
																		// samplefilegroup
		helptagAnalysisFileGroup.setIsActive(0);
		helptagAnalysisFileGroup = fileService.addFileGroup(helptagAnalysisFileGroup);

		this.helptagAnalysisFileGroupId = helptagAnalysisFileGroup.getId();
		logger.debug("new ------- recorded all encompassing fileGroup macs2AnalysisFileGroup as a container for files outputted by macs2");

		logger.debug("recorded fileGroups and fileHandles for macs2 files in MacstwoTasklet.doExecute()");

		// place in the step context in case of crash
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
		stepContext.put("jobId", this.jobId);
		stepContext.put("testCellLibraryIdListAsString", this.testCellLibraryIdListAsString);
		stepContext.put("controlCellLibraryIdListAsString", this.controlCellLibraryIdListAsString);
		stepContext.put("testSampleId", this.testSampleId);
		stepContext.put("controlSampleId", this.controlSampleId);

		stepContext.put("macs2AnalysisFileGroupId", this.helptagAnalysisFileGroupId);

		stepContext.put("commandLineCall", this.commandLineCall);
		logger.debug("saved variables in stepContext in case of crash in MacstwoTasklet.doExecute()");

		w.setResultFiles(files);

		logger.debug("executed w.getResultFiles().add(x) for " + files.size() + " FileHandles");

		w.getConfiguration().setResultsDirectory(fileService.generateJobSoftwareBaseFolderName(job, helptag));

		int counter = 0;
		for (String fileName : listOfFileHandleNames) {// need to make these symbolic links in order to properly copy files
			w.addCommand("ln -sf " + fileName + " ${" + WorkUnit.OUTPUT_FILE + "[" + counter + "]}");
			logger.debug("add command: " + "ln -sf " + fileName + " ${" + WorkUnit.OUTPUT_FILE + "[" + counter + "]}");
			counter++;
		}

		logger.debug("executed w.setResultsDirectory(a/jobId) in MacstwoTasklet.doExecute()");

		GridResult result = gridHostResolver.execute(w);

		stepContext.put(helptagService.JOB_ID_AS_STRING, jobId.toString());// promote; may not actually be required
		stepContext.put(helptagService.PREFIX_FOR_FILE_NAME, prefixForFileName);// promote
		stepContext.put(helptagService.HELPTAG_ANALYSIS_FILEGROUP_ID_AS_STRING, helptagAnalysisFileGroupId.toString());// promote
		stepContext.put(helptagService.WORKING_DIRECTORY, result.getWorkingDirectory());// promote
		stepContext.put(helptagService.RESULTS_DIRECTORY, result.getResultsDirectory());// promote

		return result;
	}

}
