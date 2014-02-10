/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.macstwo.batch.tasklet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import edu.yu.einstein.wasp.Assert;

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionExponential;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;

import edu.yu.einstein.wasp.macstwo.software.Macstwo;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Genome;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * @author 
 * 
 */
public class MacstwoTasklet extends WaspTasklet implements StepExecutionListener {

	private List<Integer> testCellLibraryIdList;//treated, such as IP
	private List<Integer> controlCellLibraryIdList;//contol, such as input 
	Integer modelScriptGId;
	Integer peaksXlsGId;
	Integer narrowPeaksBedGId;
	Integer summitsBedGId;
	Integer treatPileupBedGraphGId;
	Integer controlLambdaBedGraphGId;
	Integer testSampleId;
	
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
	private FileType macs2TreatPileupBedGraphFileType;
	@Autowired
	private FileType macs2ControlLambdaBedGraphFileType;
	
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
	private Macstwo macs2;//TODO: how????
	

	public MacstwoTasklet() {
		// proxy
	}

	public MacstwoTasklet(String testCellLibraryIdListAsString, String controlCellLibraryIdListAsString) throws Exception {
		logger.debug("*************************Starting MacstwoTasklet constructor");
		logger.debug("*************************testCellLibraryIdListAsString: " + testCellLibraryIdListAsString);
		logger.debug("*************************controlCellLibraryIdListAsString: " + controlCellLibraryIdListAsString);
		this.testCellLibraryIdList = WaspSoftwareJobParameters.getLibraryCellIdList(testCellLibraryIdListAsString);//should be all from same job
		Assert.assertTrue(!this.testCellLibraryIdList.isEmpty());
		this.controlCellLibraryIdList = WaspSoftwareJobParameters.getLibraryCellIdList(controlCellLibraryIdListAsString);//may be empty
		logger.debug("*************************Ending MacstwoTasklet constructor");
	}

