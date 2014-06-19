/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.macstwo.batch.tasklet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
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
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.macstwo.software.Macstwo;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.mps.grid.software.Imagemagick;
import edu.yu.einstein.wasp.plugin.mps.grid.software.R;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * @author 
 * 
 */
public class MacstwoTasklet extends WaspRemotingTasklet implements StepExecutionListener {

	private Integer jobId;
	private String testCellLibraryIdListAsString;
	private String controlCellLibraryIdListAsString;
	private List<Integer> testCellLibraryIdList;//treated, such as IP
	private List<Integer> controlCellLibraryIdList;//contol, such as input 
	private Integer modelScriptGId;
	private Integer peaksXlsGId;
	private Integer narrowPeaksBedGId;
	private Integer summitsBedGId;
	private Integer summitsModifiedBedGId;
	private Integer treatPileupBedGraphGId;
	private Integer controlLambdaBedGraphGId;
	private Integer testSampleId;
	private Integer controlSampleId;
	private String commandLineCall;
	
	
	
	private Integer modelPdfGId;//generated in doExecute()
	private Integer modelPngGId;//generated in doExecute()
	
	private Integer macs2AnalysisFileGroupId;
	
	private StepExecution stepExecution;
	
	
	@Autowired
	private FileType macs2ModelScriptFileType;
	@Autowired
	private FileType macs2PeaksXlsFileType;
	@Autowired
	private FileType macs2NarrowPeaksBedFileType;
	@Autowired
	private FileType macs2SummitsBedFileType;
	@Autowired
	private FileType macs2SummitsModifiedBedFileType;
	@Autowired
	private FileType macs2TreatPileupBedGraphFileType;
	@Autowired
	private FileType macs2ControlLambdaBedGraphFileType;
	
	
	
	@Autowired
	private FileType macs2ModelPdfFileType;

	@Autowired
	private FileType macs2ModelPngFileType;
	
	
	@Autowired
	private FileType macs2AnalysisFileType;
	
	
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private FileType bamFileType;

	@Autowired
	private SampleService sampleService;
	@Autowired
	private FileService fileService;

	@Autowired
	private GridHostResolver gridHostResolver;
	
	@Autowired
	private Macstwo macs2;
	

	public MacstwoTasklet() {
		// proxy
	}

	public MacstwoTasklet(String jobIdAsString, String testCellLibraryIdListAsString, String controlCellLibraryIdListAsString) throws Exception {
		
		logger.debug("Starting MacstwoTasklet constructor");
		logger.debug("jobIdAsString: " + jobIdAsString);
		logger.debug("testCellLibraryIdListAsString: " + testCellLibraryIdListAsString);
		logger.debug("controlCellLibraryIdListAsString: " + controlCellLibraryIdListAsString);
		
		Assert.assertTrue(!jobIdAsString.isEmpty());
		this.jobId = Integer.parseInt(jobIdAsString);		
		Assert.assertTrue(this.jobId > 0);
		
		this.testCellLibraryIdListAsString = testCellLibraryIdListAsString;
		Assert.assertTrue(!testCellLibraryIdListAsString.isEmpty());
		this.testCellLibraryIdList = WaspSoftwareJobParameters.getCellLibraryIdList(testCellLibraryIdListAsString);//should be all from same job

		Assert.assertTrue(!this.testCellLibraryIdList.isEmpty());
		
		//oddly enough (and not expected from the code), WaspSoftwareJobParameters.getLibraryCellIdList(controlCellLibraryIdListAsString)
		//throws an exception if controlCellLibraryIdListAsString is an empty string, thus the need for the if-else statement
		if(controlCellLibraryIdListAsString==null || controlCellLibraryIdListAsString.isEmpty()){//could be empty!!
			this.controlCellLibraryIdListAsString = "";
			this.controlCellLibraryIdList = new ArrayList<Integer>();
		}
		else{
			this.controlCellLibraryIdListAsString = controlCellLibraryIdListAsString;
			this.controlCellLibraryIdList = WaspSoftwareJobParameters.getCellLibraryIdList(controlCellLibraryIdListAsString);//may be empty
			Assert.assertTrue(!this.controlCellLibraryIdList.isEmpty());
		}
		
		logger.debug("in constructor this.jobId: " + this.jobId);
		logger.debug("in constructor testCellLibraryIdList.size(): " + testCellLibraryIdList.size());
		logger.debug("in constructor controlCellLibraryIdList.size(): " + controlCellLibraryIdList.size());
		logger.debug("Ending MacstwoTasklet constructor");
	}
	
