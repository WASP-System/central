package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.easymock.classextension.EasyMock.*;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


import edu.yu.einstein.wasp.batch.core.extension.JobExplorerWasp;
import edu.yu.einstein.wasp.dao.JobCellSelectionDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.dao.JobMetaDao;
import edu.yu.einstein.wasp.dao.JobResourcecategoryDao;
import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.dao.JobSoftwareDao;
import edu.yu.einstein.wasp.dao.JobUserDao;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleDraftMetaDao;
import edu.yu.einstein.wasp.dao.SampleJobCellSelectionDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.TaskMappingDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.dao.impl.JobCellSelectionDaoImpl;
import edu.yu.einstein.wasp.dao.impl.JobDaoImpl;
import edu.yu.einstein.wasp.dao.impl.JobDraftDaoImpl;
import edu.yu.einstein.wasp.dao.impl.JobMetaDaoImpl;
import edu.yu.einstein.wasp.dao.impl.JobResourcecategoryDaoImpl;
import edu.yu.einstein.wasp.dao.impl.JobSampleDaoImpl;
import edu.yu.einstein.wasp.dao.impl.JobSoftwareDaoImpl;
import edu.yu.einstein.wasp.dao.impl.JobUserDaoImpl;
import edu.yu.einstein.wasp.dao.impl.LabDaoImpl;
import edu.yu.einstein.wasp.dao.impl.RoleDaoImpl;
import edu.yu.einstein.wasp.dao.impl.SampleDaoImpl;
import edu.yu.einstein.wasp.dao.impl.SampleJobCellSelectionDaoImpl;
import edu.yu.einstein.wasp.dao.impl.SampleMetaDaoImpl;
import edu.yu.einstein.wasp.dao.impl.TaskMappingDaoImpl;
import edu.yu.einstein.wasp.dao.impl.WorkflowDaoImpl;
import edu.yu.einstein.wasp.exception.FileMoveException;
import edu.yu.einstein.wasp.exception.InvalidParameterException;
import edu.yu.einstein.wasp.exception.ParameterValueRetrievalException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.model.AcctJobquotecurrent;
import edu.yu.einstein.wasp.model.AcctQuote;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobCellSelection;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftCellSelection;
import edu.yu.einstein.wasp.model.JobDraftFile;
import edu.yu.einstein.wasp.model.JobDraftMeta;
import edu.yu.einstein.wasp.model.JobDraftSoftware;
import edu.yu.einstein.wasp.model.JobDraftresourcecategory;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.JobSoftware;
import edu.yu.einstein.wasp.model.JobUser;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftJobDraftCellSelection;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SampleJobCellSelection;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;


public class TestJobServiceImpl extends EasyMockSupport{

  TaskMappingDao mockTaskMappingDao;
  JobSampleDao mockJobSampleDao;
  SampleDao mockSampleDao;
  JobDao mockJobDao;
  JobMetaDao mockJobMetaDao;
  JobSoftwareDao mockJobSoftwareDao;
  JobResourcecategoryDao mockJobResourcecategoryDao;
  RoleDao mockRoleDao;
  JobUserDao mockJobUserDao;
  LabDao mockLabDao;
  JobCellSelectionDao mockJobCellSelectionDao;
  SampleJobCellSelectionDao mockSampleJobCellSelectionDao;
  JobDraftDao mockJobDraftDao;
  SampleMetaDao mockSampleMetaDao;

  WorkflowDao mockWorkflowDao;

  JobExplorerWasp mockJobExplorerWasp;
  JobExplorer mockJobExplorer;
  
  JobService mockJobServiceImpl;
  SampleService mockSampleService;
	
  JobServiceImpl jobServiceImpl = new JobServiceImpl();
  TaskServiceImpl taskServiceImpl = new TaskServiceImpl();
 
  //Test when sample.getParent() is NOT null (a facility-generated library)
  @Test
  public void getSubmittedSamples() {
	  
	  List<JobSample> jobSamples = new ArrayList<JobSample>();
	 
	  Job job = new Job();
	  job.setJobId(1);
	  Sample sample = new Sample();
	  sample.setSampleId(1);
	  Sample sampleParent  = new Sample();
	  sample.setParent(sampleParent);
	  sample.setParentId(2);
	    
	  JobSample jobSample = new JobSample();
	  jobSample.setJobSampleId(0001);
	  jobSample.setJobId(1);
	  jobSample.setSampleId(1);
	  jobSample.setSample(sample);
	  jobSamples.add(jobSample);
	  job.setJobSample(jobSamples);
	  
	  //expected
	  List<Sample> submittedSamplesList = new ArrayList<Sample>();
	  jobServiceImpl.setJobSampleDao(mockJobSampleDao);
	  jobServiceImpl.setSampleDao(mockSampleDao);
	  expect(mockJobSampleDao.getJobSampleByJobId(job.getJobId())).andReturn(jobSamples);
	  expect(mockSampleDao.getSampleBySampleId(jobSample.getSampleId())).andReturn(sample);
	    
	  replay(mockJobSampleDao);
	  replay(mockSampleDao);
	  
	  Assert.assertEquals(submittedSamplesList, jobServiceImpl.getSubmittedSamples(job));
	  
	  verify(mockJobSampleDao);
	  verify(mockSampleDao);
	  
  }
  
   //Test when sample.getParent() is null (not a facility-generated library)
   @Test
  public void getSubmittedSamples2() {
		  List<JobSample> jobSamples = new ArrayList<JobSample>();
			 
		  Job job = new Job();
		  job.setJobId(1);
		  Sample sample = new Sample();
		  sample.setSampleId(1);
		    
		  JobSample jobSample = new JobSample();
		  jobSample.setJobSampleId(0001);
		  jobSample.setJobId(1);
		  jobSample.setSampleId(1);
		  jobSample.setSample(sample);
		  jobSamples.add(jobSample);
		  job.setJobSample(jobSamples);
		  
		  //expected
		  List<Sample> submittedSamplesList = new ArrayList<Sample>();
		  submittedSamplesList.add(sample);
		  
		  jobServiceImpl.setJobSampleDao(mockJobSampleDao);
		  jobServiceImpl.setSampleDao(mockSampleDao);
		  
		  expect(mockJobSampleDao.getJobSampleByJobId(job.getJobId())).andReturn(jobSamples);
		  expect(mockSampleDao.getSampleBySampleId(jobSample.getSampleId())).andReturn(sample);
		    
		  replay(mockJobSampleDao);
		  replay(mockSampleDao);
		  
		  Assert.assertEquals(submittedSamplesList, jobServiceImpl.getSubmittedSamples(job));
		  
		  verify(mockJobSampleDao);
		  verify(mockSampleDao);
	  
  }

  //Test when job is null
  @Test
  public void getSubmittedSamples3() {

	  //expected
	  try {
		  jobServiceImpl.getSubmittedSamples(null);
	  }
	  catch (RuntimeException e) {
		  Assert.assertEquals(e.getMessage(), "No Job provided");
	  }
	  
	  
  }
  
  //Test when bad job is passed
  @Test
  public void getSubmittedSamples4() {

	  Job job = new Job();	  
	  //expected
	  try {
		  jobServiceImpl.getSubmittedSamples(job);
	  }
	  catch (RuntimeException e) {
		  Assert.assertEquals(e.getMessage(), "Invalid Job Provided");
	  }
  }
  