	/**
	 * 
	 * @param contrib
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@Override
	@RetryOnExceptionExponential
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		// if the work has already been started, then check to see if it is finished
		// if not, throw an exception that is caught by the repeat policy.
		
		logger.debug("Starting MacstwoTasklet execute");		
		
		RepeatStatus repeatStatus = super.execute(contrib, context);
		if (repeatStatus.equals(RepeatStatus.FINISHED)){
			// register .bam and .bai file groups with cellLib so as to make available to views
			Sample testSample = sampleService.getSampleById(testSampleId);
			if (modelScriptGId != null && testSample.getId() != 0)
				fileService.setSampleFile(fileService.getFileGroupById(modelScriptGId), testSample);
			if (peaksXlsGId != null && testSample.getId() != 0)
				fileService.setSampleFile(fileService.getFileGroupById(peaksXlsGId), testSample);

			if (narrowPeaksBedGId != null && testSample.getId() != 0)
				fileService.setSampleFile(fileService.getFileGroupById(narrowPeaksBedGId), testSample);
			if (summitsBedGId != null && testSample.getId() != 0)
				fileService.setSampleFile(fileService.getFileGroupById(summitsBedGId), testSample);
			if (treatPileupBedGraphGId != null && testSample.getId() != 0)
				fileService.setSampleFile(fileService.getFileGroupById(treatPileupBedGraphGId), testSample);
			if (controlLambdaBedGraphGId != null && testSample.getId() != 0)
				fileService.setSampleFile(fileService.getFileGroupById(controlLambdaBedGraphGId), testSample);

			return RepeatStatus.FINISHED;
		}
		
		Map<String,Object> jobParameters = context.getStepContext().getJobParameters();		
		for (String key : jobParameters.keySet()) {
			logger.debug("Key: " + key + " Value: " + jobParameters.get(key).toString());
		}

		SampleSource firstTestCellLibrary = sampleService.getCellLibraryBySampleSourceId(this.testCellLibraryIdList.get(0));
		Job job = sampleService.getJobOfLibraryOnCell(firstTestCellLibrary);//should all be from same job
		logger.debug("job name : id = " + job.getName() + " : " + job.getId());
		List<JobMeta> jobMetaList = jobService.getJobMeta(job.getId());
		logger.debug("Size of jobMeta = " + jobMetaList.size());
		Sample testSample = sampleService.getLibrary(firstTestCellLibrary);//all these cellLibraries are from the same library or macromoleucle
		while(testSample.getParent()!=null){
			testSample = testSample.getParent();
		}
		logger.debug("testSample.name = " + testSample.getName());		
		this.testSampleId = testSample.getId();
		stepExecution.getExecutionContext().put("testSampleId", testSampleId);

		List<FileHandle> testFileHandleList = new ArrayList<FileHandle>();		
		for(Integer id : this.testCellLibraryIdList){
			SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(id);
			Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLibrary, bamFileType);
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
			while(controlSample.getParent()!=null){
				controlSample = controlSample.getParent();
			}
		}
		if(controlSample==null){
			logger.debug("controlSample IS NULL");
		}
		else{
			logger.debug("controlSample.name = " + controlSample.getName());
		}
		
		
		List<FileHandle> controlFileHandleList = new ArrayList<FileHandle>();
		for(Integer id : this.controlCellLibraryIdList){
			SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(id);
			Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLibrary, bamFileType);
			logger.debug("control fileGroups size = " + fileGroups.size());
			for(FileGroup fileGroup : fileGroups){
				for(FileHandle fileHandle : fileGroup.getFileHandles()){
					controlFileHandleList.add(fileHandle);//can be empty
					logger.debug("control fileHandle = " + fileHandle.getFileName());
				}
			}
		}
		logger.debug("controlFileHandleList.size = " + controlFileHandleList.size());
			
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
		//TODO: need way to tell that this is using testSample and (perhpas) controlSample
		stepContext.put("testSampleId", testSample.getId()); //place in the step context
		if(controlSample!=null){
			stepContext.put("controlSampleId", controlSample.getId()); //place in the step context			
		}
		//Macstwo macs2 = new Macstwo();
		WorkUnit w = macs2.getPeaks(testSample, controlSample, jobMetaList, testFileHandleList, controlFileHandleList, jobParameters);//configure
		
		//set the output files
		/*
		String bamOutput = fileService.generateUniqueBaseFileName(cellLib) + "bwa.bam";
		FileGroup bamG = new FileGroup();
		FileHandle bam = new FileHandle();
		bam.setFileName(bamOutput);
		bam = fileService.addFile(bam);
		bamG.addFileHandle(bam);
		bamG.setFileType(bamFileType);
		bamG.setDescription(bamOutput);
		bamG = fileService.addFileGroup(bamG);
		bamGId = bamG.getId();
		// save in step context in case of batch restart
		stepExecution.getExecutionContext().put("bamGID", bamGId);
	*/
		FileGroup modelScriptG = new FileGroup();
		FileHandle modelScript = new FileHandle();
		modelScript.setFileName(name + "_model.r");//TODO need to run Rscript on this file to generate pdf - do not know how to do this
		modelScript = fileService.addFile(modelScript);
		modelScriptG.addFileHandle(modelScript);
		modelScriptG.setFileType(macs2ModelScriptFileType);//TODO: define
		modelScriptG.setDescription(modelScript.getFileName());
		modelScriptG = fileService.addFileGroup(modelScriptG);
		modelScriptGId = modelScriptG.getId();
		stepExecution.getExecutionContext().put("modelScriptGId", modelScriptGId);
		//TODO may need to save this to job context, since this file will need to be run by RScript to convert it into model.pdf
		
		FileGroup peaksXlsG = new FileGroup();
		FileHandle peaksXls = new FileHandle();
		peaksXls.setFileName(name + "_peaks.xls");
		peaksXls = fileService.addFile(peaksXls);
		peaksXlsG.addFileHandle(peaksXls);
		peaksXlsG.setFileType(macs2PeaksXlsFileType);//TODO: define
		peaksXlsG.setDescription(peaksXls.getFileName());
		peaksXlsG = fileService.addFileGroup(peaksXlsG);
		peaksXlsGId = peaksXlsG.getId();
		stepExecution.getExecutionContext().put("peaksXlsGId", peaksXlsGId);		
		
		FileGroup narrowPeaksBedG = new FileGroup();
		FileHandle narrowPeaksBed = new FileHandle();
		narrowPeaksBed.setFileName(name + "_peaks.narrowPeak");
		narrowPeaksBed = fileService.addFile(narrowPeaksBed);
		narrowPeaksBedG.addFileHandle(narrowPeaksBed);
		narrowPeaksBedG.setFileType(macs2NarrowPeaksBedFileType);//TODO: define
		narrowPeaksBedG.setDescription(narrowPeaksBed.getFileName());
		narrowPeaksBedG = fileService.addFileGroup(narrowPeaksBedG);
		narrowPeaksBedGId = narrowPeaksBedG.getId();
		stepExecution.getExecutionContext().put("narrowPeaksBedGId", narrowPeaksBedGId);
	
