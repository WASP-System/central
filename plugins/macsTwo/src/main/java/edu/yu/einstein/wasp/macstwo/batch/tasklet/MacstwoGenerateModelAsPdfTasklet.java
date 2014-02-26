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
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.macstwo.integration.messages.MacstwoSoftwareJobParameters;
import edu.yu.einstein.wasp.macstwo.software.Macstwo;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * @author 
 * 
 */
public class MacstwoGenerateModelAsPdfTasklet extends WaspRemotingTasklet implements StepExecutionListener {

	private Integer jobId;
	private Integer modelScriptGId;//passed in
	private Integer testSampleId;//passed in
	private Integer controlSampleId;//passed in (could be zero - which indicates no control was used)
	private Integer modelPdfGId;//generated in doExecute()

	private StepExecution stepExecution;
	
	@Autowired
	private FileType macs2ModelPdfFileType;
	@Autowired
	private FileType macs2ModelScriptFileType;
	
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
	

	public MacstwoGenerateModelAsPdfTasklet() {
		// proxy
	}

	//this constructor not currently used; could NOT make it obtain parameter
	public MacstwoGenerateModelAsPdfTasklet(String modelScriptGIdAsString) throws Exception {
		logger.debug("***Starting MacstwoGenerateModelAsPdfTasklet constructor");
		logger.debug("modelScriptGIdAsString: " + modelScriptGIdAsString);
		this.modelScriptGId = new Integer(modelScriptGIdAsString);
		Assert.assertTrue(this.modelScriptGId != null);
		Assert.assertTrue(this.modelScriptGId.intValue() > 0);
		logger.debug("value in constructor: this.modelScriptGId (integer): " + this.modelScriptGId.toString());
		logger.debug("Ending MacstwoGenerateModelAsPdfTasklet constructor");
	}
	
//TODO: remove this next method
	@Override
	@Transactional("entityManager")
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		this.doExecute(context);
		return RepeatStatus.FINISHED;
	}

	/**
	 * 
	 * @param contrib
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		logger.debug("*************************************");
		logger.debug("Starting MacstwoGenerateModelAsPdfTasklet execute");
		
		Map<String,Object> jobParametersMap = context.getStepContext().getJobParameters();		
		for (String key : jobParametersMap.keySet()) {
			if(key.equalsIgnoreCase(MacstwoSoftwareJobParameters.JOB_ID)){
				this.jobId = new Integer(jobParametersMap.get(key).toString());
			}
			logger.debug("jobParametersMap Key: " + key + " Value: " + jobParametersMap.get(key).toString());
		}
		Assert.assertTrue(this.jobId != null);
		Assert.assertTrue(this.jobId.intValue() > 0);
		stepExecution.getExecutionContext().put("jobId", this.jobId);//in case of crash
		logger.debug("this.jobId (integer): " + this.jobId.toString());
				
		Map<String,Object> jobExecutionContextMap = context.getStepContext().getJobExecutionContext();		
		for (String key : jobExecutionContextMap.keySet()) {
			if(key.equalsIgnoreCase(MacstwoSoftwareJobParameters.MODEL_SCRIPT_FILEGROUP_ID)){
				this.modelScriptGId = new Integer(jobExecutionContextMap.get(key).toString());
			}
			else if(key.equalsIgnoreCase(MacstwoSoftwareJobParameters.TEST_SAMPLE_ID)){
				this.testSampleId = new Integer(jobExecutionContextMap.get(key).toString());
			}
			else if(key.equalsIgnoreCase(MacstwoSoftwareJobParameters.CONTROL_SAMPLE_ID)){
				this.controlSampleId = new Integer(jobExecutionContextMap.get(key).toString());
			}
			logger.debug("*****      jobExecutionContextMap Key: " + key + " Value: " + jobExecutionContextMap.get(key).toString());
		}
		Assert.assertTrue(this.modelScriptGId != null);
		Assert.assertTrue(this.modelScriptGId.intValue() > 0);
		stepExecution.getExecutionContext().put("modelScriptGId", this.modelScriptGId);//in case of crash
		logger.debug("this.modelScriptGId (integer): " + this.modelScriptGId.toString());		
		
		Assert.assertTrue(this.testSampleId != null);
		Assert.assertTrue(this.testSampleId.intValue() > 0);
		stepExecution.getExecutionContext().put("testSampleId", this.testSampleId);//in case of crash
		logger.debug("this.testSampleId (integer): " + this.testSampleId.toString());		
		
		Assert.assertTrue(this.controlSampleId != null);
		//controlSampleId can be zero
		stepExecution.getExecutionContext().put("controlSampleId", this.controlSampleId);//in case of crash
		logger.debug("this.controlSampleId (integer): " + this.controlSampleId.toString());
		
		FileGroup modelScriptFileGroup = fileService.getFileGroupById(this.modelScriptGId);
		logger.debug("modelScriptFileGroup.description: " + modelScriptFileGroup.getDescription());
		Set<FileHandle> fileHandleSet = modelScriptFileGroup.getFileHandles();
		logger.debug("fileHandleSet.size: " + fileHandleSet.size());
		Assert.assertTrue(fileHandleSet.size()==1);
		for(FileHandle fh : fileHandleSet){
			logger.debug("filehandle.name = " + fh.getFileName());
		}
		FileHandle modelScriptFileHandle = new ArrayList<FileHandle>(fileHandleSet).get(0);
		logger.debug("*****modelScriptFileHandle.name = " + modelScriptFileHandle.getFileName());
		/* //TODO: uncomment this line: */ Assert.assertTrue(modelScriptFileHandle.getFileType().getIName().equalsIgnoreCase(macs2ModelScriptFileType.getIName()));
		String pdfFileName = modelScriptFileHandle.getFileName().replaceAll(".r$", ".pdf");//abc_model.r will be used to generate abc_model.pdf
		logger.debug("*****pdfFileName = " + pdfFileName);
		logger.debug("preparing to generate workunit");
		WorkUnit w = macs2.getModelPdf(modelScriptFileHandle);//configure
		logger.debug("OK, workunit has been generated");
		

	///*				 
		FileGroup modelPdfG = new FileGroup();
		FileHandle modelPdf = new FileHandle();
		modelPdf.setFileName(pdfFileName);
		modelPdf = fileService.addFile(modelPdf);
		modelPdfG.addFileHandle(modelPdf);
		modelPdfG.setFileType(macs2ModelPdfFileType);
		modelPdfG.setDescription(modelPdf.getFileName());
		modelPdfG = fileService.addFileGroup(modelPdfG);
		this.modelPdfGId = modelPdfG.getId();
		stepExecution.getExecutionContext().put("modelPdfGId", this.modelPdfGId);//in case of crash		
		
		w.getResultFiles().add(modelPdfG);
		
		w.setResultsDirectory(WorkUnit.RESULTS_DIR_PLACEHOLDER + "/" + this.jobId.toString());   
		GridResult result = gridHostResolver.execute(w);		
		//place the grid result in the step context
		storeStartedResult(context, result);
	//*/	
		/*testing only
		logger.debug("   getting ready to thow rob-generated exception");
		if(1==1){
			throw new Exception("   throwing Rob-generated exception in MacstwoGenerateModelAsPdfTasklet.execute()");
		}
		logger.debug("   throwing Rob-generated exception in MacstwoGenerateModelAsPdfTasklet.execute()");	
		*/	
	}
	

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return stepExecution.getExitStatus();
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		logger.debug("StepExecutionListener beforeStep saving StepExecution");
		this.stepExecution = stepExecution;		
		//in case of crash
		this.jobId = (Integer) stepExecution.getExecutionContext().get("jobId");
		this.testSampleId = (Integer) stepExecution.getExecutionContext().get("testSampleId");
		this.controlSampleId = (Integer) stepExecution.getExecutionContext().get("controlSampleId");

		this.modelScriptGId = (Integer) stepExecution.getExecutionContext().get("modelScriptGId");
		this.modelPdfGId = (Integer) stepExecution.getExecutionContext().get("modelPdfGId");
	}
	@Override
	@Transactional("entityManager")
	public void doPreFinish(ChunkContext context) throws Exception {
		
		// register file groups 
		
		Sample testSample = sampleService.getSampleById(this.testSampleId);
		
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
	}
}