	/**
	 * 
	 * @param contrib
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@Transactional("entityManager")
	@Override
	public void doExecute(ChunkContext context) throws Exception {
		
		logger.debug("Starting MacstwoTasklet execute");		
		logger.debug("in doExecute this.jobId: " + this.jobId);
		logger.debug("in doExecute testCellLibraryIdList.size(): " + testCellLibraryIdList.size());
		logger.debug("in doExecute controlCellLibraryIdList.size(): " + controlCellLibraryIdList.size());
		Job job = jobService.getJobByJobId(jobId);
		Map<String,Object> jobParametersMap = context.getStepContext().getJobParameters();		
		for (String key : jobParametersMap.keySet()) {
			logger.debug("jobParameters Key: " + key + " Value: " + jobParametersMap.get(key).toString());
		}
		Map<String,Object> jobExecutionContextMap = context.getStepContext().getJobExecutionContext();		
		for (String key : jobExecutionContextMap.keySet()) {
			logger.debug("jobExecutionContextMap Key: " + key + " Value: " + jobExecutionContextMap.get(key).toString());
		}
		
		SampleSource firstTestCellLibrary = sampleService.getCellLibraryBySampleSourceId(this.testCellLibraryIdList.get(0));
		Sample testSample = sampleService.getLibrary(firstTestCellLibrary);//all these cellLibraries are from the same library or macromoleucle
		String antibodyTarget = null;
		while(testSample.getParentId()!=null){
			testSample = sampleService.getSampleById(testSample.getParentId());
		}
		for(SampleMeta sm : testSample.getSampleMeta()){
			if(sm.getK().toLowerCase().contains("antibody")){
				antibodyTarget = sm.getV();
			}
		}
		logger.debug("testSample.name = " + testSample.getName());		
		this.testSampleId = testSample.getId();

		Set<SampleSource> setOfCellLibrariesForDerivedFrom = new HashSet<SampleSource>();//tests and controls
		
		Set<FileGroup> derrivedFromFileGroups = new HashSet<FileGroup>();
		
		List<FileHandle> testFileHandleList = new ArrayList<FileHandle>();		
		for(Integer id : this.testCellLibraryIdList){
			SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(id);
			setOfCellLibrariesForDerivedFrom.add(cellLibrary);
			Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLibrary, bamFileType);
			derrivedFromFileGroups.addAll(fileGroups);
			logger.debug("test fileGroups size = " + fileGroups.size());
			for(FileGroup fileGroup : fileGroups){
				for(FileHandle fileHandle : fileGroup.getFileHandles()){
					testFileHandleList.add(fileHandle);
					logger.debug("test fileHandle = " + fileHandle.getFileName());
				}				
			}
		}
		Assert.assertTrue(!testFileHandleList.isEmpty());
		logger.debug("test bam files size = " + testFileHandleList.size());

		Sample controlSample = null;
		if(!controlCellLibraryIdList.isEmpty()){
			controlSample = sampleService.getLibrary(sampleService.getCellLibraryBySampleSourceId(controlCellLibraryIdList.get(0)));//all these cellLibraries are from the same library or macromoleucle
			while(controlSample.getParentId()!=null){
				controlSample = sampleService.getSampleById(controlSample.getParentId());//controlSample.getParent();
			}
		}
		if(controlSample==null){
			logger.debug("controlSample IS NULL");
			this.controlSampleId = 0;
		}
		else{
			logger.debug("controlSample.name = " + controlSample.getName());
			this.controlSampleId = controlSample.getId();
		}		
		
		List<FileHandle> controlFileHandleList = new ArrayList<FileHandle>();
		for(Integer id : this.controlCellLibraryIdList){
			SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(id);
			setOfCellLibrariesForDerivedFrom.add(cellLibrary);
			Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLibrary, bamFileType);
			derrivedFromFileGroups.addAll(fileGroups);
			logger.debug("control fileGroups size = " + fileGroups.size());
			for(FileGroup fileGroup : fileGroups){
				for(FileHandle fileHandle : fileGroup.getFileHandles()){
					controlFileHandleList.add(fileHandle);//can be empty
					logger.debug("control fileHandle = " + fileHandle.getFileName());
				}
			}
		}
		logger.debug("controlFileHandleList.size = " + controlFileHandleList.size());
			
		Date dateNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd");
	    //new
	    String prefixForFileName = ft.format(dateNow)+ "_IP_" + testSample.getName().replaceAll("\\s+", "_");
	    if(antibodyTarget!=null && !antibodyTarget.isEmpty()){
	    	prefixForFileName = prefixForFileName + "_TARGET_" + antibodyTarget.replaceAll("\\s+", "_");
	    }
	    else{
	    	prefixForFileName = prefixForFileName + "_TARGET_unspecified";
	    }
	    if(controlSample != null){
	    	prefixForFileName = prefixForFileName + "_CONTROL_" + controlSample.getName().replaceAll("\\s+", "_");
	    }
	    else{
	    	prefixForFileName = prefixForFileName + "_CONTROL_none";
	    }
	    
	    //old
	    /*
	    String prefixForFileName = "TEST_" + testSample.getName().replaceAll("\\s+", "_") + "_CONTROL_";
		if(controlSample == null){
			prefixForFileName = prefixForFileName + "none";
		}
		else{
			prefixForFileName = prefixForFileName + controlSample.getName().replaceAll("\\s+", "_");
		}
		*/
		
		logger.debug("prefixForFileName = " + prefixForFileName);
		logger.debug("preparing to generate workunit");
		
		String modelFileName = prefixForFileName + "_model.r";
		String pdfFileName = modelFileName.replaceAll(".r$", ".pdf");//abc_model.r will be used to generate abc_model.pdf
		String pngFileName = modelFileName.replaceAll(".r$", ".png");//abc_model.pdf will be used to generate abc_model.png

