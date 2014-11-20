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
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
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
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ProcessMode;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.macstwo.integration.messages.MacstwoSoftwareJobParameters;
import edu.yu.einstein.wasp.macstwo.service.MacstwoService;
import edu.yu.einstein.wasp.macstwo.software.Macstwo;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.mps.grid.software.Imagemagick;
import edu.yu.einstein.wasp.plugin.mps.grid.software.R;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;

public class MacstwoTaskletModel extends WaspRemotingTasklet implements StepExecutionListener {

	private String jobIdAsString = "";
	private Integer jobId = null;
	private Job job = null;	
	private String prefixForFileName = "";
	private String macs2AnalysisFileGroupIdAsString = "";
	private String workingDirectory = "";
	private String resultsDirectory = "";
	private Integer macs2AnalysisFileGroupId = null;
	
	private String commandLineCall;	
	private String softwareIdUsedListAsString;
	
	private StepExecution stepExecution;
	
	@Autowired
	private FileType textFileType;//here, the model.r script output file
	@Autowired
	private FileType pdfFileType;//output pdf file after running the model.r script
	@Autowired
	private FileType pngFileType;//output after imagemagik on the pdf file
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private FileService fileService;

	@Autowired
	private GridHostResolver gridHostResolver;
	
	@Autowired
	private Macstwo macs2;
	
	@Autowired
	private MacstwoService macstwoService;

