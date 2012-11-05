/**
 *
 * JobServiceImpl.java 
 * @author dubin
 *  
 * the JobService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.batch.core.extension.JobExplorerWasp;
import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.dao.FileDao;
import edu.yu.einstein.wasp.dao.JobCellSelectionDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.dao.JobFileDao;
import edu.yu.einstein.wasp.dao.JobMetaDao;
import edu.yu.einstein.wasp.dao.JobResourcecategoryDao;
import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.dao.JobSoftwareDao;
import edu.yu.einstein.wasp.dao.JobUserDao;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.LabUserDao;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.ResourceTypeDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleFileDao;
import edu.yu.einstein.wasp.dao.SampleJobCellSelectionDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.dao.SoftwareDao;
import edu.yu.einstein.wasp.dao.UserDao;

import edu.yu.einstein.wasp.exception.FileMoveException;
import edu.yu.einstein.wasp.exception.InvalidParameterException;
import edu.yu.einstein.wasp.exception.ParameterValueRetrievalException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.JobStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspJobTask;
import edu.yu.einstein.wasp.integration.messages.payload.WaspStatus;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobCellSelection;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftCellSelection;
import edu.yu.einstein.wasp.model.JobDraftFile;
import edu.yu.einstein.wasp.model.JobDraftMeta;
import edu.yu.einstein.wasp.model.JobDraftSoftware;
import edu.yu.einstein.wasp.model.JobDraftresourcecategory;
import edu.yu.einstein.wasp.model.JobFile;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.JobSoftware;
import edu.yu.einstein.wasp.model.JobUser;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftJobDraftCellSelection;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SampleFile;
import edu.yu.einstein.wasp.model.SampleJobCellSelection;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.JobService;

import edu.yu.einstein.wasp.service.WorkflowService;


import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.util.StringHelper;


@Service
@Transactional
public class JobServiceImpl extends WaspMessageHandlingServiceImpl implements JobService {

	private JobDao	jobDao;
	
	/**
	 * setJobDao(JobDao jobDao)
	 * 
	 * @param jobDao
	 * 
	 */
	@Override
	@Autowired
	public void setJobDao(JobDao jobDao) {
		this.jobDao = jobDao;
	}

	/**
	 * getJobDao();
	 * 
	 * @return jobDao
	 * 
	 */
	@Override
	public JobDao getJobDao() {
		return this.jobDao;
	}

	@Autowired
	private JobDraftDao jobDraftDao;
	
	@Autowired
	private SampleMetaDao sampleMetaDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private TaskService taskService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private JobMetaDao jobMetaDao;

	@Autowired
	protected LabDao labDao;

	@Autowired
	protected LabUserDao labUserDao;

	@Autowired
	protected JobUserDao jobUserDao;

	@Autowired
	protected RoleDao roleDao;

	@Autowired
	protected ResourceDao resourceDao;

	@Autowired
	protected ResourceCategoryDao resourceCategoryDao;

	@Autowired
	protected SoftwareDao softwareDao;

	@Autowired
	protected ResourceTypeDao resourceTypeDao;

	@Autowired
	protected SampleDao sampleDao;

	@Autowired
	protected SampleFileDao sampleFileDao;

	@Autowired
	protected JobSampleDao jobSampleDao;
	
	@Autowired
	protected SampleTypeDao sampleTypeDao;
	
	@Autowired
	protected SampleSubtypeDao sampleSubtypeDao;

	@Autowired
	protected SampleSubtypeDao subSampleTypeDao;

	@Autowired
	protected FileDao fileDao;

	@Autowired
	protected JobCellSelectionDao jobCellSelectionDao;
	
	@Autowired
	protected SampleJobCellSelectionDao sampleJobCellSelectionDao;
	
	@Autowired
	protected JobSoftwareDao jobSoftwareDao;
	
	@Autowired
	protected JobResourcecategoryDao jobResourcecategoryDao;
	
	@Autowired
	protected JobFileDao jobFileDao;
	
	protected JobExplorerWasp batchJobExplorer;
	
	@Autowired
	void setJobExplorer(JobExplorer jobExplorer){
		this.batchJobExplorer = (JobExplorerWasp) jobExplorer;
	}
	
		
	@Autowired
	protected WorkflowService workflowService;
	
	@PostConstruct
	@Override
	protected void initialize() {
		// need to initialize the message channels
		super.initialize();
	}

	 /**
	   * {@inheritDoc}
	   */
	@Override
	public List<Sample> getSubmittedSamples(Job job){
		Assert.assertParameterNotNull(job, "No Job provided");
		Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
		List<Sample> submittedSamplesList = new ArrayList<Sample>();
		if(job != null && job.getJobId().intValue()>0){
			for(JobSample jobSample : job.getJobSample()){
				  Sample sample  = jobSample.getSample();//includes submitted samples that are macromolecules, submitted samples that are libraries, and facility-generated libraries generated from a macromolecule
				  if(sample.getParent() == null){//this sample is NOT a facility-generated library (by contrast, if sample.getParent() != null this indicates a facility-generated library), so add it to the submittedSample list
					  submittedSamplesList.add(sample);
				  }
			  }	
		}
		
		return submittedSamplesList;		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Sample> getSubmittedSamplesNotYetReceived(Job job){
		Assert.assertParameterNotNull(job, "No Job provided");
		Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
		
		List<Sample> submittedSamplesNotYetReceivedList = new ArrayList<Sample>();
		
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put(WaspJobParameters.JOB_ID, job.getJobId().toString());
		List<StepExecution> stepExecutions = batchJobExplorer.getStepExecutions("wasp.sample.step.listenForSampleReceived", parameterMap, false, BatchStatus.STARTED);
		for (StepExecution stepExecution: stepExecutions){
			Integer sampleId = null;
			try{
				sampleId = Integer.valueOf(batchJobExplorer.getJobParameterValueByKey(stepExecution, WaspJobParameters.SAMPLE_ID));
			} catch (ParameterValueRetrievalException e){
				logger.warn(e.getMessage());
				continue;
			}
			Sample sample = sampleDao.getSampleBySampleId(sampleId);
			if (sample == null){
				logger.warn("Sample with sample id '"+sampleId+"' does not have a match in the database!");
				continue;
			}
			submittedSamplesNotYetReceivedList.add(sample);
		}
		return submittedSamplesNotYetReceivedList;
	}
	
	 /**
	   * {@inheritDoc}
	   */
	@Override
	public List<Job> getActiveJobs(){
		
		List<Job> activeJobList = new ArrayList<Job>();
		
		Map<String, String> parameterMap = new HashMap<String, String>();
		// get all job executions from the Batch database which only have one parameter which is job_id but we want all
		// jobIds (so use '*'). Also only get those with a BatchStatus of STARTED. Then get the value of the job ids from the parameter
		parameterMap.put(WaspJobParameters.JOB_ID, "*");
		List<JobExecution> jobExecutions = batchJobExplorer.getJobExecutions(parameterMap, true, BatchStatus.STARTED);
		for (JobExecution jobExecution: jobExecutions){
			try{
				String parameterVal = batchJobExplorer.getJobParameterValueByKey(jobExecution, WaspJobParameters.JOB_ID);
				Job job = jobDao.getJobByJobId(Integer.valueOf(parameterVal));
				if (job.getJobId() == 0){
					logger.warn("Expected a job object with id '"+parameterVal+"' but got none");
				} else {
					activeJobList.add(job);
				}
			} catch(ParameterValueRetrievalException e){
				logger.warn(e.getLocalizedMessage());
			} catch(NumberFormatException e){
				logger.warn(e.getLocalizedMessage());
			}
			
		}
		sortJobsByJobId(activeJobList);
		
		return activeJobList;
	}
	
	 /**
	   * {@inheritDoc}
	   */
	@Override
	public List<Job> getJobsAwaitingReceivingOfSamples(){
		
		List<Job> jobsAwaitingReceivingOfSamples = new ArrayList<Job>();
		
		for (Job job: getActiveJobs()){
			if (! getSubmittedSamplesNotYetReceived(job).isEmpty()) // some samples not yet received
				jobsAwaitingReceivingOfSamples.add(job);
		}
		
		sortJobsByJobId(jobsAwaitingReceivingOfSamples);
		
		return jobsAwaitingReceivingOfSamples;
	}
	
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void sortJobsByJobId(List<Job> jobs){
		  Assert.assertParameterNotNull(jobs, "No Job list provided");
		  class JobIdComparator implements Comparator<Job> {
			    @Override
			    public int compare(Job arg0, Job arg1) {
			        return arg0.getJobId().compareTo(arg1.getJobId());
			    }
		  }
		  Collections.sort(jobs, new JobIdComparator());//sort by job ID 
	  }
	  
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean isJobAwaitingPiApproval(Job job){
			Assert.assertParameterNotNull(job, "No Job provided");
			Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
			Map<String, String> parameterMap = new HashMap<String, String>();
			parameterMap.put(WaspJobParameters.JOB_ID, job.getJobId().toString());
			StepExecution stepExecution = batchJobExplorer.getMostRecentlyStartedStepExecutionInList(
					batchJobExplorer.getStepExecutions("step.piApprove", parameterMap, true)
				);
			if(stepExecution != null && stepExecution.getExitStatus().equals(ExitStatus.EXECUTING))
				return true;
			return false;
		}
	
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean isJobAwaitingDaApproval(Job job){
			Assert.assertParameterNotNull(job, "No Job provided");
			Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
			Map<String, String> parameterMap = new HashMap<String, String>();
			parameterMap.put(WaspJobParameters.JOB_ID, job.getJobId().toString());
			StepExecution stepExecution = batchJobExplorer.getMostRecentlyStartedStepExecutionInList(
					batchJobExplorer.getStepExecutions("step.adminApprove", parameterMap, true)
				);
			if(stepExecution != null && stepExecution.getExitStatus().equals(ExitStatus.EXECUTING))
				return true;
			return false;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean isJobAwaitingQuote(Job job){
			Assert.assertParameterNotNull(job, "No Job provided");
			Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
			Map<String, String> parameterMap = new HashMap<String, String>();
			parameterMap.put(WaspJobParameters.JOB_ID, job.getJobId().toString());
			StepExecution stepExecution = batchJobExplorer.getMostRecentlyStartedStepExecutionInList(
					batchJobExplorer.getStepExecutions("step.quote", parameterMap, true)
				);
			if(stepExecution != null && stepExecution.getExitStatus().equals(ExitStatus.EXECUTING))
				return true;
			return false;
		}
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public Map<String, String> getExtraJobDetails(Job job){
		  Assert.assertParameterNotNull(job, "No Job provided");
		  Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
		  Map<String, String> extraJobDetailsMap = new HashMap<String, String>();

		  List<JobResourcecategory> jobResourceCategoryList = job.getJobResourcecategory();
		  for(JobResourcecategory jrc : jobResourceCategoryList){
			  if(jrc.getResourceCategory().getResourceType().getIName().equals("mps")){
				  extraJobDetailsMap.put("Machine", jrc.getResourceCategory().getName());
				  break;
			  }
		  }
		  for(JobMeta jobMeta : job.getJobMeta()){
			  if(jobMeta.getK().indexOf("readLength") != -1){
				  extraJobDetailsMap.put("Read Length", jobMeta.getV());
			  }
			  if(jobMeta.getK().indexOf("readType") != -1){
				  extraJobDetailsMap.put("Read Type", jobMeta.getV().toUpperCase());
			  }
		  }
		  
		  Map<String, String> parameterMap = new HashMap<String, String>();
		  parameterMap.put(WaspJobParameters.JOB_ID, job.getJobId().toString());
		  // when getting stepExecutions from batch job explorer, get status from the most recently started one
		  // in case job was re-run. This is defensive programming as theoretically this shouldn't happen and there
		  // should only be one entry returned anyway.
		  List<StepExecution> stepExecutions =  batchJobExplorer.getStepExecutions("step.piApprove", parameterMap, true);
		  StepExecution stepExecution = batchJobExplorer.getMostRecentlyStartedStepExecutionInList(stepExecutions);

		  if (stepExecution == null){
			  extraJobDetailsMap.put("PI Approval", "Not Yet Set");
		  }
		  else {
			  ExitStatus adminApprovalStatus = stepExecution.getExitStatus();
			  if(adminApprovalStatus.equals(ExitStatus.EXECUTING)){
				  extraJobDetailsMap.put("PI Approval", "Awaiting Response");
			  }
			  else if(adminApprovalStatus.equals(ExitStatus.COMPLETED)){
				  extraJobDetailsMap.put("PI Approval", "Approved");
			  }
			  else if(adminApprovalStatus.equals(ExitStatus.FAILED)){
				  extraJobDetailsMap.put("PI Approval", "Rejected");
			  }
			  else if(adminApprovalStatus.equals(ExitStatus.STOPPED)){
				  extraJobDetailsMap.put("PI Approval", "Abandoned");
			  }
			  else{
				  extraJobDetailsMap.put("PI Approval", "Unknown");
			  }
		  }
		  
		  
		  stepExecution = batchJobExplorer.getMostRecentlyStartedStepExecutionInList(
				  batchJobExplorer.getStepExecutions("step.adminApprove", parameterMap, true)
				);
		  if (stepExecution == null){
			  extraJobDetailsMap.put("DA Approval", "Not Yet Set");
		  }
		  else {
			  ExitStatus adminApprovalStatus = stepExecution.getExitStatus();
			  if(adminApprovalStatus.equals(ExitStatus.EXECUTING)){
				  extraJobDetailsMap.put("DA Approval", "Awaiting Response");
			  }
			  else if(adminApprovalStatus.equals(ExitStatus.COMPLETED)){
				  extraJobDetailsMap.put("DA Approval", "Approved");
			  }
			  else if(adminApprovalStatus.equals(ExitStatus.FAILED)){
				  extraJobDetailsMap.put("DA Approval", "Rejected");
			  }
			  else if(adminApprovalStatus.equals(ExitStatus.STOPPED)){
				  extraJobDetailsMap.put("DA Approval", "Abandoned");
			  }
			  else{
				  extraJobDetailsMap.put("DA Approval", "Unknown");
			  }
		  }
		  		  
		  stepExecution = batchJobExplorer.getMostRecentlyStartedStepExecutionInList(
				  batchJobExplorer.getStepExecutions("step.quote", parameterMap, true)
				);
		  if (stepExecution == null){
			  extraJobDetailsMap.put("Quote Job Price", "Not Yet Set");
		  }
		  else {
			  ExitStatus adminApprovalStatus = stepExecution.getExitStatus();
			  if(adminApprovalStatus.equals(ExitStatus.EXECUTING)){
				  extraJobDetailsMap.put("Quote Job Price", "Awaiting Response");
			  }
			  else if(adminApprovalStatus.equals(ExitStatus.COMPLETED)){
				  try{
					  Float price = new Float(job.getAcctJobquotecurrent().get(0).getAcctQuote().getAmount());
					  extraJobDetailsMap.put("Quote Job Price", "$"+String.format("%.2f", price));
				  }
				  catch(Exception e){
					  logger.warn("JobServiceImpl::getExtraJobDetails(): " + e);
					  extraJobDetailsMap.put("Quote Job Price", "$?.??"); 
				  }	
			  }
			  else if(adminApprovalStatus.equals(ExitStatus.FAILED)){
				  extraJobDetailsMap.put("Quote Job Price", "Rejected");
			  }
			  else if(adminApprovalStatus.equals(ExitStatus.STOPPED)){
				  extraJobDetailsMap.put("Quote Job Price", "Abandoned");
			  }
			  else{
				  extraJobDetailsMap.put("Quote Job Price", "Unknown");
			  }
		  }
		 

		 
		  
		  return extraJobDetailsMap;	  
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public Job createJobFromJobDraft(JobDraft jobDraft, User user) throws FileMoveException, WaspMessageBuildingException{
		  	Assert.assertParameterNotNull(jobDraft, "No JobDraft provided");
			Assert.assertParameterNotNullNotZero(jobDraft.getJobDraftId(), "Invalid JobDraft Provided");
			Assert.assertParameterNotNull(user, "No User provided");
			Assert.assertParameterNotNullNotZero(user.getUserId(), "Invalid User Provided");
		  	
			// Copies JobDraft to a new Job
			Job job = new Job();
			job.setUserId(user.getUserId());
			job.setLabId(jobDraft.getLabId());
			job.setName(jobDraft.getName());
			job.setWorkflowId(jobDraft.getWorkflowId());
			job.setIsActive(1);
			job.setCreatets(new Date());
			
			job.setViewablebylab(0); // TODO: get from lab? Not being used yet
			
			Job jobDb = jobDao.save(job); 
			
			// Saves the metadata
			for (JobDraftMeta jdm: jobDraft.getJobDraftMeta()) {
				JobMeta jobMeta = new JobMeta();
				jobMeta.setJobId(jobDb.getJobId());
				jobMeta.setK(jdm.getK());
				jobMeta.setV(jdm.getV());
			
				jobMetaDao.save(jobMeta); 
			}
			
			// save the software selected
			for (JobDraftSoftware jdr: jobDraft.getJobDraftSoftware()) {
				JobSoftware jobSoftware = new JobSoftware();
				jobSoftware.setJobId(jobDb.getJobId());
				jobSoftware.setSoftwareId(jdr.getSoftwareId());
			
				jobSoftwareDao.save(jobSoftware); 
			}
			
			// save the resource category selected
			for (JobDraftresourcecategory jdr: jobDraft.getJobDraftresourcecategory()) {
				JobResourcecategory jobResourceCategory = new JobResourcecategory();
				jobResourceCategory.setJobId(jobDb.getJobId());
				jobResourceCategory.setResourcecategoryId(jdr.getResourcecategoryId());
			
				jobResourcecategoryDao.save(jobResourceCategory); 
			}
			
			
			// Creates the JobUser Permission
			JobUser jobUser = new JobUser(); 
			jobUser.setUserId(user.getUserId());
			jobUser.setJobId(jobDb.getJobId());
			Role role = roleDao.getRoleByRoleName("js");
			jobUser.setRoleId(role.getRoleId());
			jobUserDao.save(jobUser);
			
			// added 10-20-11 by rob dubin: with job submission, add lab PI as job viewer ("jv")
			//note: could use similar logic in loop to assign jv to all the lab members
			Lab lab = labDao.getLabByLabId(jobDb.getLabId());		
			// if the pi is different from the job user
			if (jobUser.getUserId().intValue() != lab.getPrimaryUserId().intValue()) {
				JobUser jobUser2 = new JobUser();		
				jobUser2.setUserId(lab.getPrimaryUserId());//the lab PI
				jobUser2.setJobId(jobDb.getJobId());
				Role role2 = roleDao.getRoleByRoleName("jv");
				jobUser2.setRoleId(role2.getRoleId());
				jobUserDao.save(jobUser2);
			}
			
			// Job Cells (oldid, newobj)
			Map<Integer,JobCellSelection> jobDraftCellMap = new HashMap<Integer,JobCellSelection>();
			
			for (JobDraftCellSelection jdc: jobDraft.getJobDraftCellSelection()) {
				JobCellSelection jobCellSelection = new JobCellSelection();
				jobCellSelection.setJobId(jobDb.getJobId());
				jobCellSelection.setCellIndex(jdc.getCellIndex());
			
				JobCellSelection jobCellSelectionDb =	jobCellSelectionDao.save(jobCellSelection);	
			
				jobDraftCellMap.put(jdc.getJobDraftCellSelectionId(), jobCellSelectionDb);
			}
			
			// Create Samples
			for (SampleDraft sd: jobDraft.getSampleDraft()) {
				// existing sample...
				Sample sampleDb;
			
				if (sd.getSourceSampleId() != null) {
					sampleDb = sampleDao.getSampleBySampleId(sd.getSourceSampleId());
				} else { 
			
					Sample sample = new Sample();
					sample.setName(sd.getName()); 
					sample.setSampleTypeId(sd.getSampleTypeId()); 
					sample.setSampleSubtypeId(sd.getSampleSubtypeId()); 
					sample.setSubmitterLabId(jobDb.getLabId()); 
					sample.setSubmitterUserId(user.getUserId()); 
					sample.setSubmitterJobId(jobDb.getJobId()); 
					sample.setIsReceived(0);
					sample.setIsActive(1);
			
					sampleDb = sampleDao.save(sample); 
			
					// sample file
					if (sd.getFileId() != null) {
						SampleFile sampleFile = new SampleFile();
						sampleFile.setSampleId(sampleDb.getSampleId());
						sampleFile.setFileId(sd.getFileId());
			
						sampleFile.setIsActive(1);
			
						// TODO ADD NAME AND INAME
			
						sampleFileDao.save(sampleFile);
					}
			
					// Sample Draft Meta Data
					for (SampleDraftMeta sdm: sd.getSampleDraftMeta()) {
						SampleMeta sampleMeta = new SampleMeta();
			
						sampleMeta.setSampleId(sampleDb.getSampleId());	
						sampleMeta.setK(sdm.getK());	
						sampleMeta.setV(sdm.getV());	
						sampleMeta.setPosition(sdm.getPosition());	
			
						sampleMetaDao.save(sampleMeta); 
					}
				}
			
				// Job Sample
				JobSample jobSample = new JobSample();
				jobSample.setJobId(jobDb.getJobId());
				jobSample.setSampleId(sampleDb.getSampleId());
			
				jobSampleDao.save(jobSample);
			
				for (SampleDraftJobDraftCellSelection sdc: sd.getSampleDraftJobDraftCellSelection()) {
					SampleJobCellSelection sampleJobCellSelection = new SampleJobCellSelection();
					sampleJobCellSelection.setSampleId(sampleDb.getSampleId());
					sampleJobCellSelection.setJobCellSelectionId(jobDraftCellMap.get(sdc.getJobDraftCellSelectionId()).getJobCellSelectionId());
					sampleJobCellSelection.setLibraryIndex(sdc.getLibraryIndex());
					sampleJobCellSelectionDao.save(sampleJobCellSelection);
				}
			}
			
			
			// jobDraftFile -> jobFile
			for(JobDraftFile jdf: jobDraft.getJobDraftFile()){
				File file = jdf.getFile();
				String folderPath = file.getAbsolutePathToFileFolder();
				String absPath = file.getAbsolutePath();
				java.io.File folder = new java.io.File(folderPath);
				String destPath = folderPath.replaceFirst("/jd_"+jobDraft.getJobDraftId()+"$", "/j_"+jobDb.getJobId());
				if (destPath.equals(folderPath)){
					throw new FileMoveException("Cannot convert path from '"+destPath+"'");
				}
				try{
					folder.renameTo(new java.io.File(destPath));
				} catch (Exception e){
					throw new FileMoveException("Cannot rename path '"+folderPath+"' to '"+destPath+"'");
				}
				String newAbsolutePath = absPath.replaceFirst("/jd_"+jobDraft.getJobDraftId(), "/j_"+jobDb.getJobId());
				file.setAbsolutePath(newAbsolutePath);
				JobFile jobFile = new JobFile();
				jobFile.setJob(jobDb);
				jobFile.setFile(file);
				jobFileDao.save(jobFile);
			}
			
			// send message to initiate job processing
			Map<String, String> jobParameters = new HashMap<String, String>();
			jobParameters.put(WaspJobParameters.JOB_ID, jobDb.getJobId().toString());
			String batchJobName = workflowService.getJobFlowBatchJobName(jobDraft.getWorkflow());
			BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( new BatchJobLaunchContext(batchJobName, jobParameters) );
			sendOutboundMessage(batchJobLaunchMessageTemplate.build());
			
			// update the jobdraft
			jobDraft.setStatus("SUBMITTED");
			jobDraft.setSubmittedjobId(jobDb.getJobId());
			jobDraftDao.save(jobDraft); 
						
			return jobDb;
	  }
	  
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Job> getJobsAwaitingLibraryCreation(){
		
		List<Job> JobsAwaitingLibraryCreation = new ArrayList<Job>();
		
		List<StepExecution> stepExecutions = batchJobExplorer.getStepExecutions("wasp.library.step.listenForLibraryCreated", BatchStatus.STARTED);
		Set<Integer> uniqueJobIds = new HashSet<Integer>(); // just to be sure no duplicates, store the job ids in a Set
		for (StepExecution stepExecution: stepExecutions){
			try{
				uniqueJobIds.add( Integer.valueOf(batchJobExplorer.getJobParameterValueByKey(stepExecution, WaspJobParameters.JOB_ID)) );
			} catch (ParameterValueRetrievalException e){
				logger.warn(e.getMessage());
				continue;
			}
		}
		for (Integer jobId: uniqueJobIds){
			Job job = jobDao.getJobByJobId(jobId);
			if (job == null){
				logger.warn("Job with job id '"+jobId+"' does not have a match in the database!");
				continue;
			}
			JobsAwaitingLibraryCreation.add(job);
		}
		return JobsAwaitingLibraryCreation;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Job> getJobsWithLibrariesToGoOnPlatformUnit(ResourceCategory resourceCategory){
		List<Job> jobsFilteredByResourceCategory = new ArrayList<Job>();
		for (Job currentJob: getJobsWithLibrariesToGoOnPlatformUnit()){
			JobResourcecategory jrc = jobResourcecategoryDao.getJobResourcecategoryByResourcecategoryIdJobId(resourceCategory.getResourceCategoryId(), currentJob.getJobId());
			if(jrc!=null && jrc.getJobResourcecategoryId()!=null && jrc.getJobResourcecategoryId().intValue() != 0)
				jobsFilteredByResourceCategory.add(currentJob);
		}
		return jobsFilteredByResourceCategory;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Job> getJobsWithLibrariesToGoOnPlatformUnit(){
		List<Job> jobsWithLibrariesToGoOnFlowCell = new ArrayList<Job>();
		for (Job job: getActiveJobs()){
			Map<Integer, Integer> librariesForJobWithAnalysisFlow = new HashMap<Integer, Integer>();
			Map<String, String> parameterMap = new HashMap<String, String>();
			parameterMap.put(WaspJobParameters.JOB_ID, job.getJobId().toString());
			// get all 'wasp.analysis.step.waitForData' StepExecutions for current job
			// the job may have many libraries and each library may need to be run more than once
			for (StepExecution stepExecution: batchJobExplorer.getStepExecutions("wasp.analysis.step.waitForData", parameterMap, false) ){
				try{
					Integer libraryId =  Integer.valueOf(batchJobExplorer.getJobParameterValueByKey(stepExecution, WaspJobParameters.LIBRARY_ID));
					// put to list of all sample id's on all analysis flows (librariesInAnalysisFlow)
					if (librariesForJobWithAnalysisFlow.containsKey(libraryId)){
						librariesForJobWithAnalysisFlow.put(libraryId, librariesForJobWithAnalysisFlow.get(libraryId) + 1);
					} else {
						librariesForJobWithAnalysisFlow.put(libraryId, 1);
					}
				} catch (ParameterValueRetrievalException e){
					logger.warn(e.getMessage());
					continue;
				} catch (NumberFormatException e){
					logger.warn(e.getMessage());
					continue;
				}
				
				for (Integer libraryId: librariesForJobWithAnalysisFlow.keySet()){
					Sample library = sampleDao.getSampleBySampleId(libraryId);
					if (library.getSampleId() == 0){
						logger.warn("Cannot find Sample with id=" + libraryId);
						continue;
					}
					List<SampleSource> sampleSources = library.getSampleSource(); // library -> cell relationships
					Integer numberOfLibraryInstancesOnCells = 0;
					if (sampleSources != null)
						numberOfLibraryInstancesOnCells = sampleSources.size();
					
					Integer numberOfAnalysisFlowsForLibrary = librariesForJobWithAnalysisFlow.get(libraryId);
					
					if (numberOfAnalysisFlowsForLibrary > numberOfLibraryInstancesOnCells)
						jobsWithLibrariesToGoOnFlowCell.add(job);
					
				}
			}
		}
		return jobsWithLibrariesToGoOnFlowCell;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Job> getJobsSubmittedOrViewableByUser(User user){
		Assert.assertParameterNotNull(user, "No User provided");
		Assert.assertParameterNotNullNotZero(user.getUserId(), "Invalid User Provided");
		
		List<Job> jobList = new ArrayList<Job>();
		List<JobUser> jobUserList = new ArrayList<JobUser>();
		
		Map m = new HashMap();
		m.put("UserId", user.getUserId().intValue());
		List<String> orderByColumnNames = new ArrayList<String>();
		orderByColumnNames.add("jobId");
		
		jobUserList = this.jobUserDao.findByMapDistinctOrderBy(m, null, orderByColumnNames, "desc");//default order is by jobId/desc
		
		for(JobUser jobUser : jobUserList){
			jobList.add(jobUser.getJob());
		}		
		
		return jobList;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean isJobAwaitingLibraryCreation(Job job, Sample sample){
		Assert.assertParameterNotNull(sample, "No Sample provided");
		Integer sampleId = sample.getSampleId();
		Assert.assertParameterNotNullNotZero(sampleId, "Invalid Sample Provided");
		Assert.assertParameterNotNull(job, "No Job provided");
		Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
		boolean sampleIsInJob = false;
		for (Sample s: job.getSample()){
			if (s.getSampleId().equals(sampleId)){
				sampleIsInJob = true;
				break;
			}
		}
		if (!sampleIsInJob){
			logger.warn("supplied sample is not associated with supplied job");
			return false;
		}
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put(WaspJobParameters.SAMPLE_ID, sampleId.toString());
		parameterMap.put(WaspJobParameters.JOB_ID, job.getJobId().toString());
		StepExecution stepExecution = batchJobExplorer.getMostRecentlyStartedStepExecutionInList(
				batchJobExplorer.getStepExecutions("wasp.library.step.listenForLibraryCreated", parameterMap, true, BatchStatus.STARTED)
			);
		if (stepExecution != null)
			return true;
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateJobQuoteStatus(Job job, WaspStatus status) throws WaspMessageBuildingException{
		updateJobStatus(job, status, WaspJobTask.QUOTE);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateJobDaApprovalStatus(Job job, WaspStatus status) throws WaspMessageBuildingException{
		updateJobStatus(job, status, WaspJobTask.ADMIN_APPROVE);
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateJobPiApprovalStatus(Job job, WaspStatus status) throws WaspMessageBuildingException{
		updateJobStatus(job, status, WaspJobTask.QUOTE);
	}
	
	private void updateJobStatus(Job job, WaspStatus status, String task) throws WaspMessageBuildingException{
		// TODO: Write test!!
		Assert.assertParameterNotNull(job, "No Job provided");
		Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
		Assert.assertParameterNotNull(status, "No Status provided");
		if (status != WaspStatus.CREATED && status != WaspStatus.ABANDONED)
			throw new InvalidParameterException("WaspStatus is null, or not CREATED or ABANDONED");
		
		Assert.assertParameterNotNull(task, "No Task provided");
		  
		JobStatusMessageTemplate messageTemplate = new JobStatusMessageTemplate(job.getJobId());
		messageTemplate.setTask(task);
		messageTemplate.setStatus(status); // sample received (CREATED) or abandoned (ABANDONED)
		sendOutboundMessage(messageTemplate.build());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addJobViewer(Integer jobId, String newViewerEmailAddress) throws Exception{
		
		  if(jobId == null || newViewerEmailAddress == null){
		  	  throw new Exception("listJobSamples.illegalOperation.label");	
		  }		
		  //System.out.println("at 7");	  		
		  Job job = jobDao.getJobByJobId(jobId.intValue());
		  if(job.getJobId()==null || job.getJobId().intValue() <= 0){
			throw new Exception("listJobSamples.jobNotFound.label");			  
		  }
		  
		  User userPerformingThisAction = authenticationService.getAuthenticatedUser();
		  if(userPerformingThisAction.getUserId()==null || userPerformingThisAction.getUserId().intValue()<=0){
			  throw new Exception("listJobSamples.illegalOperation.label");
		  }
		  
		  Boolean userPerformingThisActionIsPermittedToAddJobViewers = false;
		  //does the webviewer have the authority to perform this function?
		  if(authenticationService.isSuperUser() //webviewer (person performing the action) is superuser so OK
				  ||  userPerformingThisAction.getUserId().intValue() == job.getUserId().intValue() //webViewer is the job submitter, so OK
				  || userPerformingThisAction.getUserId().intValue() == job.getLab().getPrimaryUserId().intValue()//webViewer is the job PI, so OK
			)
		  {
			  userPerformingThisActionIsPermittedToAddJobViewers = true; //superuser, job's submitter, job's PI
		  }
		  if(!userPerformingThisActionIsPermittedToAddJobViewers){
			  throw new Exception("listJobSamples.illegalOperation.label");			  
		  }
		  
		  if(newViewerEmailAddress==null || "".equals(newViewerEmailAddress.trim()) || ! StringHelper.isStringAValidEmailAddress(newViewerEmailAddress) ){
			  throw new Exception("listJobSamples.invalidFormatEmailAddress.label");
		  }
		  User newViewerToBeAddedToJob = userDao.getUserByEmail(newViewerEmailAddress.trim());
		  if(newViewerToBeAddedToJob.getUserId()==null || newViewerToBeAddedToJob.getUserId().intValue()<= 0){
			  throw new Exception("listJobSamples.userNotFoundByEmailAddress.label");	
		  }
		  JobUser jobUser = jobUserDao.getJobUserByJobIdUserId(jobId.intValue(), newViewerToBeAddedToJob.getUserId().intValue());
		  if(jobUser.getJobUserId()!=null && jobUser.getJobUserId().intValue() > 0){//viewer to be added is already a viewer for this job.
			  throw new Exception("listJobSamples.alreadyIsViewerOfThisJob.label");
		  }
		  Role role = roleDao.getRoleByRoleName("jv");
		  if(role.getRoleId()==null || role.getRoleId().intValue()<=0){
			  throw new Exception("listJobSamples.roleNotFound.label");
		  }
		  JobUser newJobUser = new JobUser();
		  newJobUser.setJob(job);
		  newJobUser.setUser(newViewerToBeAddedToJob);
		  newJobUser.setLastUpdUser(userPerformingThisAction.getUserId());
		  newJobUser.setRole(role);
		  jobUserDao.save(newJobUser);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeJobViewer(Integer jobId, Integer userId)throws Exception{
		
		  if(jobId == null || userId == null){
			  throw new Exception("listJobSamples.illegalOperation.label");	
		  }
		  
		  Job job = jobDao.getJobByJobId(jobId.intValue());
		  if(job.getJobId()==null || job.getJobId().intValue() <= 0 ){
			  throw new Exception("listJobSamples.jobNotFound.label");			  
		  }
		  User userToBeRemoved = userDao.getUserByUserId(userId.intValue());
		  if(userToBeRemoved.getUserId()==null || userToBeRemoved.getUserId().intValue() <= 0 ){//userToBeRemoved not found in the user table; odd.
			  throw new Exception("listJobSamples.userNotFound.label");			  
		  }

		  
		  User userPerformingThisAction = authenticationService.getAuthenticatedUser();
		  if(userPerformingThisAction.getUserId()==null || userPerformingThisAction.getUserId().intValue()<=0){
			  throw new Exception("listJobSamples.illegalOperation.label");
		  }
		  
		  Boolean userPerformingThisActionIsPermittedToRemoveJobViewers = false;
		  //does the webviewer have the authority to perform this function?
		  if(authenticationService.isSuperUser() //webviewer (person performing the action) is superuser so OK
				  ||  userPerformingThisAction.getUserId().intValue() == job.getUserId().intValue() //webViewer is the job submitter, so OK
				  || userPerformingThisAction.getUserId().intValue() == job.getLab().getPrimaryUserId().intValue()//webViewer is the job PI, so OK
				  || userPerformingThisAction.getUserId().intValue() == userToBeRemoved.getUserId().intValue()//webViewer is attempting to remove him/her self from list, which is allowed (so long as the webviewer is neither the job submitter or the job's PI).
			)
		  {
			  userPerformingThisActionIsPermittedToRemoveJobViewers = true; //superuser, job's submitter, job's PI
		  }
		  if(!userPerformingThisActionIsPermittedToRemoveJobViewers){
			  throw new Exception("listJobSamples.illegalOperation.label");			  
		  }
		  
		  //we checked that webviewer is authorized to do this. Now make certain that the webviewer is not trying to remove the job submitter or job PI. 
		  if(userToBeRemoved.getUserId().intValue() == job.getUserId().intValue()){//trying to remove job's submitter as viewer; not allowed
			  throw new Exception("listJobSamples.submitterRemovalIllegal.label");			  
		  }
		  if(userToBeRemoved.getUserId().intValue() == job.getLab().getPrimaryUserId().intValue()){//trying to remove job pi as viewer; not allowed
			  throw new Exception("listJobSamples.piRemovalIllegal.label");			  
		  }
		  
		  JobUser jobUser = jobUserDao.getJobUserByJobIdUserId(job.getJobId().intValue(), userToBeRemoved.getUserId().intValue());
		  if(jobUser.getJobUserId().intValue() <= 0){//jobuser not found for this job and this user in the jobuser table.
			  throw new Exception("listJobSamples.userNotViewerOfThisJob.label");
		  }
		  else{
			  jobUserDao.remove(jobUser);
			  jobUserDao.flush(jobUser);
		  }		
	}
}