		WorkUnit w = macs2.getPeaks(prefixForFileName, testFileHandleList, controlFileHandleList, jobParametersMap, modelFileName, pdfFileName, pngFileName);//configure
		logger.debug("OK, workunit has been generated");
		this.commandLineCall = w.getCommand();
		this.commandLineCall = this.commandLineCall.replaceAll("\\n", "<br /><br />");//the workunit tagged on a newline at the end of the command; so remove it for db storage and replace with <br /> for display purposes

		List<String> listOfFileHandleNames = new ArrayList<String>();
		
		Set<FileHandle> files = new LinkedHashSet<FileHandle>();
				
		FileGroup modelScriptG = new FileGroup();
		FileHandle modelScript = new FileHandle();
		modelScript.setFileName(modelFileName);//prefixForFileName + "_model.r"
		//WHEN THHIS IS A SINGLE FILEGROUP FOR ALL FILES, then do:
		//BUT NOT FOR modelScriptG, use the new name  whatever that is modelScriptG.setDerivedFrom(derrivedFromFileGroups);
		//but keep the other meta just in case, as fileGroupMeta
		//since the fileGroup will have a file type of chipSeqAnalysis.macs2 with a name of previxForFileName
		listOfFileHandleNames.add(modelFileName );
		modelScript.setFileType(macs2ModelScriptFileType);
		modelScript = fileService.addFile(modelScript);
		modelScriptG.addFileHandle(modelScript);
		files.add(modelScript);
		modelScriptG.setFileType(macs2ModelScriptFileType);
		modelScriptG.setDescription(modelScript.getFileName());
		modelScriptG.setSoftwareGeneratedBy(macs2);
		modelScriptG.setSampleSources(setOfCellLibrariesForDerivedFrom);
		modelScriptG = fileService.addFileGroup(modelScriptG);
		this.modelScriptGId = modelScriptG.getId();
		
		FileGroup peaksXlsG = new FileGroup();
		FileHandle peaksXls = new FileHandle();
		peaksXls.setFileName(prefixForFileName + "_peaks.xls");
		listOfFileHandleNames.add(prefixForFileName + "_peaks.xls");
		peaksXls.setFileType(macs2PeaksXlsFileType);
		peaksXls = fileService.addFile(peaksXls);
		peaksXlsG.addFileHandle(peaksXls);
		files.add(peaksXls);
		peaksXlsG.setFileType(macs2PeaksXlsFileType);
		peaksXlsG.setDescription(peaksXls.getFileName());
		peaksXlsG.setSoftwareGeneratedBy(macs2);
		peaksXlsG.setSampleSources(setOfCellLibrariesForDerivedFrom);
		peaksXlsG = fileService.addFileGroup(peaksXlsG);
		this.peaksXlsGId = peaksXlsG.getId();
		
		FileGroup narrowPeaksBedG = new FileGroup();
		FileHandle narrowPeaksBed = new FileHandle();
		narrowPeaksBed.setFileName(prefixForFileName + "_peaks.narrowPeak");
		listOfFileHandleNames.add(prefixForFileName + "_peaks.narrowPeak");
		narrowPeaksBed.setFileType(macs2NarrowPeaksBedFileType);
		narrowPeaksBed = fileService.addFile(narrowPeaksBed);
		narrowPeaksBedG.addFileHandle(narrowPeaksBed);
		files.add(narrowPeaksBed);
		narrowPeaksBedG.setFileType(macs2NarrowPeaksBedFileType);
		narrowPeaksBedG.setDescription(narrowPeaksBed.getFileName());
		narrowPeaksBedG.setSoftwareGeneratedBy(macs2);
		narrowPeaksBedG.setSampleSources(setOfCellLibrariesForDerivedFrom);
		narrowPeaksBedG = fileService.addFileGroup(narrowPeaksBedG);
		this.narrowPeaksBedGId = narrowPeaksBedG.getId();
	