	public MacstwoTaskletModel() {
		// proxy
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
	public GridResult doExecute(ChunkContext context) throws Exception {
		
		logger.debug("Starting MacstwoTaskletModel execute");
		
		JobExecution jobExecution = this.stepExecution.getJobExecution();
		ExecutionContext executionContext = jobExecution.getExecutionContext();
				
		if(executionContext.containsKey(MacstwoService.JOB_ID_AS_STRING)){
			this.jobIdAsString = (String) executionContext.get(MacstwoService.JOB_ID_AS_STRING);
			this.jobId = Integer.parseInt(jobIdAsString);
			this.job = jobService.getJobByJobId(jobId);
		}
		if(executionContext.containsKey(MacstwoService.PREFIX_FOR_FILE_NAME)){
			this.prefixForFileName = (String) executionContext.get(MacstwoService.PREFIX_FOR_FILE_NAME);
		}		
		if(executionContext.containsKey(MacstwoService.MACSTWO_ANALYSIS_FILEGROUP_ID_AS_STRING)){
			this.macs2AnalysisFileGroupIdAsString = (String) executionContext.get(MacstwoService.MACSTWO_ANALYSIS_FILEGROUP_ID_AS_STRING);
			macs2AnalysisFileGroupId = Integer.parseInt(macs2AnalysisFileGroupIdAsString);
		}		
		if(executionContext.containsKey(MacstwoService.WORKING_DIRECTORY)){
			this.workingDirectory = (String) executionContext.get(MacstwoService.WORKING_DIRECTORY);
		}		
		if(executionContext.containsKey(MacstwoService.RESULTS_DIRECTORY)){
			this.resultsDirectory = (String) executionContext.get(MacstwoService.RESULTS_DIRECTORY);
		}
		logger.debug("in MacstwoTaskletModel.doExecute this.jobIdAsString: " + this.jobIdAsString);
		logger.debug("in MacstwoTaskletModel.doExecute this.jobId (an integer.tostring()): " + this.jobId.toString());
		logger.debug("in MacstwoTaskletModel.doExecute this.job.getName(): " + job.getName()); 
		logger.debug("in MacstwoTaskletModel.doExecute this.prefixForFileName: " + this.prefixForFileName);
		logger.debug("in MacstwoTaskletModel.doExecute this.macs2AnalysisFileGroupIdAsString: " + this.macs2AnalysisFileGroupIdAsString);
		logger.debug("in MacstwoTaskletModel.doExecute this.macs2AnalysisFileGroupId (an integer.tostring()): " + this.macs2AnalysisFileGroupId.toString());
		logger.debug("in MacstwoTaskletModel.doExecute this.workingDirectory: " + this.workingDirectory);
		logger.debug("in MacstwoTaskletModel.doExecute this.resultsDirectory: " + this.resultsDirectory);
		
		Assert.assertTrue(!this.jobIdAsString.isEmpty());
		Assert.assertTrue(this.jobId!=null);
		Assert.assertTrue(this.job!=null && this.job.getId()!=null);
		Assert.assertTrue(!this.prefixForFileName.isEmpty());
		Assert.assertTrue(!this.macs2AnalysisFileGroupIdAsString.isEmpty());
		Assert.assertTrue(this.macs2AnalysisFileGroupId!=null);
		Assert.assertTrue(!this.workingDirectory.isEmpty());
		Assert.assertTrue(!this.resultsDirectory.isEmpty());	
		
		logger.debug("preparing to generate workunit in MacstwoTaskletModel.doExecute()");
		
		String modelFileName = prefixForFileName + "_model.r";
		String pdfFileName = modelFileName.replaceAll(".r$", ".pdf");//abc_model.r will be used to generate abc_model.pdf
		String pngFileName = modelFileName.replaceAll(".r$", ".png");//abc_model.pdf will be used to generate abc_model.png
	
		WorkUnit w = macs2.getModel(modelFileName, pdfFileName, pngFileName);//configure
		logger.debug("OK, workunit has been generated in MacstwoTaskletModel.doExecute()");
		
		this.commandLineCall = w.getCommand();
		this.commandLineCall = this.commandLineCall.replaceAll("\\n", "<br /><br />");//the workunit tagged on a newline at the end of the command; so remove it for db storage and replace with <br /> for display purposes
		//will add commandLineCall (concat); see below
		Imagemagick imagemagickSoftware = (Imagemagick) macs2.getSoftwareDependencyByIname("imagemagick"); 
		R rSoftware = (R) macs2.getSoftwareDependencyByIname("rPackage");
		this.softwareIdUsedListAsString = rSoftware.getId().toString() + ":" + imagemagickSoftware.getId().toString();
		//will add (concatenate) with existing softwareIdUsedListAsString; see below

		//deal with the files
		List<String> listOfFileHandleNames = new ArrayList<String>();		
		LinkedHashSet<FileHandle> files = new LinkedHashSet<FileHandle>();		
		Set <FileGroup> innerFileGroupSet = new HashSet<FileGroup>();		
		
		//the prefixForFileName_model.r file (which may or may not have already been generated). Well, if we are at this line, a decider has already determined that this file HAS BEEN generated.
		FileHandle modelScriptFileHandle = macstwoService.createAndSaveInnerFileHandle(modelFileName, textFileType);
		listOfFileHandleNames.add(modelScriptFileHandle.getFileName());
		files.add(modelScriptFileHandle);
		FileGroup modelScriptFileGroup = macstwoService.createAndSaveInnerFileGroup(modelScriptFileHandle, macs2, "xxx_model.r is a macs2-generated R script that can be converted into a pdf (using RScript) and illustrates the peak model");
		innerFileGroupSet.add(modelScriptFileGroup);
		
		//the pdf (generated from running Rscript on xx_model.r file)
		FileHandle modelPdfFileHandle = macstwoService.createAndSaveInnerFileHandle(pdfFileName, pdfFileType);
		listOfFileHandleNames.add(modelPdfFileHandle.getFileName());
		files.add(modelPdfFileHandle);
		FileGroup modelPdfFileGroup = macstwoService.createAndSaveInnerFileGroup(modelPdfFileHandle, rSoftware, "xxx_model.pdf is a pdf file generated by running RScript against xxx_model.r and is an image of the read distribution in model peaks and fragment size estimation");
		innerFileGroupSet.add(modelPdfFileGroup);
		
		//the png (converted from the pdf using ImageMagick)
		FileHandle modelPngFileHandle = macstwoService.createAndSaveInnerFileHandle(pngFileName, pngFileType);
		listOfFileHandleNames.add(modelPngFileHandle.getFileName());
		files.add(modelPngFileHandle);
		FileGroup modelPngFileGroup = macstwoService.createAndSaveInnerFileGroup(modelPngFileHandle, imagemagickSoftware, "xxx_model.png is a png image file converted from xxx_model.pdf using ImageMagick");
		innerFileGroupSet.add(modelPngFileGroup);

		//important; add these three files to the collection
		FileGroup macs2AnalysisFileGroup = fileService.getFileGroupById(macs2AnalysisFileGroupId.intValue());		
		macs2AnalysisFileGroup = fileService.addToFileGroupCollection(macs2AnalysisFileGroup, innerFileGroupSet);
		logger.debug("dealt with files in in MacstwoTaskletModel.doExecute()");
		
		//place in the step context in case of crash
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
		stepContext.put("jobId", this.jobId); //needed? I don't believe so				
		stepContext.put("macs2AnalysisFileGroupId", this.macs2AnalysisFileGroupId);
		stepContext.put("softwareIdUsedListAsString", this.softwareIdUsedListAsString);		
		stepContext.put("commandLineCall", this.commandLineCall);
		logger.debug("saved variables in stepContext in case of crash in MacstwoTaskletModel.doExecute()");
		
		w.setResultFiles(files);
		
		/// use next two lines here instead      w.getConfiguration().setResultsDirectory(fileService.generateJobSoftwareBaseFolderName(job, macs2));			
		w.getConfiguration().setResultsDirectory(resultsDirectory);		
		w.getConfiguration().setWorkingDirectory(workingDirectory);
		logger.debug("w.getConfiguration().setResultsDirectory() set as: " + resultsDirectory);
		logger.debug("w.getConfiguration().setWorkingDirectory() set as: " + workingDirectory);
		
		int counter = 0;
		for(String fileName : listOfFileHandleNames){//need to make these symbolic links in order to properly copy files
			w.addCommand("ln -sf " + fileName + " ${" + WorkUnit.OUTPUT_FILE+"["+counter+"]}");
			logger.debug("add command: " + "ln -sf " + fileName + " ${" + WorkUnit.OUTPUT_FILE+"["+counter+"]}");
			counter++;
		}
		
		GridResult result = gridHostResolver.execute(w);
		logger.debug("***********Executed gridHostResolver.execute(w) in MactwoTaskletModel.doExecute()");		
				
		return result;
	
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
		logger.debug("*****  StepExecutionListener beforeStep saving StepExecution in MacstwoTaskletModel.beforeStep");
		this.stepExecution = stepExecution;				
	
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
		//in case of crash
		if(this.jobId==null){//set initially in constructor
			this.jobId = (Integer) stepContext.get("jobId");
		}
		
		this.macs2AnalysisFileGroupId = (Integer) stepContext.get("macs2AnalysisFileGroupId");	
		this.commandLineCall = (String) stepContext.get("commandLineCall");			
		this.softwareIdUsedListAsString = (String) stepContext.get("softwareIdUsedListAsString");
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public void doPreFinish(ChunkContext context) throws Exception {
		
		logger.debug("starting MacstwoTaskletModeldoPreFinish()");
		
		//at Andy's suggestion, do this here too:
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();		
		this.commandLineCall = (String) stepContext.get("commandLineCall");		
		this.macs2AnalysisFileGroupId = (Integer) stepContext.get("macs2AnalysisFileGroupId");
		this.softwareIdUsedListAsString = (String) stepContext.get("softwareIdUsedListAsString");

		logger.debug("in middle of MacstwoTaskletModel.doPreFinish(), getting ready to set to active the 3 model files and update two meta attributes");
		
		if (this.macs2AnalysisFileGroupId != null){
			FileGroup macs2AnalysisFileGroup = fileService.getFileGroupById(this.macs2AnalysisFileGroupId);
			//macs2AnalysisFileGroup already set to active in previous tasklet
			for(FileGroup childFG : macs2AnalysisFileGroup.getChildren()){
				if(childFG.getIsActive().intValue()==0){
					childFG.setIsActive(1);
					fileService.addFileGroup(childFG);//save new active setting
				}
			}
			///already done fileService.setSampleFile(macs2AnalysisFileGroupId, testSample);			
			List<FileGroupMeta> fgmList = new ArrayList<FileGroupMeta>();			
			//want to update these two outer fileGroup metadata fields 
			boolean foundCommandLineCallMeta = false;
			boolean foundSoftwareIdUsedListAsStringMeta = false;
			for(FileGroupMeta fgm : macs2AnalysisFileGroup.getFileGroupMeta()){//existing meta fields
				if(fgm.getK().equals("macs2Analysis.commandLineCall")){//this is an existing meta field
					fgm.setV(fgm.getV() + this.commandLineCall);//simply concatenate
					fgmList.add(fgm);
					foundCommandLineCallMeta = true;
				}
				if(fgm.getK().equals("macs2Analysis.softwareIdUsedListAsString")){//this is an existing meta field
					fgm.setV(fgm.getV() + ":" + this.softwareIdUsedListAsString);//add : then concatenate
					fgmList.add(fgm);
					foundSoftwareIdUsedListAsStringMeta = true;
				}
			}
			
			if(!foundCommandLineCallMeta){//most likely the field already exists, so will not need to execute this
				FileGroupMeta fgm5 = new FileGroupMeta();
				fgm5.setK("macs2Analysis.commandLineCall");
				fgm5.setV(this.commandLineCall);
				fgm5.setFileGroupId(macs2AnalysisFileGroup.getId());
				fgmList.add(fgm5);
			}
			if(!foundSoftwareIdUsedListAsStringMeta){//most likely the field already exists, so will not need to execute this
				FileGroupMeta fgm8 = new FileGroupMeta();
				fgm8.setK("macs2Analysis.softwareIdUsedListAsString");
				fgm8.setV(this.softwareIdUsedListAsString);
				fgm8.setFileGroupId(macs2AnalysisFileGroup.getId());			
				fgmList.add(fgm8);
			}
			if(!fgmList.isEmpty()){//save the updates as needed
				fileService.saveFileGroupMeta(fgmList, macs2AnalysisFileGroup);
			}
		}

		logger.debug("ending MacstwoTaskletModel.doPreFinish() ");
	}
		
	@Override
	public void doCleanupBeforeRestart(StepExecution stepExecution) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