		FileGroup summitsBedG = new FileGroup();
		FileHandle summitsBed = new FileHandle();
		summitsBed.setFileName(name + "_summits.bed");
		summitsBed = fileService.addFile(summitsBed);
		summitsBedG.addFileHandle(summitsBed);
		summitsBedG.setFileType(macs2SummitsBedFileType);//TODO: define
		summitsBedG.setDescription(summitsBed.getFileName());
		summitsBedG = fileService.addFileGroup(summitsBedG);
		summitsBedGId = summitsBedG.getId();		
		stepExecution.getExecutionContext().put("summitsBedGId", summitsBedGId);
		
		FileGroup treatPileupBedGraphG = new FileGroup();
		FileHandle treatPileupBedGraph = new FileHandle();
		treatPileupBedGraph.setFileName(name + "_treat_pileup.bdg");
		treatPileupBedGraph = fileService.addFile(treatPileupBedGraph);
		treatPileupBedGraphG.addFileHandle(treatPileupBedGraph);
		treatPileupBedGraphG.setFileType(macs2TreatPileupBedGraphFileType);//TODO: define
		treatPileupBedGraphG.setDescription(treatPileupBedGraph.getFileName());
		treatPileupBedGraphG = fileService.addFileGroup(treatPileupBedGraphG);
		treatPileupBedGraphGId = treatPileupBedGraphG.getId();
		stepExecution.getExecutionContext().put("treatPileupBedGraphGId", treatPileupBedGraphGId);
	
		FileGroup controlLambdaBedGraphG = new FileGroup();
		FileHandle controlLambdaBedGraph = new FileHandle();
		controlLambdaBedGraph.setFileName(name + "_control_lambda.bdg");
		controlLambdaBedGraph = fileService.addFile(controlLambdaBedGraph);
		controlLambdaBedGraphG.addFileHandle(controlLambdaBedGraph);
		controlLambdaBedGraphG.setFileType(macs2ControlLambdaBedGraphFileType);//TODO: define
		controlLambdaBedGraphG.setDescription(controlLambdaBedGraph.getFileName());
		controlLambdaBedGraphG = fileService.addFileGroup(controlLambdaBedGraphG);
		controlLambdaBedGraphGId = controlLambdaBedGraphG.getId();
		stepExecution.getExecutionContext().put("controlLambdaBedGraphGId", controlLambdaBedGraphGId);

		w.getResultFiles().add(modelScriptG);
		w.getResultFiles().add(peaksXlsG);
		w.getResultFiles().add(narrowPeaksBedG);
		w.getResultFiles().add(summitsBedG);
		w.getResultFiles().add(treatPileupBedGraphG);
		w.getResultFiles().add(controlLambdaBedGraphG);
		
		if(1==1){
			throw new Exception("throwing exception to test");
		}
		
		
		w.setResultsDirectory(WorkUnit.RESULTS_DIR_PLACEHOLDER + "/" + job.getId());
   
		GridResult result = gridHostResolver.execute(w);
		
		//place the grid result in the step context
		storeStartedResult(context, result);

		return RepeatStatus.CONTINUABLE;
	}
	
	@BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
		logger.debug("BeforeStep saving StepExecution");
        this.stepExecution = stepExecution;
    }

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public ExitStatus afterStep(StepExecution arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		logger.debug("StepExecutionListener beforeStep saving StepExecution");
		this.stepExecution = stepExecution;		
		
		//JobExecution jobExecution = stepExecution.getJobExecution();
		//ExecutionContext jobContext = jobExecution.getExecutionContext();
		//this.scratchDirectory = jobContext.get("scrDir").toString();
		//this.cellLibId = (Integer) jobContext.get("cellLibId");
		this.modelScriptGId = (Integer) stepExecution.getExecutionContext().get("modelScriptGId");
		this.peaksXlsGId = (Integer) stepExecution.getExecutionContext().get("peaksXlsGId");
		this.narrowPeaksBedGId = (Integer) stepExecution.getExecutionContext().get("narrowPeaksBedGId");
		this.summitsBedGId = (Integer) stepExecution.getExecutionContext().get("summitsBedGId");
		this.treatPileupBedGraphGId = (Integer) stepExecution.getExecutionContext().get("treatPileupBedGraphGId");
		this.controlLambdaBedGraphGId = (Integer) stepExecution.getExecutionContext().get("controlLambdaBedGraphGId");
		this.testSampleId = (Integer) stepExecution.getExecutionContext().get("testSampleId");
		
	}
}