		FileGroup summitsBedG = new FileGroup();
		FileHandle summitsBed = new FileHandle();
		summitsBed.setFileName(prefixForFileName + "_summits.bed");
		listOfFileHandleNames.add(prefixForFileName + "_summits.bed");
		summitsBed.setFileType(macs2SummitsBedFileType);
		summitsBed = fileService.addFile(summitsBed);
		summitsBedG.addFileHandle(summitsBed);
		files.add(summitsBed);
		summitsBedG.setFileType(macs2SummitsBedFileType);
		summitsBedG.setDescription(summitsBed.getFileName());
		summitsBedG.setSoftwareGeneratedBy(macs2);
		summitsBedG.setSampleSources(setOfCellLibrariesForDerivedFrom);
		summitsBedG = fileService.addFileGroup(summitsBedG);
		this.summitsBedGId = summitsBedG.getId();		
/*		
		FileGroup summitsModifiedBedG = new FileGroup();
		FileHandle summitsModifiedBed = new FileHandle();
		summitsModifiedBed.setFileName(prefixForFileName + "_summits.modified.bed");
		listOfFileHandleNames.add(prefixForFileName + "_summits.modified.bed");
		summitsModifiedBed.setFileType(macs2SummitsModifiedBedFileType);
		summitsModifiedBed = fileService.addFile(summitsModifiedBed);
		summitsModifiedBedG.addFileHandle(summitsModifiedBed);
		files.add(summitsModifiedBed);
		summitsModifiedBedG.setFileType(macs2SummitsModifiedBedFileType);
		summitsModifiedBedG.setDescription(summitsModifiedBed.getFileName());
		summitsModifiedBedG.setSoftwareGeneratedBy(macs2);
		summitsModifiedBedG.setSampleSources(setOfCellLibrariesForDerivedFrom);
		summitsModifiedBedG = fileService.addFileGroup(summitsModifiedBedG);
		this.summitsModifiedBedGId = summitsModifiedBedG.getId();		
*/
		FileGroup treatPileupBedGraphG = new FileGroup();
		FileHandle treatPileupBedGraph = new FileHandle();
		treatPileupBedGraph.setFileName(prefixForFileName + "_treat_pileup.bdg");
		listOfFileHandleNames.add(prefixForFileName + "_treat_pileup.bdg");
		treatPileupBedGraph.setFileType(macs2TreatPileupBedGraphFileType);
		treatPileupBedGraph = fileService.addFile(treatPileupBedGraph);
		treatPileupBedGraphG.addFileHandle(treatPileupBedGraph);
		files.add(treatPileupBedGraph);
		treatPileupBedGraphG.setFileType(macs2TreatPileupBedGraphFileType);
		treatPileupBedGraphG.setDescription(treatPileupBedGraph.getFileName());
		treatPileupBedGraphG.setSoftwareGeneratedBy(macs2);
		treatPileupBedGraphG.setSampleSources(setOfCellLibrariesForDerivedFrom);
		treatPileupBedGraphG = fileService.addFileGroup(treatPileupBedGraphG);
		this.treatPileupBedGraphGId = treatPileupBedGraphG.getId();
	
		FileGroup controlLambdaBedGraphG = new FileGroup();
		FileHandle controlLambdaBedGraph = new FileHandle();
		controlLambdaBedGraph.setFileName(prefixForFileName + "_control_lambda.bdg");
		listOfFileHandleNames.add(prefixForFileName + "_control_lambda.bdg");
		controlLambdaBedGraph.setFileType(macs2ControlLambdaBedGraphFileType);
		controlLambdaBedGraph = fileService.addFile(controlLambdaBedGraph);
		controlLambdaBedGraphG.addFileHandle(controlLambdaBedGraph);
		files.add(controlLambdaBedGraph);
		controlLambdaBedGraphG.setFileType(macs2ControlLambdaBedGraphFileType);
		controlLambdaBedGraphG.setDescription(controlLambdaBedGraph.getFileName());
		controlLambdaBedGraphG.setSoftwareGeneratedBy(macs2);
		controlLambdaBedGraphG.setSampleSources(setOfCellLibrariesForDerivedFrom);
		controlLambdaBedGraphG = fileService.addFileGroup(controlLambdaBedGraphG);
		this.controlLambdaBedGraphGId = controlLambdaBedGraphG.getId();

		
		
		Imagemagick imagemagickSoftware = (Imagemagick) macs2.getSoftwareDependencyByIname("imagemagick"); 
		R rSoftware = (R) macs2.getSoftwareDependencyByIname("rPackage");


		//the pdf (generated from running Rscript on xx_model.r file)
		FileGroup modelPdfG = new FileGroup();
		FileHandle modelPdf = new FileHandle();
		modelPdf.setFileName(pdfFileName);
		listOfFileHandleNames.add(pdfFileName);
		modelPdf.setFileType(macs2ModelPdfFileType);
		modelPdf = fileService.addFile(modelPdf);
		modelPdfG.addFileHandle(modelPdf);
		files.add(modelPdf);
		modelPdfG.setFileType(macs2ModelPdfFileType);
		modelPdfG.setDescription(modelPdf.getFileName());
		modelPdfG.setSoftwareGeneratedBy(rSoftware);
		modelPdfG.addDerivedFrom(modelScriptG);
		modelPdfG = fileService.addFileGroup(modelPdfG);
		this.modelPdfGId = modelPdfG.getId();
		logger.debug("new ---   recorded fileGroup and fileHandle for rscript to create pdf in MacstwoGenerateModelAsPdfTasklet.doExecute()");

		//the png (converted from the pdf using ImageMagick)
		FileGroup modelPngG = new FileGroup();
		FileHandle modelPng = new FileHandle();
		modelPng.setFileName(pngFileName);
		listOfFileHandleNames.add(pngFileName);
		modelPng.setFileType(macs2ModelPngFileType);
		modelPng = fileService.addFile(modelPng);
		modelPngG.addFileHandle(modelPng);
		files.add(modelPng);
		modelPngG.setFileType(macs2ModelPngFileType);
		modelPngG.setDescription(modelPng.getFileName());
		modelPngG.setSoftwareGeneratedBy(imagemagickSoftware);
		modelPngG.addDerivedFrom(modelPdfG);
		modelPngG = fileService.addFileGroup(modelPngG);
		this.modelPngGId = modelPngG.getId();
		logger.debug("new --- recorded fileGroup and fileHandle for ImageMagick to create png in MacstwoGenerateModelAsPdfTasklet.doExecute()");

		
		