  @Test
  public void getSubmittedSamplesNotYetReceived() {

	  Job job = new Job();
	  job.setJobId(1);
	  
	  Job job2 = new Job();
	  
	  Sample sample = new Sample();
	  sample.setSampleId(123);
	  
	  //Map<String, String> parameterMap = new HashMap<String, String>();
	  //parameterMap.put(WaspJobParameters.JOB_ID, job.getJobId().toString());
	  
	  Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
	  Set<String> jobIdStringSet = new HashSet<String>();
	  jobIdStringSet.add(job.getJobId().toString());
	  parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
	  
	  StepExecution stepExecution;
	  JobExecution jobExecution;
	  JobInstance jobInstance;
	  JobParameters jobParameters;
	  //JobParameter jobParameter;	  
	  //jobParameter = new JobParameter("Param1");
	  jobParameters = new JobParameters();
	  
	  jobInstance = new JobInstance(new Long(12345), jobParameters, "Job Name1");
	  jobExecution = new JobExecution(jobInstance, new Long(12345));
	  stepExecution = new StepExecution("Step Name1", jobExecution, new Long(12345));
	  
	  List<StepExecution> stepExecutions = new ArrayList<StepExecution>();
	  stepExecutions.add(stepExecution);
	  
	  //stepExecution.setId(new Long(123));
	  expect(mockJobExplorerWasp.getStepExecutions("wasp.sample.step.listenForSampleReceived", parameterMap, false, BatchStatus.STARTED)).andReturn(stepExecutions);
	  expect(mockJobExplorerWasp.getStepExecutions("wasp.library.step.listenForLibraryReceived", parameterMap, false, BatchStatus.STARTED)).andReturn(new ArrayList<StepExecution>());
	  
	  try {
		expect(mockJobExplorerWasp.getJobParameterValueByKey(stepExecution, WaspJobParameters.SAMPLE_ID)).andReturn("123");
	  } catch (ParameterValueRetrievalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	  }
	  jobServiceImpl.setSampleDao(mockSampleDao);
	  expect(mockSampleDao.getSampleBySampleId(123)).andReturn(sample);
	  
	  replay(mockJobExplorerWasp);
	  replay(mockSampleDao);
	  
	  //expected to return
	  List<Sample> submittedSamplesNotYetReceivedList = new ArrayList<Sample>();
	  submittedSamplesNotYetReceivedList.add(sample);
	  
	  //Test case: 1: Job is not null
	  Assert.assertEquals(jobServiceImpl.getSubmittedSamplesNotYetReceived(job), submittedSamplesNotYetReceivedList);
	  
	  //Test case: 2: Job is null
	  try {
		  Assert.assertEquals(jobServiceImpl.getSubmittedSamplesNotYetReceived(null), submittedSamplesNotYetReceivedList);
	  }
	  catch (RuntimeException e) {
		  Assert.assertEquals(e.getMessage(), "No Job provided");
	  }
	  
	  //Test case: 3: Job is invalid (jobId is not set or null)
	  try {
		  Assert.assertEquals(jobServiceImpl.getSubmittedSamplesNotYetReceived(job2), submittedSamplesNotYetReceivedList);
	  }
	  catch (RuntimeException e) {
		  Assert.assertEquals(e.getMessage(), "Invalid Job Provided");
	  }
	  verify(mockJobExplorerWasp);
	  verify(mockSampleDao);

  }
  
  
  @Test //when Sample is null
  public void getSubmittedSamplesNotYetReceived2() {

	  Job job = new Job();
	  job.setJobId(1);
	  
	  Job job2 = new Job();
	  
	  Sample sample = new Sample();
	  sample.setSampleId(123);
	  
	  Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
	  Set<String> jobIdStringSet = new HashSet<String>();
	  jobIdStringSet.add(job.getJobId().toString());
	  parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
	  
	  StepExecution stepExecution;
	  JobExecution jobExecution;
	  JobInstance jobInstance;
	  JobParameters jobParameters;
	  //JobParameter jobParameter;	  
	  //jobParameter = new JobParameter("Param1");
	  jobParameters = new JobParameters();
	  
	  jobInstance = new JobInstance(new Long(12345), jobParameters, "Job Name1");
	  jobExecution = new JobExecution(jobInstance, new Long(12345));
	  stepExecution = new StepExecution("Step Name1", jobExecution, new Long(12345));
	  
	  List<StepExecution> stepExecutions = new ArrayList<StepExecution>();
	  stepExecutions.add(stepExecution);
	  
	  //stepExecution.setId(new Long(123));
	  expect(mockJobExplorerWasp.getStepExecutions("wasp.sample.step.listenForSampleReceived", parameterMap, false, BatchStatus.STARTED)).andReturn(stepExecutions);
	  expect(mockJobExplorerWasp.getStepExecutions("wasp.library.step.listenForLibraryReceived", parameterMap, false, BatchStatus.STARTED)).andReturn(new ArrayList<StepExecution>());
	  
	  try {
		expect(mockJobExplorerWasp.getJobParameterValueByKey(stepExecution, WaspJobParameters.SAMPLE_ID)).andReturn("123");
	  } catch (ParameterValueRetrievalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	  }
	  jobServiceImpl.setSampleDao(mockSampleDao);
	  expect(mockSampleDao.getSampleBySampleId(123)).andReturn(null);
	  
	  replay(mockJobExplorerWasp);
	  replay(mockSampleDao);
	  
	  //expected to return
	  List<Sample> submittedSamplesNotYetReceivedList = new ArrayList<Sample>();
	  
	  //Test case: 1: Sample is null
	  Assert.assertEquals(jobServiceImpl.getSubmittedSamplesNotYetReceived(job), submittedSamplesNotYetReceivedList);
  
	  verify(mockJobExplorerWasp);
	  verify(mockSampleDao);

  }
 
  @Test
  public void getSubmittedSamplesNotYetReceived3() {

	  Job job = new Job();
	  job.setJobId(1);
	  
	  Job job2 = new Job();
	  
	  Sample sample = new Sample();
	  sample.setSampleId(123);
	  
	  Sample sample2= new Sample();
	  sample2.setSampleId(456);
	  
	  Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
	  Set<String> jobIdStringSet = new HashSet<String>();
	  jobIdStringSet.add(job.getJobId().toString());
	  parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
	  
	  StepExecution stepExecution;
	  JobExecution jobExecution;
	  JobInstance jobInstance;
	  JobParameters jobParameters;
	  //JobParameter jobParameter;	  
	  //jobParameter = new JobParameter("Param1");
	  jobParameters = new JobParameters();
	  
	  jobInstance = new JobInstance(new Long(12345), jobParameters, "Job Name1");
	  jobExecution = new JobExecution(jobInstance, new Long(12345));
	  stepExecution = new StepExecution("Step Name1", jobExecution, new Long(12345));
	  
	  List<StepExecution> stepExecutions = new ArrayList<StepExecution>();
	  stepExecutions.add(stepExecution);
	  
	  JobParameters jobParameters2 = new JobParameters();
	  JobInstance jobInstance2 = new JobInstance(new Long(12345), jobParameters2, "Job Name1");
	  JobExecution jobExecution2= new JobExecution(jobInstance2, new Long(12345));
	  StepExecution stepExecution2= new StepExecution("Step Name1", jobExecution2, new Long(12345));
	  stepExecutions.add(stepExecution2);

	  jobServiceImpl.setJobExplorer(mockJobExplorerWasp);

	  expect(mockJobExplorerWasp.getStepExecutions("wasp.sample.step.listenForSampleReceived", parameterMap, false, BatchStatus.STARTED)).andReturn(stepExecutions);
	  expect(mockJobExplorerWasp.getStepExecutions("wasp.library.step.listenForLibraryReceived", parameterMap, false, BatchStatus.STARTED)).andReturn(new ArrayList<StepExecution>());
	  
	  try {
		expect(mockJobExplorerWasp.getJobParameterValueByKey(stepExecution, WaspJobParameters.SAMPLE_ID)).andReturn("123");

	  } catch (ParameterValueRetrievalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	  }
	  jobServiceImpl.setSampleDao(mockSampleDao);
	  expect(mockSampleDao.getSampleBySampleId(123)).andReturn(sample);
	  try {
			expect(mockJobExplorerWasp.getJobParameterValueByKey(stepExecution2, WaspJobParameters.SAMPLE_ID)).andReturn("456");

	  } catch (ParameterValueRetrievalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	  }
	  expect(mockSampleDao.getSampleBySampleId(456)).andReturn(sample2);
	  
	  replay(mockJobExplorerWasp);
	  replay(mockSampleDao);
	  
	  //expected to return
	  List<Sample> submittedSamplesNotYetReceivedList = new ArrayList<Sample>();
	  submittedSamplesNotYetReceivedList.add(sample);
	  submittedSamplesNotYetReceivedList.add(sample2);
	  
	  //Test case: 1: 
	  Assert.assertEquals(jobServiceImpl.getSubmittedSamplesNotYetReceived(job), submittedSamplesNotYetReceivedList);
	  
	  verify(mockJobExplorerWasp);
	  verify(mockSampleDao);

  }
  
  @Test
  public void getActiveJobs() {	  
	  List<Job> activeJobList = new ArrayList<Job>();
	  Job job = new Job();
	  job.setJobId(1);
	  
	  Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
	  Set<String> jobIdStringSet = new HashSet<String>();
	  jobIdStringSet.add("*");
	  parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);

	  JobParameters jobParameters = new JobParameters();
	  JobInstance jobInstance = new JobInstance(new Long(12345), jobParameters, "Job Name1");
	  JobExecution jobExecution = new JobExecution(jobInstance, new Long(12345));
	  
	  JobParameters jobParameters2 = new JobParameters();
	  JobInstance jobInstance2 = new JobInstance(new Long(12345), jobParameters2, "Job Name2");
	  JobExecution jobExecution2 = new JobExecution(jobInstance2, new Long(2345));
	  
	  List<JobExecution> jobExecutions = new ArrayList<JobExecution>();
	  jobExecutions.add(jobExecution);
	  jobExecutions.add(jobExecution2);
	  	  
	  expect(mockJobExplorerWasp.getJobExecutions(parameterMap, true, BatchStatus.STARTED)).andReturn(jobExecutions);
	    
	  try {
		expect(mockJobExplorerWasp.getJobParameterValueByKey(jobExecution, WaspJobParameters.JOB_ID)).andReturn("123");

	  } catch (ParameterValueRetrievalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	  }
	  jobServiceImpl.setJobDao(mockJobDao);
	  expect(mockJobDao.getJobByJobId(Integer.valueOf("123"))).andReturn(job);
	  
	  replay(mockJobExplorerWasp);
	  replay(mockJobDao);
	  activeJobList.add(job);
	  //Test case: 1: 
	  Assert.assertEquals(jobServiceImpl.getActiveJobs(), activeJobList);
	  