		//6-18-14
		//deal with the new macs2Analysis fileGroup
		//WHEN THHIS IS A SINGLE FILEGROUP FOR ALL FILES, then do:
		//BUT NOT FOR modelScriptG, use the new name  whatever that is modelScriptG.setDerivedFrom(derrivedFromFileGroups);
		//but keep the other meta just in case, as fileGroupMeta
		//since the fileGroup will have a file type of chipSeqAnalysis.macs2 with a name of previxForFileName
		FileGroup macs2AnalysisFileGroup = new FileGroup();
		macs2AnalysisFileGroup.setFileType(macs2AnalysisFileType);
		macs2AnalysisFileGroup.setDescription(prefixForFileName);
		macs2AnalysisFileGroup.setSoftwareGeneratedBy(macs2);
		macs2AnalysisFileGroup.setDerivedFrom(derrivedFromFileGroups);
		macs2AnalysisFileGroup.setFileHandles(files);
		macs2AnalysisFileGroup = fileService.addFileGroup(macs2AnalysisFileGroup);
		this.macs2AnalysisFileGroupId = macs2AnalysisFileGroup.getId();
		logger.debug("new --- recorded all encompassing fileGroup macs2AnalysisFileGroup as a container for files outputted by macs2");
	
		logger.debug("recorded fileGroups and fileHandles for macs2 files in MacstwoTasklet.doExecute()");
		
		//place in the step context in case of crash
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
		stepContext.put("jobId", this.jobId); 
		stepContext.put("testCellLibraryIdListAsString", this.testCellLibraryIdListAsString); 
		stepContext.put("controlCellLibraryIdListAsString", this.controlCellLibraryIdListAsString); 		
		stepContext.put("testSampleId", this.testSampleId); 
		stepContext.put("controlSampleId", this.controlSampleId); 	 
		stepContext.put("modelScriptGId", this.modelScriptGId);
		stepContext.put("peaksXlsGId", this.peaksXlsGId);
		stepContext.put("narrowPeaksBedGId", this.narrowPeaksBedGId);
		stepContext.put("summitsBedGId", this.summitsBedGId);
		stepContext.put("summitsModifiedBedGId", this.summitsModifiedBedGId);
		stepContext.put("treatPileupBedGraphGId", this.treatPileupBedGraphGId);
		stepContext.put("controlLambdaBedGraphGId", this.controlLambdaBedGraphGId);
		
		stepContext.put("modelPdfGId", this.modelPdfGId);
		stepContext.put("modelPngGId", this.modelPngGId);
		
		stepContext.put("macs2AnalysisFileGroupId", macs2AnalysisFileGroupId);
		
		stepContext.put("commandLineCall", this.commandLineCall);
		logger.debug("saved variables in stepContext in case of crash in MacstwoTasklet.doExecute()");

		w.setResultFiles(files);
		
		logger.debug("executed w.getResultFiles().add(x) for " + files.size() + " FileHandles");
		
		w.setResultsDirectory(fileService.generateJobSoftwareBaseFolderName(job, macs2));	
		
		int counter = 0;
		for(String fileName : listOfFileHandleNames){//need to make these symbolic links in order to properly copy files
			w.addCommand("ln -s " + fileName + " ${" + WorkUnit.OUTPUT_FILE+"["+counter+"]}");
			logger.debug("add command: " + "ln -s " + fileName + " ${" + WorkUnit.OUTPUT_FILE+"["+counter+"]}");
			counter++;
		}
		
		logger.debug("executed w.setResultsDirectory(a/jobId) in MacstwoTasklet.doExecute()");

		GridResult result = gridHostResolver.execute(w);
		logger.debug("****Executed gridHostResolver.execute(w) in MactwoTasklet.doExecute()");
		storeStartedResult(context, result);//place the grid result in the step context
	
	}
	

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return super.afterStep(stepExecution);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		super.beforeStep(stepExecution);
		logger.debug("*****  StepExecutionListener beforeStep saving StepExecution in MacstwoTasklet.beforeStep");
		this.stepExecution = stepExecution;				
		//JobExecution jobExecution = stepExecution.getJobExecution();
		//ExecutionContext jobContext = jobExecution.getExecutionContext();
		//this.scratchDirectory = jobContext.get("scrDir").toString();
	
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
		//in case of crash
		if(this.jobId==null){//set initially in constructor
			this.jobId = (Integer) stepContext.get("jobId");
		}
		if(this.testCellLibraryIdListAsString==null){//set initially in constructor
			this.testCellLibraryIdListAsString = (String) stepContext.get("testCellLibraryIdListAsString");
		}
		if(this.controlCellLibraryIdListAsString==null){//set initially in constructor
			this.controlCellLibraryIdListAsString = (String) stepContext.get("controlCellLibraryIdListAsString");
		}
		this.modelScriptGId = (Integer) stepContext.get("modelScriptGId");
		this.peaksXlsGId = (Integer) stepContext.get("peaksXlsGId");
		this.narrowPeaksBedGId = (Integer) stepContext.get("narrowPeaksBedGId");
		this.summitsBedGId = (Integer) stepContext.get("summitsBedGId");
		this.summitsModifiedBedGId = (Integer) stepContext.get("summitsModifiedBedGId");
		this.treatPileupBedGraphGId = (Integer) stepContext.get("treatPileupBedGraphGId");
		this.controlLambdaBedGraphGId = (Integer) stepContext.get("controlLambdaBedGraphGId");
		this.testSampleId = (Integer) stepContext.get("testSampleId");
		this.controlSampleId = (Integer) stepContext.get("controlSampleId");	
		this.commandLineCall = (String) stepContext.get("commandLineCall");	
		
		this.modelPdfGId = (Integer) stepContext.get("modelPdfGId");
		this.modelPngGId = (Integer) stepContext.get("modelPngGId");
		
		this.macs2AnalysisFileGroupId = (Integer) stepContext.get("macs2AnalysisFileGroupId");
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public void doPreFinish(ChunkContext context) throws Exception {
		
		logger.debug("starting doPreFinish() in MacstwoTasklet");
		
		//at Andy's suggestion, do this here too:
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
		this.jobId = (Integer) stepContext.get("jobId");//currently, not really needed here
		this.testCellLibraryIdListAsString = (String) stepContext.get("testCellLibraryIdListAsString");
		this.controlCellLibraryIdListAsString = (String) stepContext.get("controlCellLibraryIdListAsString");
		this.modelScriptGId = (Integer) stepContext.get("modelScriptGId");
		this.peaksXlsGId = (Integer) stepContext.get("peaksXlsGId");
		this.narrowPeaksBedGId = (Integer) stepContext.get("narrowPeaksBedGId");
		this.summitsBedGId = (Integer) stepContext.get("summitsBedGId");
		this.summitsModifiedBedGId = (Integer) stepContext.get("summitsModifiedBedGId");
		this.treatPileupBedGraphGId = (Integer) stepContext.get("treatPileupBedGraphGId");
		this.controlLambdaBedGraphGId = (Integer) stepContext.get("controlLambdaBedGraphGId");
		this.testSampleId = (Integer) stepContext.get("testSampleId");
		this.controlSampleId = (Integer) stepContext.get("controlSampleId");	
		this.commandLineCall = (String) stepContext.get("commandLineCall");	
		
		this.modelPdfGId = (Integer) stepContext.get("modelPdfGId");
		this.modelPngGId = (Integer) stepContext.get("modelPngGId");
		
		this.macs2AnalysisFileGroupId = (Integer) stepContext.get("macs2AnalysisFileGroupId");
		
		// associate test sample with the new file groups		
		Sample testSample = sampleService.getSampleById(testSampleId);		
		
		logger.debug("in middle of doPreFinish() in MacstwoTasklet");

		if (this.modelScriptGId != null && testSample.getId() != 0){
			////fileService.setSampleFile(fileService.getFileGroupById(modelScriptGId), testSample);
			FileGroup fg = fileService.getFileGroupById(this.modelScriptGId);
			fileService.setSampleFile(fg, testSample);
			FileGroupMeta fgm = new FileGroupMeta();
			fgm.setK("chipseqAnalysis.controlId");
			fgm.setV(this.controlSampleId.toString());
			fgm.setFileGroupId(fg.getId());
			List<FileGroupMeta> fgmList = new ArrayList<FileGroupMeta>();
			fgmList.add(fgm);
			fileService.saveFileGroupMeta(fgmList, fg);
		}
		if (this.peaksXlsGId != null && testSample.getId() != 0){
			////fileService.setSampleFile(fileService.getFileGroupById(peaksXlsGId), testSample);
			FileGroup fg = fileService.getFileGroupById(this.peaksXlsGId);
			fileService.setSampleFile(fg, testSample);
			FileGroupMeta fgm = new FileGroupMeta();
			fgm.setK("chipseqAnalysis.controlId");
			fgm.setV(this.controlSampleId.toString());
			fgm.setFileGroupId(fg.getId());
			List<FileGroupMeta> fgmList = new ArrayList<FileGroupMeta>();
			fgmList.add(fgm);
			fileService.saveFileGroupMeta(fgmList, fg);
		}
		if (this.narrowPeaksBedGId != null && testSample.getId() != 0){
			////fileService.setSampleFile(fileService.getFileGroupById(narrowPeaksBedGId), testSample);
			FileGroup fg = fileService.getFileGroupById(this.narrowPeaksBedGId);
			fileService.setSampleFile(fg, testSample);
			FileGroupMeta fgm = new FileGroupMeta();
			fgm.setK("chipseqAnalysis.controlId");
			fgm.setV(this.controlSampleId.toString());
			fgm.setFileGroupId(fg.getId());
			List<FileGroupMeta> fgmList = new ArrayList<FileGroupMeta>();
			fgmList.add(fgm);
			fileService.saveFileGroupMeta(fgmList, fg);
		}
		if (this.summitsBedGId != null && testSample.getId() != 0){
			///fileService.setSampleFile(fileService.getFileGroupById(summitsBedGId), testSample);
			FileGroup fg = fileService.getFileGroupById(this.summitsBedGId);
			fileService.setSampleFile(fg, testSample);
			FileGroupMeta fgm = new FileGroupMeta();
			fgm.setK("chipseqAnalysis.controlId");
			fgm.setV(this.controlSampleId.toString());
			fgm.setFileGroupId(fg.getId());
			List<FileGroupMeta> fgmList = new ArrayList<FileGroupMeta>();
			fgmList.add(fgm);
			fileService.saveFileGroupMeta(fgmList, fg);
		}
		if (this.summitsModifiedBedGId != null && testSample.getId() != 0){
			///fileService.setSampleFile(fileService.getFileGroupById(summitsBedGId), testSample);
			FileGroup fg = fileService.getFileGroupById(this.summitsModifiedBedGId);
			fileService.setSampleFile(fg, testSample);
			FileGroupMeta fgm = new FileGroupMeta();
			fgm.setK("chipseqAnalysis.controlId");
			fgm.setV(this.controlSampleId.toString());
			fgm.setFileGroupId(fg.getId());
			List<FileGroupMeta> fgmList = new ArrayList<FileGroupMeta>();
			fgmList.add(fgm);
			fileService.saveFileGroupMeta(fgmList, fg);
		}
		if (this.treatPileupBedGraphGId != null && testSample.getId() != 0){
			///fileService.setSampleFile(fileService.getFileGroupById(treatPileupBedGraphGId), testSample);
			FileGroup fg = fileService.getFileGroupById(this.treatPileupBedGraphGId);
			fileService.setSampleFile(fg, testSample);
			FileGroupMeta fgm = new FileGroupMeta();
			fgm.setK("chipseqAnalysis.controlId");
			fgm.setV(this.controlSampleId.toString());
			fgm.setFileGroupId(fg.getId());
			List<FileGroupMeta> fgmList = new ArrayList<FileGroupMeta>();
			fgmList.add(fgm);
			fileService.saveFileGroupMeta(fgmList, fg);
		}
		if (this.controlLambdaBedGraphGId != null && testSample.getId() != 0){
			///fileService.setSampleFile(fileService.getFileGroupById(controlLambdaBedGraphGId), testSample);
			FileGroup fg = fileService.getFileGroupById(this.controlLambdaBedGraphGId);
			fileService.setSampleFile(fg, testSample);
			FileGroupMeta fgm = new FileGroupMeta();
			fgm.setK("chipseqAnalysis.controlId");
			fgm.setV(this.controlSampleId.toString());
			fgm.setFileGroupId(fg.getId());
			List<FileGroupMeta> fgmList = new ArrayList<FileGroupMeta>();
			fgmList.add(fgm);
			fileService.saveFileGroupMeta(fgmList, fg);
		}
		
		
		
		
		
		if (this.modelPdfGId != null && testSample.getId() != 0){
			////fileService.setSampleFile(fileService.getFileGroupById(modelPdfGId), testSample);
			FileGroup fg = fileService.getFileGroupById(this.modelPdfGId);
			fileService.setSampleFile(fg, testSample);
			FileGroupMeta fgm = new FileGroupMeta();
			fgm.setK("chipseqAnalysis.controlId");
			fgm.setV(this.controlSampleId.toString());//could be zero
			fgm.setFileGroupId(fg.getId());
			List<FileGroupMeta> fgmList = new ArrayList<FileGroupMeta>();
			fgmList.add(fgm);
			fileService.saveFileGroupMeta(fgmList, fg);
		}
		
		if (this.modelPngGId != null && testSample.getId() != 0){
			////fileService.setSampleFile(fileService.getFileGroupById(modelPdfGId), testSample);
			FileGroup fg = fileService.getFileGroupById(this.modelPngGId);
			fileService.setSampleFile(fg, testSample);
			FileGroupMeta fgm = new FileGroupMeta();
			fgm.setK("chipseqAnalysis.controlId");
			fgm.setV(this.controlSampleId.toString());//could be zero
			fgm.setFileGroupId(fg.getId());
			List<FileGroupMeta> fgmList = new ArrayList<FileGroupMeta>();
			fgmList.add(fgm);
			fileService.saveFileGroupMeta(fgmList, fg);
		}
		
		
		
		
		
		//new, 5-9-14
		logger.debug("new stuff, added 5-9-14, to get numbers from txt files that are not saved");
		//context.getStepContext().attributeNames().
		GridResult result = getStartedResult(context);
		String workingDir = result.getWorkingDirectory();//is this the scratch, I think so
		
		WorkUnit w = new WorkUnit();
		w.setProcessMode(ProcessMode.SINGLE);
		String totalCountMappedReadsAsString = "";
		String totalCountMappedReadsInPeaksAsString = "";
		
		try {
			GridWorkService workService = gridHostResolver.getGridWorkService(w);
			GridTransportConnection transportConnection = workService.getTransportConnection();
			w.setWorkingDirectory(workingDir);
			w.addCommand("cat totalCountMappedReads.txt");//will appear on first line of output
			w.addCommand("cat totalCountMappedReadsInPeaks.txt");//will appear on second line of output
			
			GridResult r = transportConnection.sendExecToRemote(w);
			InputStream is = r.getStdOutStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is)); 
			boolean keepReading = true;
			int lineNumber = 0;
			while (keepReading){
				lineNumber++;
				String line = null;
				line = br.readLine();
				logger.debug("line number = " + lineNumber + " and line = " + line);
				if (line == null)
					keepReading = false;
				else{
					if (lineNumber == 1){
						totalCountMappedReadsAsString = line.replaceAll("\\n", "");//just in case there is a trailing new line
						logger.debug("totalCountMappedReadsAsString = " + totalCountMappedReadsAsString);
					} else if (lineNumber == 2){
						totalCountMappedReadsInPeaksAsString = line.replaceAll("\\n", "");//just in case there is a trailing new line;
						logger.debug("totalCountMappedReadsInPeaksAsString = " + totalCountMappedReadsInPeaksAsString);
					} else {
						keepReading = false;
					}
				}
			}
			br.close();
			
		} catch (Exception e) {
			logger.debug("unable to get totalCountMappedReads.txt value and/or totalCountMappedReadsInPeaks.txt in MacsTwo");
		} 
		
		logger.debug("getting ready to save testSample metadata  in MacstwoTasklet");
		
		// register commandLineCall, testCellLibraryIdList, controlCellLibraryIdList and  controlId with sampleMeta 
		//and record totalCountMappedReads, totalCountMappedReadsInPeaks, [FRIP statistic - will be derived from totalCountMappedReadsInPeaks / totalCountMappedReadsInPeaks]
		List<SampleMeta> testSampleMetaList = testSample.getSampleMeta();
		
		SampleMeta sm1 = new SampleMeta();
		sm1.setK("chipseqAnalysis.testCellLibraryIdList" + "::" + this.controlSampleId.toString());
		sm1.setV(this.testCellLibraryIdListAsString);
		testSampleMetaList.add(sm1);
		
		SampleMeta sm2 = new SampleMeta();
		sm2.setK("chipseqAnalysis.controlCellLibraryIdList" + "::" + this.controlSampleId.toString());
		sm2.setV(this.controlCellLibraryIdListAsString);
		testSampleMetaList.add(sm2);
		
		SampleMeta sm3 = new SampleMeta();
		sm3.setK("chipseqAnalysis.commandLineCall" + "::" + this.controlSampleId.toString());
		sm3.setV(this.commandLineCall);
		testSampleMetaList.add(sm3);
		
		SampleMeta sm4 = new SampleMeta();
		sm4.setK("chipseqAnalysis.controlId" + "::" + this.controlSampleId.toString());
		sm4.setV(this.controlSampleId.toString());
		testSampleMetaList.add(sm4);	
		
		SampleMeta sm5 = new SampleMeta();
		sm5.setK("chipseqAnalysis.totalCountMappedReads" + "::" + this.controlSampleId.toString());
		sm5.setV(totalCountMappedReadsAsString);
		testSampleMetaList.add(sm5);	
		SampleMeta sm6 = new SampleMeta();
		sm6.setK("chipseqAnalysis.totalCountMappedReadsInPeaks" + "::" + this.controlSampleId.toString());
		sm6.setV(totalCountMappedReadsInPeaksAsString);
		testSampleMetaList.add(sm6);		
		
		sampleService.saveSampleWithAssociatedMeta(testSample);
		
		
		//new 6-18-14
		if (this.macs2AnalysisFileGroupId != null && testSample.getId() != 0){
			FileGroup fg = fileService.getFileGroupById(this.macs2AnalysisFileGroupId);
			fileService.setSampleFile(fg, testSample);
			
			List<FileGroupMeta> fgmList = new ArrayList<FileGroupMeta>();
			
			FileGroupMeta fgm = new FileGroupMeta();
			fgm.setK("macs2Analysis.controlId");//could be gotten from the derivedFrom data
			fgm.setV(this.controlSampleId.toString());
			fgm.setFileGroupId(fg.getId());
			fgmList.add(fgm);
			
			FileGroupMeta fgm2 = new FileGroupMeta();
			fgm2.setK("macs2Analysis.testId");//sample --> sampleFileGroup table; but could be gotten from the derivedFrom data
			fgm2.setV(testSample.getId().toString());
			fgm2.setFileGroupId(fg.getId());
			fgmList.add(fgm2);
			
			FileGroupMeta fgm3 = new FileGroupMeta();
			fgm3.setK("macs2Analysis.testCellLibraryIdList");//these are now really stored in derivedFrom, as list of Bam files
			fgm3.setV(this.testCellLibraryIdListAsString);
			fgm3.setFileGroupId(fg.getId());
			fgmList.add(fgm3);
			
			FileGroupMeta fgm4 = new FileGroupMeta();
			fgm4.setK("macs2Analysis.controlCellLibraryIdList");//these are now really stored in derivedFrom, as list of Bam files
			fgm4.setV(this.controlCellLibraryIdListAsString);
			fgm4.setFileGroupId(fg.getId());
			fgmList.add(fgm4);
			
			FileGroupMeta fgm5 = new FileGroupMeta();
			fgm5.setK("macs2Analysis.commandLineCall");
			fgm5.setV(this.commandLineCall);
			fgm5.setFileGroupId(fg.getId());
			fgmList.add(fgm5);
			
			FileGroupMeta fgm6 = new FileGroupMeta();
			fgm6.setK("macs2Analysis.totalCountMappedReads");
			fgm6.setV(totalCountMappedReadsAsString);
			fgm6.setFileGroupId(fg.getId());
			fgmList.add(fgm6);
			
			FileGroupMeta fgm7 = new FileGroupMeta();
			fgm7.setK("macs2Analysis.totalCountMappedReadsInPeaksAsString");
			fgm7.setV(totalCountMappedReadsInPeaksAsString);
			fgm7.setFileGroupId(fg.getId());			
			fgmList.add(fgm7);
			
			fileService.saveFileGroupMeta(fgmList, fg);
		}

		logger.debug("ending doPreFinish() in MacstwoTasklet");
	}
}