	  verify(mockJobExplorerWasp);
	  verify(mockJobDao);

  }
  
  @Test 
  public void isJobAwaitingPiApproval() {
		
	  	Job job = new Job();
	  	job.setJobId(1);
	  
	  	Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(job.getJobId().toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
	  	
		StepExecution stepExecution;
		JobExecution jobExecution;
	    JobInstance jobInstance;
		JobParameters jobParameters;
		jobParameters = new JobParameters();
		jobInstance = new JobInstance(new Long(12345), jobParameters, "Job Name1");
		jobExecution = new JobExecution(jobInstance, new Long(12345));
		stepExecution = new StepExecution("Step Name1", jobExecution, new Long(12345));
		stepExecution.setExitStatus(ExitStatus.EXECUTING);
		stepExecution.setStatus(BatchStatus.STARTED);
		
		List<StepExecution> stepExecutions = new ArrayList<StepExecution>();
		stepExecutions.add(stepExecution);
		
		expect(mockJobExplorerWasp.getStepExecutions("step.piApprove", parameterMap, true, BatchStatus.STARTED)).andReturn(stepExecutions);
		
		replay(mockJobExplorerWasp);
		
		Assert.assertTrue(jobServiceImpl.isJobAwaitingPiApproval(job));
		
	    verify(mockJobExplorerWasp);
	  
  }
  
  @Test 
  public void isJobAwaitingDaApproval() {
		
	  	Job job = new Job();
	  	job.setJobId(1);
	  
	  	Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(job.getJobId().toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
	  	
		StepExecution stepExecution;
		JobExecution jobExecution;
	    JobInstance jobInstance;
		JobParameters jobParameters;
		jobParameters = new JobParameters();
		jobInstance = new JobInstance(new Long(12345), jobParameters, "Job Name1");
		jobExecution = new JobExecution(jobInstance, new Long(12345));
		stepExecution = new StepExecution("Step Name1", jobExecution, new Long(12345));
		stepExecution.setExitStatus(ExitStatus.EXECUTING);
		stepExecution.setStatus(BatchStatus.STARTED);
		
		List<StepExecution> stepExecutions = new ArrayList<StepExecution>();
		stepExecutions.add(stepExecution);
			
		expect(mockJobExplorerWasp.getStepExecutions("step.adminApprove", parameterMap, true, BatchStatus.STARTED)).andReturn(stepExecutions);
		
		replay(mockJobExplorerWasp);
		
		Assert.assertTrue(jobServiceImpl.isJobAwaitingDaApproval(job));
		
	    verify(mockJobExplorerWasp);
	  
  }
  
  @Test (description="")
  public void isJobAwaitingQuote(){
	  Job job = new Job();
	  job.setJobId(1);
	  
	  Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
	  Set<String> jobIdStringSet = new HashSet<String>();
	  jobIdStringSet.add(job.getJobId().toString());
	  parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
	  
	  JobParameters jobParameters = new JobParameters();
	  JobInstance jobInstance = new JobInstance(new Long(12345), jobParameters, "Job Name1");
	  JobExecution jobExecution = new JobExecution(jobInstance, new Long(12345));
	  StepExecution stepExecution = new StepExecution("Step Name1", jobExecution, new Long(12345));
      stepExecution.setExitStatus(ExitStatus.EXECUTING);
      stepExecution.setStatus(BatchStatus.STARTED);
			
	  List<StepExecution> stepExecutions = new ArrayList<StepExecution>();
	  stepExecutions.add(stepExecution);
	  
	  List<StepExecution> stepExecutions2 = new ArrayList<StepExecution>();
	  
	  //Test case 1: Returns TRUE if stepExecution != null and ExitStatus.EXECUTING
	  expect(mockJobExplorerWasp.getStepExecutions("step.quote", parameterMap, true, BatchStatus.STARTED)).andReturn(stepExecutions);
	  
	  //Test case 2: Returns FALSE if stepExecution != null and ExitStatus != EXECUTING
	  expect(mockJobExplorerWasp.getStepExecutions("step.quote", parameterMap, true, BatchStatus.STARTED)).andReturn(stepExecutions2);
	  
	  replay(mockJobExplorerWasp);
	
	  Assert.assertTrue(jobServiceImpl.isJobAwaitingQuote(job));
	  Assert.assertFalse(jobServiceImpl.isJobAwaitingQuote(job));

		
	  verify(mockJobExplorerWasp);
	  
  }

  @Test (description="test when machine, readLength and quote are set")
  public void getExtraJobDetails() {
	  
	  Job job = new Job();
	  job.setJobId(1);  
	  
	  //Machine (resource category)
	  JobResourcecategory jrc = new JobResourcecategory();
	  jrc.setJobResourcecategoryId(1);
	  ResourceCategory resourceCategory = new ResourceCategory();
	  ResourceType resourceType = new ResourceType();
	  resourceType.setResourceTypeId(1);
	  resourceType.setIName("mps");
	  resourceCategory.setName("Illumina HiSeq 2000");
	  resourceCategory.setResourceType(resourceType);
	  jrc.setResourceCategory(resourceCategory);
	    
	  List<JobResourcecategory> jobResourceCategoryList = new ArrayList<JobResourcecategory>();
	  jobResourceCategoryList.add(jrc);
	  job.setJobResourcecategory(jobResourceCategoryList);
	  
	  //Job meta
	  JobMeta jobMeta = new JobMeta();
	  jobMeta.setK("readLength");
	  jobMeta.setV("1");
	  List<JobMeta> jobMetaList = new ArrayList<JobMeta>();
	  jobMetaList.add(jobMeta);
	  job.setJobMeta(jobMetaList);
	  
	  //Quote
	  AcctQuote acctQuote = new AcctQuote();
	  acctQuote.setAmount(new Float(123.45));
	  
	  AcctJobquotecurrent acctJobQuoteCurrent = new AcctJobquotecurrent();
	  acctJobQuoteCurrent.setAcctQuote(acctQuote);
	  List <AcctJobquotecurrent> acctJobQuoteCurrentList = new ArrayList <AcctJobquotecurrent>();
	  acctJobQuoteCurrentList.add(0, acctJobQuoteCurrent);
	  job.setAcctJobquotecurrent(acctJobQuoteCurrentList);

	  
	  LinkedHashMap<String, String> extraJobDetailsMap = new LinkedHashMap<String, String>();	 
	  extraJobDetailsMap.put("extraJobDetails.machine.label", jrc.getResourceCategory().getName());
	  extraJobDetailsMap.put("extraJobDetails.readLength.label", jobMeta.getV());
	  extraJobDetailsMap.put("extraJobDetails.quote.label", Currency.getInstance(Locale.getDefault()).getSymbol()+String.format("%.2f", acctQuote.getAmount()));
	  
	  Assert.assertEquals(jobServiceImpl.getExtraJobDetails(job), extraJobDetailsMap);

  }

  @Test (description="test different quote amounts")
  public void getExtraJobDetails2() {
	  
	  Job job = new Job();
	  job.setJobId(1);  
	  
	  //Machine (resource category)
	  JobResourcecategory jrc = new JobResourcecategory();
	  jrc.setJobResourcecategoryId(1);
	  ResourceCategory resourceCategory = new ResourceCategory();
	  ResourceType resourceType = new ResourceType();
	  resourceType.setResourceTypeId(1);
	  resourceType.setIName("mps");
	  resourceCategory.setName("Illumina HiSeq 2000");
	  resourceCategory.setResourceType(resourceType);
	  jrc.setResourceCategory(resourceCategory);
	    
	  List<JobResourcecategory> jobResourceCategoryList = new ArrayList<JobResourcecategory>();
	  jobResourceCategoryList.add(jrc);
	  job.setJobResourcecategory(jobResourceCategoryList);
	  
	  //Job meta
	  JobMeta jobMeta = new JobMeta();
	  jobMeta.setK("readLength");
	  jobMeta.setV("1");
	  List<JobMeta> jobMetaList = new ArrayList<JobMeta>();
	  jobMetaList.add(jobMeta);
	  job.setJobMeta(jobMetaList);
	  	  
	  AcctQuote acctQuote = new AcctQuote();
	  AcctJobquotecurrent acctJobQuoteCurrent = new AcctJobquotecurrent();
	  List <AcctJobquotecurrent> acctJobQuoteCurrentList = new ArrayList <AcctJobquotecurrent>();

	  //Test case 1  
	  LinkedHashMap<String, String> extraJobDetailsMap = new LinkedHashMap<String, String>();
	  Float price = new Float(123.45);
	  extraJobDetailsMap.put("extraJobDetails.machine.label", jrc.getResourceCategory().getName());
	  extraJobDetailsMap.put("extraJobDetails.readLength.label", jobMeta.getV());
	  extraJobDetailsMap.put("extraJobDetails.quote.label", Currency.getInstance(Locale.getDefault()).getSymbol()+String.format("%.2f", price));
	  
	  acctQuote.setAmount(price);	  
	  acctJobQuoteCurrent.setAcctQuote(acctQuote);
	  acctJobQuoteCurrentList.add(0, acctJobQuoteCurrent);
	  job.setAcctJobquotecurrent(acctJobQuoteCurrentList);
	  
	  Assert.assertEquals(jobServiceImpl.getExtraJobDetails(job), extraJobDetailsMap);
	  
	  //Test case 2
	  extraJobDetailsMap.clear();
	  price = new Float(0);
	  extraJobDetailsMap.put("extraJobDetails.machine.label", jrc.getResourceCategory().getName());
	  extraJobDetailsMap.put("extraJobDetails.readLength.label", jobMeta.getV());
	  extraJobDetailsMap.put("extraJobDetails.quote.label", Currency.getInstance(Locale.getDefault()).getSymbol()+String.format("%.2f", price));
	  
	  acctQuote.setAmount(price);	  
	  acctJobQuoteCurrent.setAcctQuote(acctQuote);
	  acctJobQuoteCurrentList.add(0, acctJobQuoteCurrent);
	  job.setAcctJobquotecurrent(acctJobQuoteCurrentList);
	  
	  Assert.assertEquals(jobServiceImpl.getExtraJobDetails(job), extraJobDetailsMap);
	  
	  //Test case 3
	  extraJobDetailsMap.clear();
	  price = new Float(123);
	  extraJobDetailsMap.put("extraJobDetails.machine.label", jrc.getResourceCategory().getName());
	  extraJobDetailsMap.put("extraJobDetails.readLength.label", jobMeta.getV());
	  extraJobDetailsMap.put("extraJobDetails.quote.label", Currency.getInstance(Locale.getDefault()).getSymbol()+String.format("%.2f", price));
	  
	  acctQuote.setAmount(price);	  
	  acctJobQuoteCurrent.setAcctQuote(acctQuote);
	  acctJobQuoteCurrentList.add(0, acctJobQuoteCurrent);
	  job.setAcctJobquotecurrent(acctJobQuoteCurrentList);
	  
	  Assert.assertEquals(jobServiceImpl.getExtraJobDetails(job), extraJobDetailsMap);
	  
	  //Test case 4
	  extraJobDetailsMap.clear();
	  price = new Float(123);
	  extraJobDetailsMap.put("extraJobDetails.machine.label", jrc.getResourceCategory().getName());
	  extraJobDetailsMap.put("extraJobDetails.readLength.label", jobMeta.getV());
	  extraJobDetailsMap.put("extraJobDetails.quote.label", Currency.getInstance(Locale.getDefault()).getSymbol()+String.format("%.2f", price));
	  
	  acctQuote.setAmount(price);	  
	  acctJobQuoteCurrent.setAcctQuote(acctQuote);
	  acctJobQuoteCurrentList.add(0, acctJobQuoteCurrent);
	  job.setAcctJobquotecurrent(acctJobQuoteCurrentList);
	  	  
	  Assert.assertEquals(jobServiceImpl.getExtraJobDetails(job), extraJobDetailsMap);
	  
	  //Test case 5
	  extraJobDetailsMap.clear();
	  price = new Float(9999999999.9999999);
	  extraJobDetailsMap.put("extraJobDetails.machine.label", jrc.getResourceCategory().getName());
	  extraJobDetailsMap.put("extraJobDetails.readLength.label", jobMeta.getV());
	  extraJobDetailsMap.put("extraJobDetails.quote.label", Currency.getInstance(Locale.getDefault()).getSymbol()+String.format("%.2f", price));
	  
	  acctQuote.setAmount(price);	  
	  acctJobQuoteCurrent.setAcctQuote(acctQuote);
	  acctJobQuoteCurrentList.add(0, acctJobQuoteCurrent);
	  job.setAcctJobquotecurrent(acctJobQuoteCurrentList);
	  	  
	  Assert.assertEquals(jobServiceImpl.getExtraJobDetails(job), extraJobDetailsMap);


  }
  
   
  //Test different quote amounts
  @Test
  public void getExtraJobDetails5() {

	  try {
	   jobServiceImpl.getExtraJobDetails(null);
	  }
	  catch (InvalidParameterException e){
		  Assert.assertEquals(e.getMessage(), "No Job provided");
		  
	  }
	  
	  try {
	   Job job = new Job();

	   jobServiceImpl.getExtraJobDetails(job);
	  }
	  catch (InvalidParameterException e){
		  Assert.assertEquals(e.getMessage(), "Invalid Job Provided");
		  
	  }
  }
  
  // TODO

/*
  @Test (description="") 
  public void createJobFromJobDraft () {
	  User user = new User();
	  user.setUserId(123);
	  JobDraft jobDraft = new JobDraft();
	  jobDraft.setJobDraftId(1);
	  jobDraft.setLabId(123);
	  jobDraft.setName("my job");
	  jobDraft.setWorkflowId(1);
	  
	  // Copies JobDraft to a new Job
	  Job job = new Job();
	  job.setJobId(1);
	  job.setUserId(user.getUserId());
	  job.setLabId(jobDraft.getLabId());
	  job.setName(jobDraft.getName());
	  job.setWorkflowId(jobDraft.getWorkflowId());
	  job.setIsActive(1);
	  job.setCreatets(new Date());
	  job.setViewablebylab(0); // TODO: get from lab? Not being used yet

	  JobDraftMeta jobDraftMeta = new JobDraftMeta();
	  jobDraftMeta.setK("chipSeq.samplePairsTvsC");
	  jobDraftMeta.setV("1:2;2:1;");
	  List <JobDraftMeta> jobDraftMetaList = new ArrayList <JobDraftMeta>();
	  jobDraftMetaList.add(jobDraftMeta);
	  jobDraft.setJobDraftMeta(jobDraftMetaList);
	  
	  JobMeta jobMeta = new JobMeta();
	  jobMeta.setJobId(job.getJobId());
	  jobMeta.setK(jobDraftMeta.getK());
	  jobMeta.setV(jobDraftMeta.getV());
	  
	  JobDraftSoftware jobDraftSoftware = new JobDraftSoftware();
	  jobDraftSoftware.setSoftwareId(new Integer(123));
	  List <JobDraftSoftware> jobDraftSoftwareList = new ArrayList <JobDraftSoftware>();
	  jobDraftSoftwareList.add(jobDraftSoftware);
	  jobDraft.setJobDraftSoftware(jobDraftSoftwareList);
	  
	  JobSoftware jobSoftware = new JobSoftware();
	  jobSoftware.setJobId(job.getJobId());
	  jobSoftware.setSoftwareId(jobDraftSoftware.getSoftwareId());
	  
	  JobDraftresourcecategory jobDraftresourcecategory= new JobDraftresourcecategory();
	  jobDraftresourcecategory.setResourcecategoryId(new Integer(123));
	  List <JobDraftresourcecategory> jobDraftresourcecategoryList = new ArrayList <JobDraftresourcecategory>();
	  jobDraftresourcecategoryList.add(jobDraftresourcecategory);
	  jobDraft.setJobDraftresourcecategory(jobDraftresourcecategoryList);
	  
	  JobResourcecategory jobResourceCategory = new JobResourcecategory();
	  jobResourceCategory.setJobId(job.getJobId());
	  jobResourceCategory.setResourcecategoryId(jobDraftresourcecategory.getResourcecategoryId());
	  
	  Role role = new Role();
	  role.setRoleId(new Integer(1));
	  
	  JobUser jobUser = new JobUser(); 
	  jobUser.setUserId(user.getUserId());
	  jobUser.setJobId(job.getJobId());
	  	  	  
	  Lab lab = new Lab();
	  lab.setLabId(new Integer(1));
	  
	  // TODO: write test for when pi is different from job user
	  // if the pi is the same as the job user
	  lab.setPrimaryUserId(new Integer(job.getUserId()));
	  
	  // Job Cells (oldid, newobj)
	  JobDraftCellSelection jdc = new JobDraftCellSelection();
	  jdc.setCellIndex(new Integer(1));
	  jdc.setJobDraftCellSelectionId(1);
	  List <JobDraftCellSelection> jdcList = new ArrayList <JobDraftCellSelection>();
	  jdcList.add(jdc);
	  jobDraft.setJobDraftCellSelection(jdcList);
	  
	  JobCellSelection jobCellSelection = new JobCellSelection();
	  jobCellSelection.setJobId(job.getJobId());
	  jobCellSelection.setCellIndex(jdc.getCellIndex());	
	  jobCellSelection.setJobCellSelectionId(1);
	  
	  SampleDraftJobDraftCellSelection sdjdc = new SampleDraftJobDraftCellSelection();
	  sdjdc.setJobDraftCellSelectionId(1);
	  sdjdc.setLibraryIndex(1);
	  
	  List <SampleDraftJobDraftCellSelection> sdjdcList = new ArrayList <SampleDraftJobDraftCellSelection>();
	  sdjdcList.add(sdjdc);
	  SampleDraft sd = new SampleDraft();
	  sd.setName("sample draft");
	  sd.setSampleTypeId(1);
	  sd.setSampleSubtypeId(1);
	  sd.setSampleDraftJobDraftCellSelection(sdjdcList);
	  sd.setSampleDraftId(new Integer(1));
	  
	  //Sample Draft Meta
	  SampleDraftMeta  sdm = new SampleDraftMeta();
	  sdm.setK("genericBiomolecule.species");
	  sdm.setV("Human");
	  sdm.setPosition(1);
	  
	  SampleMeta sampleMeta = new SampleMeta();
	  sampleMeta.setK(sdm.getK());
	  sampleMeta.setV(sdm.getV());
	  sampleMeta.setPosition(sdm.getPosition());
	  
	  
	  
	  // TODO: write test for when SourceSampleId is null
	  //Test when SourceSampleId is not null
	  sd.setSourceSampleId(new Integer(1));
	  List <SampleDraft> sdList = new ArrayList <SampleDraft>();
	  sdList.add(sd);
	  jobDraft.setSampleDraft(sdList);

	  //Create Samples
	  Sample sample = new Sample();
	  sample.setName(sd.getName()); 
	  sample.setSampleTypeId(sd.getSampleTypeId()); 
	  sample.setSampleSubtypeId(sd.getSampleSubtypeId()); 
	  sample.setSubmitterLabId(job.getLabId()); 
	  sample.setSubmitterUserId(user.getUserId()); 
	  sample.setSubmitterJobId(job.getJobId()); 
	  sample.setIsReceived(0);
	  sample.setIsActive(1);
	  
	  // TODO: Sample File: sd.getFileId() != null
	  
	  // Job Sample
	  SampleJobCellSelection sampleJobCellSelection = new SampleJobCellSelection();

	  JobSample jobSample = new JobSample();
	  jobSample.setJobId(job.getJobId());
	  jobSample.setSampleId(sample.getSampleId());
	  
	  jobDraft.setStatus("SUBMITTED");
	  jobDraft.setSubmittedjobId(job.getJobId());
	  	  
	  expect(mockJobDao.save(EasyMock.isA(Job.class))).andReturn(job); 
	  replay(mockJobDao);

	  expect(mockJobMetaDao.save(EasyMock.isA(JobMeta.class))).andReturn(jobMeta);
	  replay(mockJobMetaDao);
	  
	  expect(mockJobSoftwareDao.save(EasyMock.isA(JobSoftware.class))).andReturn(jobSoftware);
	  replay(mockJobSoftwareDao);
	  
	  expect(mockJobResourcecategoryDao.save(EasyMock.isA(JobResourcecategory.class))).andReturn(jobResourceCategory);
	  replay(mockJobResourcecategoryDao);
	  
	  expect(mockRoleDao.getRoleByRoleName("js")).andReturn(role);
	  replay(mockRoleDao);
	  
	  expect(mockJobUserDao.save(EasyMock.isA(JobUser.class))).andReturn(jobUser);
	  replay(mockJobUserDao);
	  
	  expect(mockLabDao.getLabByLabId(job.getLabId())).andReturn(lab);
	  replay(mockLabDao);
	  
	  expect(mockJobCellSelectionDao.save(EasyMock.isA(JobCellSelection.class))).andReturn(jobCellSelection);
	  replay(mockJobCellSelectionDao);
	  	  
	  expect(mockSampleDao.save(sample)).andReturn(sample);
	  replay(mockSampleDao);
	  
	  expect(mockSampleMetaDao.save(sampleMeta)).andReturn(sampleMeta);
	  replay(mockSampleMetaDao);
	  
	  
	  expect(mockJobSampleDao.save(EasyMock.isA(JobSample.class))).andReturn(jobSample);
	  replay(mockJobSampleDao);
	  
	  expect(mockSampleJobCellSelectionDao.save(EasyMock.isA(SampleJobCellSelection.class))).andReturn(sampleJobCellSelection);
	  replay(mockSampleJobCellSelectionDao); 
	  
	  expect(mockJobDraftDao.save(jobDraft)).andReturn(jobDraft);
	  replay(mockJobDraftDao); 

      try {
		Assert.assertEquals(jobServiceImpl.createJobFromJobDraft(jobDraft, user), job);
	  } catch (FileMoveException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	  }
  
	  verify(mockJobDao);
	  verify(mockJobMetaDao);
	  verify(mockJobSoftwareDao);
	  verify(mockJobResourcecategoryDao);
	  verify(mockRoleDao);
	  verify(mockJobUserDao);
	  verify(mockLabDao);
	  verify(mockJobCellSelectionDao);
	  verify(mockJobDraftDao);
  }
*/
  
  @Test
  public void initiateBatchJobForJobSubmission() {
	  try {
		   jobServiceImpl.initiateBatchJobForJobSubmission(null);
		  }
		  catch (InvalidParameterException e){
			  Assert.assertEquals(e.getMessage(), "No Job provided");
			  
		  }
		  
		  try {
		   Job job = new Job();

		   jobServiceImpl.initiateBatchJobForJobSubmission(job);
		  }
		  catch (InvalidParameterException e){
			  Assert.assertEquals(e.getMessage(), "Invalid Job Provided");
			  
		  }
	  
	  
	  
  }
 /*
  @Test
  public void initiateBatchJobForJobSubmission2() {
	  Job job = new Job();
	  job.setJobId(1);
	  job.setWorkflowId(1);
	  
	  Workflow workflow = new Workflow();
	  
	  Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
	  //Set<String> jobIdStringSet = new HashSet<String>();
	  //jobIdStringSet.add(job.getJobId().toString());
	  expect(mockWorkflowDao.getWorkflowByWorkflowId(job.getWorkflowId())).andReturn(workflow);
	  replay(mockWorkflowDao); 
	  
  }
  
 */ 
  
  @Test
  public void getJobsAwaitingLibraryCreation(){
	  
	  List<Job> JobsAwaitingLibraryCreation = new ArrayList<Job>();
	  Job job = new Job();
	  job.setJobId(1);
	  
	  Sample sample = new Sample();
	  List<Sample> samples = new ArrayList<Sample>();
	  samples.add(sample);
	  job.setSample(samples);
	  
	  List<Job> activeJobs = new ArrayList<Job>();
	  activeJobs.add(job);
	
	  expect(mockJobServiceImpl.getActiveJobs()).andReturn(activeJobs);
      replay(mockJobServiceImpl);
      
      expect(mockSampleService.isSampleAwaitingLibraryCreation(sample)).andReturn(true);
      replay(mockSampleService);
		
      JobsAwaitingLibraryCreation.add(job);
      Assert.assertEquals(mockJobServiceImpl.getJobsAwaitingLibraryCreation(), JobsAwaitingLibraryCreation);
      
      verify(mockSampleService);
      verify(mockJobServiceImpl);
	  
	  
  }
  
  @Test
  public void getJobsAwaitingLibraryCreation2(){
	  
	  List<Job> JobsAwaitingLibraryCreation = new ArrayList<Job>();
	  
	  StepExecution stepExecution;
	  JobExecution jobExecution;
	  JobInstance jobInstance;
	  JobParameters jobParameters;
	  jobParameters = new JobParameters();
	  
	  jobInstance = new JobInstance(new Long(12345), jobParameters, "Job Name1");
	  jobExecution = new JobExecution(jobInstance, new Long(12345));
	  stepExecution = new StepExecution("Step Name1", jobExecution, new Long(12345));
	  
	  List<StepExecution> stepExecutions = new ArrayList<StepExecution>();
	  stepExecutions.add(stepExecution);

	  jobServiceImpl.setJobExplorer(mockJobExplorerWasp);
	  
	  expect(mockJobExplorerWasp.getStepExecutions("wasp.library.step.listenForLibraryCreated", BatchStatus.STARTED)).andReturn(stepExecutions);

	  try {
			expect(mockJobExplorerWasp.getJobParameterValueByKey(stepExecution, WaspJobParameters.JOB_ID)).andReturn("123");
		  } catch (ParameterValueRetrievalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		  }
	  expect(mockJobDao.getJobByJobId(123)).andReturn(null);
	  
	  replay(mockJobExplorerWasp);
	  replay(mockJobDao);
	  Assert.assertEquals(jobServiceImpl.getJobsAwaitingLibraryCreation(), JobsAwaitingLibraryCreation);
	  
	  verify(mockJobExplorerWasp);
	  verify(mockJobDao);

	  
  }

  
  @Test (description="tests when sampleSources = NULL")
  public void getJobsWithLibrariesToGoOnPlatformUnit() {
	  
		List<Job> jobsWithLibrariesToGoOnFlowCell = new ArrayList<Job>();
		
		Job job = new Job();
		job.setJobId(1);
		List<Job> activeJobs = new ArrayList<Job>();
		activeJobs.add(job);
		
		expect(mockJobServiceImpl.getActiveJobs()).andReturn(activeJobs);
	    replay(mockJobServiceImpl);	    
	    
	    Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(job.getJobId().toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		
		JobParameters jobParameters = new JobParameters();

	    JobInstance jobInstance = new JobInstance(new Long(12345), jobParameters, "Job Name1");
		JobExecution jobExecution = new JobExecution(jobInstance, new Long(12345));
	    StepExecution stepExecution = new StepExecution("Step Name1", jobExecution, new Long(12345));
				
	    List<StepExecution> stepExecutions = new ArrayList<StepExecution>();
		stepExecutions.add(stepExecution);
		
		((JobServiceImpl) mockJobServiceImpl).setJobExplorer(mockJobExplorerWasp);

		expect(mockJobExplorerWasp.getStepExecutions("wasp.analysis.step.waitForData", parameterMap, false)).andReturn(stepExecutions);
		String libraryId="1";
		try {
			expect(mockJobExplorerWasp.getJobParameterValueByKey(stepExecution, WaspJobParameters.LIBRARY_ID)).andReturn(libraryId);
		} catch (ParameterValueRetrievalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Sample library = new Sample();
		library.setSampleId(1);
		
		((JobServiceImpl) mockJobServiceImpl).setSampleDao(mockSampleDao);
		expect(mockSampleDao.getSampleBySampleId(Integer.valueOf(libraryId))).andReturn(library);
		
	    replay(mockJobExplorerWasp);
	    replay(mockSampleDao);
	    
	    jobsWithLibrariesToGoOnFlowCell.add(job);
		Assert.assertEquals(mockJobServiceImpl.getJobsWithLibrariesToGoOnPlatformUnit(), jobsWithLibrariesToGoOnFlowCell);
		
		verify(mockJobServiceImpl);
		verify(mockJobExplorerWasp);
		verify(mockSampleDao);
  
	  
  }
  
  @Test (description="tests when sampleSources != NULL and numberOfAnalysisFlowsForLibrary < numberOfLibraryInstancesOnCells")
  public void getJobsWithLibrariesToGoOnPlatformUnit2() {
	  
		List<Job> jobsWithLibrariesToGoOnFlowCell = new ArrayList<Job>();
		
		Job job = new Job();
		job.setJobId(1);
		List<Job> activeJobs = new ArrayList<Job>();
		activeJobs.add(job);
		
		expect(mockJobServiceImpl.getActiveJobs()).andReturn(activeJobs);
	    replay(mockJobServiceImpl);	    
	    
	    Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(job.getJobId().toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		
		JobParameters jobParameters = new JobParameters();

	    JobInstance jobInstance = new JobInstance(new Long(12345), jobParameters, "Job Name1");
		JobExecution jobExecution = new JobExecution(jobInstance, new Long(12345));
	    StepExecution stepExecution = new StepExecution("Step Name1", jobExecution, new Long(12345));
				
	    List<StepExecution> stepExecutions = new ArrayList<StepExecution>();
		stepExecutions.add(stepExecution);
		
		((JobServiceImpl) mockJobServiceImpl).setJobExplorer(mockJobExplorerWasp);

		expect(mockJobExplorerWasp.getStepExecutions("wasp.analysis.step.waitForData", parameterMap, false)).andReturn(stepExecutions);
		String libraryId="1";
		try {
			expect(mockJobExplorerWasp.getJobParameterValueByKey(stepExecution, WaspJobParameters.LIBRARY_ID)).andReturn(libraryId);
		} catch (ParameterValueRetrievalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Sample library = new Sample();
		library.setSampleId(1);
		SampleSource sampleSource = new SampleSource();
		sampleSource.setSourceSampleId(1);
		SampleSource sampleSource2 = new SampleSource();
		sampleSource.setSourceSampleId(2);

		List<SampleSource> sampleSources = new ArrayList<SampleSource>(); // library -> cell relationships
		sampleSources.add(sampleSource);
		sampleSources.add(sampleSource2);

		library.setSampleSource(sampleSources);
		
		((JobServiceImpl) mockJobServiceImpl).setSampleDao(mockSampleDao);
		expect(mockSampleDao.getSampleBySampleId(Integer.valueOf(libraryId))).andReturn(library);
		
	    replay(mockJobExplorerWasp);
	    replay(mockSampleDao);
	    
		Assert.assertEquals(mockJobServiceImpl.getJobsWithLibrariesToGoOnPlatformUnit(), jobsWithLibrariesToGoOnFlowCell);
	    
		verify(mockJobServiceImpl);
		verify(mockJobExplorerWasp);
		verify(mockSampleDao);

  }
 
  @Test (description="tests when more than one job and sampleSources != NULL and numberOfAnalysisFlowsForLibrary > numberOfLibraryInstancesOnCells")
  public void getJobsWithLibrariesToGoOnPlatformUnit3() {
	  
		List<Job> jobsWithLibrariesToGoOnFlowCell = new ArrayList<Job>();
		
		Job job = new Job();
		job.setJobId(1);
		Job job2 = new Job();
		job2.setJobId(2);
		List<Job> activeJobs = new ArrayList<Job>();
		activeJobs.add(job);
		activeJobs.add(job2);

		expect(mockJobServiceImpl.getActiveJobs()).andReturn(activeJobs);
	    replay(mockJobServiceImpl);	    
	    
	    //Test parameters 1
	    Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(job.getJobId().toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		JobParameters jobParameters = new JobParameters();
		
	    JobInstance jobInstance = new JobInstance(new Long(12345), jobParameters, "Job Name1");
		JobExecution jobExecution = new JobExecution(jobInstance, new Long(12345));
	    StepExecution stepExecution = new StepExecution("Step Name1", jobExecution, new Long(12345));
				
	    List<StepExecution> stepExecutions = new ArrayList<StepExecution>();
		stepExecutions.add(stepExecution);

		//Test parameters 2
		Map<String, Set<String>> parameterMap2 = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet2 = new HashSet<String>();
		jobIdStringSet2.add(job2.getJobId().toString());
		parameterMap2.put(WaspJobParameters.JOB_ID, jobIdStringSet2);
		JobParameters jobParameters2 = new JobParameters();
		
		JobInstance jobInstance2 = new JobInstance(new Long(12345), jobParameters, "Job2");
		JobExecution jobExecution2 = new JobExecution(jobInstance, new Long(12345));
	    StepExecution stepExecution2 = new StepExecution("Step2", jobExecution, new Long(12345));
				
	    List<StepExecution> stepExecutions2 = new ArrayList<StepExecution>();
		stepExecutions2.add(stepExecution2);
		
		
		((JobServiceImpl) mockJobServiceImpl).setJobExplorer(mockJobExplorerWasp);

		//job1
		expect(mockJobExplorerWasp.getStepExecutions("wasp.analysis.step.waitForData", parameterMap, false)).andReturn(stepExecutions);
		String libraryId="1";
		try {
			expect(mockJobExplorerWasp.getJobParameterValueByKey(stepExecution, WaspJobParameters.LIBRARY_ID)).andReturn(libraryId);
		} catch (ParameterValueRetrievalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Sample library = new Sample();
		library.setSampleId(1);

		((JobServiceImpl) mockJobServiceImpl).setSampleDao(mockSampleDao);
		expect(mockSampleDao.getSampleBySampleId(Integer.valueOf(libraryId))).andReturn(library);
		
		//job2
		expect(mockJobExplorerWasp.getStepExecutions("wasp.analysis.step.waitForData", parameterMap2, false)).andReturn(stepExecutions2);
		libraryId="2";
		try {
			expect(mockJobExplorerWasp.getJobParameterValueByKey(stepExecution2, WaspJobParameters.LIBRARY_ID)).andReturn(libraryId);
		} catch (ParameterValueRetrievalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		library = new Sample();
		library.setSampleId(2);

		((JobServiceImpl) mockJobServiceImpl).setSampleDao(mockSampleDao);
		expect(mockSampleDao.getSampleBySampleId(Integer.valueOf(libraryId))).andReturn(library);
		
	    replay(mockJobExplorerWasp);
	    replay(mockSampleDao);
	    
	    jobsWithLibrariesToGoOnFlowCell.add(job);
	    jobsWithLibrariesToGoOnFlowCell.add(job2);
	    
		Assert.assertEquals(mockJobServiceImpl.getJobsWithLibrariesToGoOnPlatformUnit(), jobsWithLibrariesToGoOnFlowCell);
	    
		verify(mockJobServiceImpl);
		verify(mockJobExplorerWasp);
		verify(mockSampleDao);

  }
  

  /* Method removed from JobServiceImpl.java
  //Test when state is set to null
  @Test
  public void getActiveJobs_StateIsNull() {
	  
	  List<State> states = null;
	  
	  taskServiceImpl.setTaskDao(mockTaskDao);
	  jobServiceImpl.setTaskService(taskServiceImpl);
	  expect(mockTaskDao.getStatesByTaskIName("Start Job", "CREATED")).andReturn(states);
	  replay(mockTaskDao);
	  
	  //expected to return empty activeJobList if states==null
	  List<Job> activeJobList = new ArrayList<Job>();
	  Assert.assertEquals(activeJobList, jobServiceImpl.getActiveJobs());
	  verify(mockTaskDao);
	  
  }

  
  @Test
  public void getJobsAwaitingSubmittedSamples() {
	  
	  List<State> states = new ArrayList<State>();
	  List<Statesample> stateSamples = new ArrayList<Statesample>();
	  List<JobSample> jobSamples = new ArrayList<JobSample>();

	  State state = new State();
	  Sample sample = new Sample();
	  Job job = new Job();
	  JobSample jobSample = new JobSample();
	  jobSample.setJob(job);
	  jobSamples.add(jobSample);
	  sample.setJobSample(jobSamples);
	  Statesample stateSample = new Statesample();
	  stateSample.setSample(sample);
	  stateSamples.add(stateSample);
	  state.setStatesample(stateSamples);
	  states.add(state);
	  
	  taskServiceImpl.setTaskDao(mockTaskDao);
	  jobServiceImpl.setTaskService(taskServiceImpl);
	  expect(mockTaskDao.getStatesByTaskIName("Receive Sample", "CREATED")).andReturn(states);
	  replay(mockTaskDao);
	  
	  //
	  List<Job> jobsAwaitingSubmittedSamplesList = new ArrayList<Job>();
	  jobsAwaitingSubmittedSamplesList.add(job);
	  Assert.assertEquals(jobsAwaitingSubmittedSamplesList, jobServiceImpl.getJobsAwaitingSubmittedSamples());
	  verify(mockTaskDao);
	  
  }
  
  */
  
  /*	REMOVE TEMPORARILY UNTIL FIXED UP FOR NEW WAY OF TASK HANDLING (A S MCLELLAN)
  @Test
  public void getJobsAwaitingSubmittedSamples_StateIsNull() {
	  List<State> states = null;
	  
	  taskServiceImpl.setTaskDao(mockTaskDao);
	  jobServiceImpl.setTaskService(taskServiceImpl);
	  expect(mockTaskDao.getStatesByTaskIName("Start Job", "CREATED")).andReturn(states);
	  replay(mockTaskDao);
	  
	  //expected to return empty jobsAwaitingSubmittedSamplesList if states==null
	  List<Job> jobsAwaitingSubmittedSamplesList = new ArrayList<Job>();
	  Assert.assertEquals(jobsAwaitingSubmittedSamplesList, jobServiceImpl.getActiveJobs());
	  verify(mockTaskDao);
	  
  }
  
  @Test
  public void getExtraJobDetails() {

	  JobMeta jobMeta = new JobMeta();
	  jobMeta.setK("readLength");
	  jobMeta.setV("");
	  List <JobMeta> jobMetas = new ArrayList<JobMeta>();
	  jobMetas.add(jobMeta);
	  List<JobResourcecategory> jobResourceCategories = new ArrayList <JobResourcecategory>();
	  JobResourcecategory jobResourceCategory = new JobResourcecategory();
	  ResourceCategory resourceCategory = new ResourceCategory();
	  ResourceType resourceType = new ResourceType();
	  resourceType.setIName("mps");
	  resourceType.setResourceTypeId(1);
	  resourceCategory.setResourceType(resourceType);
	  resourceCategory.setName("Illumina HiSeq 2000");
	  jobResourceCategory.setResourceCategory(resourceCategory);
	  jobResourceCategories.add(jobResourceCategory);
	  
	  //Test case:1
	  Task task = new Task();
	  task.setIName("DA Approval");
	  //Test case:2
	  Task task2 = new Task();
	  task2.setIName("PI Approval");
	  //Test case:3
	  Task task3 = new Task();
	  task3.setIName("Quote Job");
	  
	  //Test case: 1a: status="CREATED"
	  Job job = new Job();
	  job.setJobMeta(jobMetas);
	  job.setJobResourcecategory(jobResourceCategories);
	  Statejob stateJob = new Statejob();
	  List<Statejob> statejobs = new ArrayList<Statejob>();
	  State state = new State();
	  state.setTask(task);
	  state.setStatus("CREATED");
	  stateJob.setState(state);
	  statejobs.add(stateJob);
	  job.setStatejob(statejobs);
	  
	  //Test case: 1b status="COMPLETED"
	  Job job2 = new Job();
	  job2.setJobMeta(jobMetas);
	  job2.setJobResourcecategory(jobResourceCategories);
	  Statejob stateJob2 = new Statejob();
	  List<Statejob> statejobs2 = new ArrayList<Statejob>();
	  State state2 = new State();
	  state2.setTask(task);
	  state2.setStatus("COMPLETED");
	  stateJob2.setState(state2);
	  statejobs2.add(stateJob2);
	  job2.setStatejob(statejobs2);
	  
	  //Test case: 1c: status="ABANDONED"
	  Job job3 = new Job();
	  job3.setJobMeta(jobMetas);
	  job3.setJobResourcecategory(jobResourceCategories);
	  Statejob stateJob3 = new Statejob();
	  List<Statejob> statejobs3 = new ArrayList<Statejob>();
	  State state3 = new State();
	  state3.setTask(task);
	  state3.setStatus("ABANDONED");
	  stateJob3.setState(state3);
	  statejobs3.add(stateJob3);
	  job3.setStatejob(statejobs3);
	  
	  //Test case: 1:  If task="DA Approval"
	  //Test case: 1a: status="CREATED"
	  Map<String, String> extraJobDetailsMap = new HashMap<String, String>();
	  extraJobDetailsMap.put("Machine", jobResourceCategory.getResourceCategory().getName());
	  extraJobDetailsMap.put("Read Length", jobMeta.getV());
	  extraJobDetailsMap.put("DA Approval", "Awaiting Response");
	  extraJobDetailsMap.put("PI Approval", "Not Yet Set");
	  extraJobDetailsMap.put("Quote Job Price", "Not Yet Set");
	  
	  //Test case: 1b: status="COMPLETED" OR  status="FINALIZED"
	  Map<String, String> extraJobDetailsMap2 = new HashMap<String, String>();
	  extraJobDetailsMap2.put("Machine", jobResourceCategory.getResourceCategory().getName());
	  extraJobDetailsMap2.put("Read Length", jobMeta.getV());
	  extraJobDetailsMap2.put("DA Approval", "Approved");
	  extraJobDetailsMap2.put("PI Approval", "Not Yet Set");
	  extraJobDetailsMap2.put("Quote Job Price", "Not Yet Set");
	  
	  //Test case: 1c: status="ABANDONED"
	  Map<String, String> extraJobDetailsMap3 = new HashMap<String, String>();
	  extraJobDetailsMap3.put("Machine", jobResourceCategory.getResourceCategory().getName());
	  extraJobDetailsMap3.put("Read Length", jobMeta.getV());
	  extraJobDetailsMap3.put("DA Approval", "Rejected");
	  extraJobDetailsMap3.put("PI Approval", "Not Yet Set");
	  extraJobDetailsMap3.put("Quote Job Price", "Not Yet Set");

	  Assert.assertEquals(extraJobDetailsMap, jobServiceImpl.getExtraJobDetails(job));
	  Assert.assertEquals(extraJobDetailsMap2, jobServiceImpl.getExtraJobDetails(job2));
	  Assert.assertEquals(extraJobDetailsMap3, jobServiceImpl.getExtraJobDetails(job3));

	  
  }
  
  @Test
  public void getExtraJobDetails2() {

	  JobMeta jobMeta = new JobMeta();
	  jobMeta.setK("readLength");
	  jobMeta.setV("");
	  List <JobMeta> jobMetas = new ArrayList<JobMeta>();
	  jobMetas.add(jobMeta);
	  List<JobResourcecategory> jobResourceCategories = new ArrayList <JobResourcecategory>();
	  JobResourcecategory jobResourceCategory = new JobResourcecategory();
	  ResourceCategory resourceCategory = new ResourceCategory();
	  ResourceType resourceType = new ResourceType();
	  resourceType.setIName("mps");
	  resourceType.setResourceTypeId(1);
	  resourceCategory.setResourceType(resourceType);
	  resourceCategory.setName("Illumina HiSeq 2000");
	  jobResourceCategory.setResourceCategory(resourceCategory);
	  jobResourceCategories.add(jobResourceCategory);
	  
	  //Test case:2
	  Task task = new Task();
	  task.setIName("PI Approval");
	  	  
	  //Test case: 1a: status="CREATED"
	  Job job = new Job();
	  job.setJobMeta(jobMetas);
	  job.setJobResourcecategory(jobResourceCategories);
	  Statejob stateJob = new Statejob();
	  List<Statejob> statejobs = new ArrayList<Statejob>();
	  State state = new State();
	  state.setTask(task);
	  state.setStatus("CREATED");
	  stateJob.setState(state);
	  statejobs.add(stateJob);
	  job.setStatejob(statejobs);
	  
	  //Test case: 1b status="COMPLETED"
	  Job job2 = new Job();
	  job2.setJobMeta(jobMetas);
	  job2.setJobResourcecategory(jobResourceCategories);
	  Statejob stateJob2 = new Statejob();
	  List<Statejob> statejobs2 = new ArrayList<Statejob>();
	  State state2 = new State();
	  state2.setTask(task);
	  state2.setStatus("COMPLETED");
	  stateJob2.setState(state2);
	  statejobs2.add(stateJob2);
	  job2.setStatejob(statejobs2);
	  
	  //Test case: 1c: status="ABANDONED"
	  Job job3 = new Job();
	  job3.setJobMeta(jobMetas);
	  job3.setJobResourcecategory(jobResourceCategories);
	  Statejob stateJob3 = new Statejob();
	  List<Statejob> statejobs3 = new ArrayList<Statejob>();
	  State state3 = new State();
	  state3.setTask(task);
	  state3.setStatus("ABANDONED");
	  stateJob3.setState(state3);
	  statejobs3.add(stateJob3);
	  job3.setStatejob(statejobs3);
	  
	  //Test case: 2:  If task="PI Approval"
	  //Test case: 2a: status="CREATED"
	  Map<String, String> extraJobDetailsMap = new HashMap<String, String>();
	  extraJobDetailsMap.put("Machine", jobResourceCategory.getResourceCategory().getName());
	  extraJobDetailsMap.put("Read Length", jobMeta.getV());
	  extraJobDetailsMap.put("PI Approval", "Awaiting Response");
	  extraJobDetailsMap.put("DA Approval", "Not Yet Set");
	  extraJobDetailsMap.put("Quote Job Price", "Not Yet Set");
	  
	  //Test case: 2b: status="COMPLETED" OR  status="FINALIZED"
	  Map<String, String> extraJobDetailsMap2 = new HashMap<String, String>();
	  extraJobDetailsMap2.put("Machine", jobResourceCategory.getResourceCategory().getName());
	  extraJobDetailsMap2.put("Read Length", jobMeta.getV());
	  extraJobDetailsMap2.put("PI Approval", "Approved");
	  extraJobDetailsMap2.put("DA Approval", "Not Yet Set");
	  extraJobDetailsMap2.put("Quote Job Price", "Not Yet Set");
	  
	  //Test case: 2c: status="ABANDONED"
	  Map<String, String> extraJobDetailsMap3 = new HashMap<String, String>();
	  extraJobDetailsMap3.put("Machine", jobResourceCategory.getResourceCategory().getName());
	  extraJobDetailsMap3.put("Read Length", jobMeta.getV());
	  extraJobDetailsMap3.put("PI Approval", "Rejected");
	  extraJobDetailsMap3.put("DA Approval", "Not Yet Set");
	  extraJobDetailsMap3.put("Quote Job Price", "Not Yet Set");

	  Assert.assertEquals(extraJobDetailsMap, jobServiceImpl.getExtraJobDetails(job));
	  Assert.assertEquals(extraJobDetailsMap2, jobServiceImpl.getExtraJobDetails(job2));
	  Assert.assertEquals(extraJobDetailsMap3, jobServiceImpl.getExtraJobDetails(job3));

	  
  }

  @Test
  public void getExtraJobDetails3() {

	  JobMeta jobMeta = new JobMeta();
	  jobMeta.setK("readLength");
	  jobMeta.setV("");
	  List <JobMeta> jobMetas = new ArrayList<JobMeta>();
	  jobMetas.add(jobMeta);
	  List<JobResourcecategory> jobResourceCategories = new ArrayList <JobResourcecategory>();
	  JobResourcecategory jobResourceCategory = new JobResourcecategory();
	  ResourceCategory resourceCategory = new ResourceCategory();
	  ResourceType resourceType = new ResourceType();
	  resourceType.setIName("mps");
	  resourceType.setResourceTypeId(1);
	  resourceCategory.setResourceType(resourceType);
	  resourceCategory.setName("Illumina HiSeq 2000");
	  jobResourceCategory.setResourceCategory(resourceCategory);
	  jobResourceCategories.add(jobResourceCategory);
	  
	  //Test case:3
	  Task task = new Task();
	  task.setIName("Quote Job");
	  	  
	  //Test case: 3a: status="CREATED"
	  Job job = new Job();
	  job.setJobMeta(jobMetas);
	  job.setJobResourcecategory(jobResourceCategories);
	  Statejob stateJob = new Statejob();
	  List<Statejob> statejobs = new ArrayList<Statejob>();
	  State state = new State();
	  state.setTask(task);
	  state.setStatus("CREATED");
	  stateJob.setState(state);
	  statejobs.add(stateJob);
	  job.setStatejob(statejobs);
	  
	  //Test case: 3b status="COMPLETED"
	  Job job2 = new Job();
	  job2.setJobMeta(jobMetas);
	  job2.setJobResourcecategory(jobResourceCategories);
	  Statejob stateJob2 = new Statejob();
	  List<Statejob> statejobs2 = new ArrayList<Statejob>();
	  State state2 = new State();
	  state2.setTask(task);
	  state2.setStatus("COMPLETED");
	  stateJob2.setState(state2);
	  statejobs2.add(stateJob2);
	  job2.setStatejob(statejobs2);
	  
	  //Test case: 3c: status="ABANDONED"
	  Job job3 = new Job();
	  job3.setJobMeta(jobMetas);
	  job3.setJobResourcecategory(jobResourceCategories);
	  Statejob stateJob3 = new Statejob();
	  List<Statejob> statejobs3 = new ArrayList<Statejob>();
	  State state3 = new State();
	  state3.setTask(task);
	  state3.setStatus("ABANDONED");
	  stateJob3.setState(state3);
	  statejobs3.add(stateJob3);
	  job3.setStatejob(statejobs3);
	  
	  //Test case: 3:  If task="Quote Job"
	  //Test case: 3a: status="CREATED"
	  Map<String, String> extraJobDetailsMap = new HashMap<String, String>();
	  extraJobDetailsMap.put("Machine", jobResourceCategory.getResourceCategory().getName());
	  extraJobDetailsMap.put("Read Length", jobMeta.getV());
	  extraJobDetailsMap.put("Quote Job Price", "Awaiting Quote");
	  extraJobDetailsMap.put("DA Approval", "Not Yet Set");
	  extraJobDetailsMap.put("PI Approval", "Not Yet Set");
	  
	  //Test case: 3b: status="COMPLETED" OR  status="FINALIZED"
	  AcctJobquotecurrent acctJobQuoteCurrent = new AcctJobquotecurrent();
	  AcctQuote acctQuote = new AcctQuote();
	  //TO DO: need to add more test cases to test different amounts
	  Float price = new Float(123.45);
	  acctQuote.setAmount(new Float(123.45));
	  acctJobQuoteCurrent.setAcctQuote(acctQuote);
	  List <AcctJobquotecurrent> acctJobQuoteCurrentList = new ArrayList <AcctJobquotecurrent>();
	  acctJobQuoteCurrentList.add(0, acctJobQuoteCurrent);
	  job2.setAcctJobquotecurrent(acctJobQuoteCurrentList);
	  //Float price = new Float(job.getAcctJobquotecurrent().get(0).getAcctQuote().getAmount());

	  Map<String, String> extraJobDetailsMap2 = new HashMap<String, String>();
	  extraJobDetailsMap2.put("Machine", jobResourceCategory.getResourceCategory().getName());
	  extraJobDetailsMap2.put("Read Length", jobMeta.getV());
	  extraJobDetailsMap2.put("Quote Job Price", "$"+String.format("%.2f", price));
	  extraJobDetailsMap2.put("DA Approval", "Not Yet Set");
	  extraJobDetailsMap2.put("PI Approval", "Not Yet Set");
	  
	  //Test case: 3c: other status
	  Map<String, String> extraJobDetailsMap3 = new HashMap<String, String>();
	  extraJobDetailsMap3.put("Machine", jobResourceCategory.getResourceCategory().getName());
	  extraJobDetailsMap3.put("Read Length", jobMeta.getV());
	  extraJobDetailsMap3.put("Quote Job Price", "Unknown");
	  extraJobDetailsMap3.put("DA Approval", "Not Yet Set");
	  extraJobDetailsMap3.put("PI Approval", "Not Yet Set");

	  Assert.assertEquals(extraJobDetailsMap, jobServiceImpl.getExtraJobDetails(job));
	  Assert.assertEquals(extraJobDetailsMap2, jobServiceImpl.getExtraJobDetails(job2));
	  Assert.assertEquals(extraJobDetailsMap3, jobServiceImpl.getExtraJobDetails(job3));

	  
  }

  */
  @BeforeMethod
  public void beforeMethod() {
	  jobServiceImpl.setJobExplorer(mockJobExplorerWasp);
	  jobServiceImpl.setJobDao(mockJobDao);
	  jobServiceImpl.setJobMetaDao(mockJobMetaDao);
	  jobServiceImpl.setJobSoftwareDao(mockJobSoftwareDao);
	  jobServiceImpl.setJobResourcecategoryDao(mockJobResourcecategoryDao);
	  jobServiceImpl.setRoleDao(mockRoleDao);
	  jobServiceImpl.setJobUserDao(mockJobUserDao);
	  jobServiceImpl.setLabDao(mockLabDao);
	  jobServiceImpl.setJobCellSelectionDao(mockJobCellSelectionDao);
	  jobServiceImpl.setSampleDao(mockSampleDao);
	  jobServiceImpl.setJobSampleDao(mockJobSampleDao);
	  jobServiceImpl.setSampleJobCellSelectionDao(mockSampleJobCellSelectionDao);
	  jobServiceImpl.setWorkflowDao(mockWorkflowDao);
	  jobServiceImpl.setJobDraftDao(mockJobDraftDao);
	  jobServiceImpl.setSampleMetaDao(mockSampleMetaDao);


  }

  @AfterMethod
  public void afterMethod() {
	
	  EasyMock.reset(mockTaskMappingDao);
	  EasyMock.reset(mockJobSampleDao);
	  EasyMock.reset(mockSampleDao);
	  EasyMock.reset(mockJobDao);
	  EasyMock.reset(mockJobExplorerWasp);
	  EasyMock.reset(mockJobDao);
	  EasyMock.reset(mockJobMetaDao);
	  EasyMock.reset(mockJobSoftwareDao);
	  EasyMock.reset(mockJobResourcecategoryDao);
	  EasyMock.reset(mockRoleDao);
	  EasyMock.reset(mockJobUserDao);
	  EasyMock.reset(mockRoleDao);
	  EasyMock.reset(mockJobCellSelectionDao);
	  EasyMock.reset(mockSampleJobCellSelectionDao);
	  EasyMock.reset(mockWorkflowDao);
	  EasyMock.reset(mockJobServiceImpl);
	  EasyMock.reset(mockJobDraftDao);
	  EasyMock.reset(mockSampleMetaDao);
	  EasyMock.reset(mockSampleService);


	  
	  //resetAll();//resets all registered mock controls


  }

  @BeforeClass
  public void beforeClass() {
  }

  @AfterClass
  public void afterClass() {
  }

  @BeforeTest
  public void beforeTest() {
	  mockTaskMappingDao = createMockBuilder(TaskMappingDaoImpl.class).addMockedMethods(TaskMappingDaoImpl.class.getMethods()).createMock();
	  mockJobSampleDao = createMockBuilder(JobSampleDaoImpl.class).addMockedMethods(JobSampleDaoImpl.class.getMethods()).createMock();
	  mockSampleDao = createMockBuilder(SampleDaoImpl.class).addMockedMethods(SampleDaoImpl.class.getMethods()).createMock();	  
	  mockJobDao = createMockBuilder(JobDaoImpl.class).addMockedMethods(JobDaoImpl.class.getMethods()).createMock();
	  mockJobMetaDao = createMockBuilder(JobMetaDaoImpl.class).addMockedMethods(JobMetaDaoImpl.class.getMethods()).createMock();
	  mockJobSoftwareDao = createMockBuilder(JobSoftwareDaoImpl.class).addMockedMethods(JobSoftwareDaoImpl.class.getMethods()).createMock();
	  mockJobResourcecategoryDao = createMockBuilder(JobResourcecategoryDaoImpl.class).addMockedMethods(JobResourcecategoryDaoImpl.class.getMethods()).createMock();
	  mockRoleDao = createMockBuilder(RoleDaoImpl.class).addMockedMethods(RoleDaoImpl.class.getMethods()).createMock();
	  mockJobUserDao = createMockBuilder(JobUserDaoImpl.class).addMockedMethods(JobUserDaoImpl.class.getMethods()).createMock();
	  mockLabDao = createMockBuilder(LabDaoImpl.class).addMockedMethods(LabDaoImpl.class.getMethods()).createMock();
	  mockJobCellSelectionDao = createMockBuilder(JobCellSelectionDaoImpl.class).addMockedMethods(JobCellSelectionDaoImpl.class.getMethods()).createMock();
	  mockSampleJobCellSelectionDao = createMockBuilder(SampleJobCellSelectionDaoImpl.class).addMockedMethods(SampleJobCellSelectionDaoImpl.class.getMethods()).createMock();
	  mockWorkflowDao = createMockBuilder(WorkflowDaoImpl.class).addMockedMethods(WorkflowDaoImpl.class.getMethods()).createMock();
	  mockJobDraftDao = createMockBuilder(JobDraftDaoImpl.class).addMockedMethods(JobDraftDaoImpl.class.getMethods()).createMock();
	  mockSampleMetaDao = createMockBuilder(SampleMetaDaoImpl.class).addMockedMethods(SampleMetaDaoImpl.class.getMethods()).createMock();

	  mockJobExplorerWasp = EasyMock.createNiceMock(JobExplorerWasp.class);
	  		
	  mockJobServiceImpl = EasyMock
		         .createMockBuilder(JobServiceImpl.class) //create builder first
		         .addMockedMethod("getActiveJobs") // tell EasyMock to mock getActiveJobs() method
		         .createMock(); 
	  
	  mockSampleService = EasyMock
		         .createMockBuilder(SampleServiceImpl.class) //create builder first
		         .addMockedMethod("isSampleAwaitingLibraryCreation") // tell EasyMock to mock getActiveJobs() method
		         .createMock(); 
	
	  Assert.assertNotNull(mockTaskMappingDao);
	  Assert.assertNotNull(mockJobSampleDao);
	  Assert.assertNotNull(mockSampleDao);
	  Assert.assertNotNull(mockJobDao);
	  Assert.assertNotNull(mockJobExplorerWasp);
	  Assert.assertNotNull(mockJobDao);
	  Assert.assertNotNull(mockJobMetaDao);
	  Assert.assertNotNull(mockJobSoftwareDao);
	  Assert.assertNotNull(mockJobResourcecategoryDao);
	  Assert.assertNotNull(mockRoleDao);
	  Assert.assertNotNull(mockJobUserDao);
	  Assert.assertNotNull(mockLabDao);
	  Assert.assertNotNull(mockJobCellSelectionDao);
	  Assert.assertNotNull(mockWorkflowDao);
	  Assert.assertNotNull(mockJobServiceImpl);
	  Assert.assertNotNull(mockJobDraftDao);
	  Assert.assertNotNull(mockSampleMetaDao);


  }

  @AfterTest
  public void afterTest() {
  }

}
