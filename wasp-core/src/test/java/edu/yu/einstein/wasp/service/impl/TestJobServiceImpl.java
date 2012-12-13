package edu.yu.einstein.wasp.service.impl;

import java.lang.reflect.Field;
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
import org.junit.runner.RunWith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.JobStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.payload.WaspStatus;
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

	  mockJobServiceImpl.setSampleService(mockSampleService);
      
      expect(mockSampleService.isSampleAwaitingLibraryCreation(sample)).andReturn(true);
      replay(mockSampleService);
		
      JobsAwaitingLibraryCreation.add(job);
      Assert.assertEquals(mockJobServiceImpl.getJobsAwaitingLibraryCreation(), JobsAwaitingLibraryCreation);
      
      verify(mockJobServiceImpl);
      verify(mockSampleService);
	  
	  
  }
  
  @Test
  public void getJobsAwaitingLibraryCreation2(){
	  
	  List<Job> JobsAwaitingLibraryCreation = new ArrayList<Job>();
	  List<Job> activeJobs = new ArrayList<Job>();
	  
	  expect(mockJobServiceImpl.getActiveJobs()).andReturn(activeJobs);
      replay(mockJobServiceImpl);

      Assert.assertEquals(mockJobServiceImpl.getJobsAwaitingLibraryCreation(), JobsAwaitingLibraryCreation);
      
      verify(mockJobServiceImpl);
	  
	  
  }

  @Test (description="isLibrary=true AND isLibraryAwaitingPlatformUnitPlacement(sample) && sampleService.isLibraryPassQC(sample) is TRUE")
  public void getJobsWithLibrariesToGoOnPlatformUnit() {
	  
		List<Job> jobsWithLibrariesToGoOnFlowCell = new ArrayList<Job>();
		
		Job job = new Job();
		job.setJobId(1);
		List<Sample> samples = new ArrayList<Sample>();

		Sample sample = new Sample();
		samples.add(sample);
		job.setSample(samples);
		List<Job> activeJobs = new ArrayList<Job>();
		activeJobs.add(job);
		
		expect(mockJobServiceImpl.getActiveJobs()).andReturn(activeJobs);
	    replay(mockJobServiceImpl);	   
	    
	    mockJobServiceImpl.setSampleService(mockSampleService);

	    expect(mockSampleService.isLibrary(sample)).andReturn(true);
	    try {
			expect(mockSampleService.isLibraryAwaitingPlatformUnitPlacement(sample)).andReturn(true);
		} catch (SampleTypeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		expect(mockSampleService.isLibraryPassQC(sample)).andReturn(true);
	    replay(mockSampleService);
	    
	    jobsWithLibrariesToGoOnFlowCell.add(job);
	    Assert.assertEquals(mockJobServiceImpl.getJobsWithLibrariesToGoOnPlatformUnit(), jobsWithLibrariesToGoOnFlowCell);
		
		verify(mockJobServiceImpl);
		verify(mockSampleService);
 
	  
  }
  @Test (description="isLibrary=true AND isLibraryAwaitingPlatformUnitPlacement(sample) && sampleService.isLibraryPassQC(sample) is FALSE")
  public void getJobsWithLibrariesToGoOnPlatformUnit2() {
	  
		List<Job> jobsWithLibrariesToGoOnFlowCell = new ArrayList<Job>();
		
		Job job = new Job();
		job.setJobId(1);
		List<Sample> samples = new ArrayList<Sample>();

		Sample sample = new Sample();
		samples.add(sample);
		job.setSample(samples);
		List<Job> activeJobs = new ArrayList<Job>();
		activeJobs.add(job);
		
		
		expect(mockJobServiceImpl.getActiveJobs()).andReturn(activeJobs);
	    replay(mockJobServiceImpl);	   
	    
	    mockJobServiceImpl.setSampleService(mockSampleService);

	    expect(mockSampleService.isLibrary(sample)).andReturn(true);
	    try {
			expect(mockSampleService.isLibraryAwaitingPlatformUnitPlacement(sample)).andReturn(false);
		} catch (SampleTypeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	    replay(mockSampleService);
	    
	    Assert.assertEquals(mockJobServiceImpl.getJobsWithLibrariesToGoOnPlatformUnit(), jobsWithLibrariesToGoOnFlowCell);
		
		verify(mockJobServiceImpl);
		verify(mockSampleService);
 
	  
  }
  
  @Test (description="isLibrary=false, sample has children")
  public void getJobsWithLibrariesToGoOnPlatformUnit3() {
	  
		List<Job> jobsWithLibrariesToGoOnFlowCell = new ArrayList<Job>();
		
		Job job = new Job();
		job.setJobId(1);
		
		List<Sample> childrenList = new ArrayList<Sample>();
		Sample children = new Sample();
		childrenList.add(children);

		List<Sample> samples = new ArrayList<Sample>();
		Sample sample = new Sample();
		sample.setChildren(childrenList);
		samples.add(sample);
		
		job.setSample(samples);
		List<Job> activeJobs = new ArrayList<Job>();
		activeJobs.add(job);	
		
		expect(mockJobServiceImpl.getActiveJobs()).andReturn(activeJobs);
	    replay(mockJobServiceImpl);	   
	    
	    mockJobServiceImpl.setSampleService(mockSampleService);

	    expect(mockSampleService.isLibrary(sample)).andReturn(false);
	    
	    try {
			expect(mockSampleService.isLibraryAwaitingPlatformUnitPlacement(children)).andReturn(true);
		} catch (SampleTypeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		expect(mockSampleService.isLibraryPassQC(children)).andReturn(true);
	    replay(mockSampleService);
	    
	    jobsWithLibrariesToGoOnFlowCell.add(job);
			    
	    Assert.assertEquals(mockJobServiceImpl.getJobsWithLibrariesToGoOnPlatformUnit(), jobsWithLibrariesToGoOnFlowCell);
		
		verify(mockJobServiceImpl);
		verify(mockSampleService);
 
	  
  }
  
  @Test (description="isLibrary=true")
  public void getJobsWithLibrariesToGoOnPlatformUnit4() {
	  
		List<Job> jobsWithLibrariesToGoOnFlowCell = new ArrayList<Job>();
		
		List<Sample> childrenList = new ArrayList<Sample>();
		Sample children = new Sample();
		childrenList.add(children);
		List<Sample> samples = new ArrayList<Sample>();
		Sample sample = new Sample();
		sample.setChildren(childrenList);
		samples.add(sample);
		Job job = new Job();
		job.setJobId(1);
		job.setSample(samples);
		
		List<Sample> childrenList2 = new ArrayList<Sample>();
		Sample children2 = new Sample();
		childrenList2.add(children2);
		List<Sample> samples2 = new ArrayList<Sample>();
		Sample sample2= new Sample();
		sample2.setChildren(childrenList2);
		samples2.add(sample2);
		Job job2 = new Job();
		job2.setJobId(2);
		job2.setSample(samples2);
		
		List<Sample> childrenList3 = new ArrayList<Sample>();
		Sample children3 = new Sample();
		childrenList3.add(children3);
		List<Sample> samples3 = new ArrayList<Sample>();
		Sample sample3= new Sample();
		sample3.setChildren(childrenList3);
		samples3.add(sample3);
		Job job3 = new Job();
		job3.setJobId(3);
		job3.setSample(samples3);
		
		List<Job> activeJobs = new ArrayList<Job>();
		activeJobs.add(job);	
		activeJobs.add(job2);	
		activeJobs.add(job3);	


		expect(mockJobServiceImpl.getActiveJobs()).andReturn(activeJobs);
	    replay(mockJobServiceImpl);	   
	    
	    mockJobServiceImpl.setSampleService(mockSampleService);

	    expect(mockSampleService.isLibrary(sample)).andReturn(true);
	    
	    try {
			expect(mockSampleService.isLibraryAwaitingPlatformUnitPlacement(sample)).andReturn(true);
			
		} catch (SampleTypeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		expect(mockSampleService.isLibraryPassQC(sample)).andReturn(false);
	
	    expect(mockSampleService.isLibrary(sample2)).andReturn(true);
	    try {
			expect(mockSampleService.isLibraryAwaitingPlatformUnitPlacement(sample2)).andReturn(false);
			
		} catch (SampleTypeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	    expect(mockSampleService.isLibrary(sample3)).andReturn(true);
	    try {
			expect(mockSampleService.isLibraryAwaitingPlatformUnitPlacement(sample3)).andReturn(true);
			
		} catch (SampleTypeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		expect(mockSampleService.isLibraryPassQC(sample3)).andReturn(true);

	    replay(mockSampleService);
	    
	    jobsWithLibrariesToGoOnFlowCell.add(job3);
			    
	    Assert.assertEquals(mockJobServiceImpl.getJobsWithLibrariesToGoOnPlatformUnit(), jobsWithLibrariesToGoOnFlowCell);
		
		verify(mockJobServiceImpl);
		verify(mockSampleService);
  
  }
  
  @Test
  public void getJobsSubmittedOrViewableByUser() {
	  try {
		   jobServiceImpl.getJobsSubmittedOrViewableByUser(null);
	  }
	  catch (InvalidParameterException e){
		  Assert.assertEquals(e.getMessage(), "No User provided");		  
	  }
	  
	  try {
	   User user = new User();

	   jobServiceImpl.getJobsSubmittedOrViewableByUser(user);
	  }
	  catch (InvalidParameterException e){
		  Assert.assertEquals(e.getMessage(), "Invalid User Provided");  
	  }
  }
  
  @Test
  public void getJobsSubmittedOrViewableByUser2() {
	  User user = new User();
	  user.setUserId(1);
	   
	  Map m = new HashMap();
	  m.put("UserId", user.getUserId().intValue()); 
	  List<String> orderByColumnNames = new ArrayList<String>();
	  orderByColumnNames.add("jobId");
	  
	  List<JobUser> jobUserList = new ArrayList<JobUser>();
	  JobUser jobUser = new JobUser();
	  JobUser jobUser2 = new JobUser();
	  JobUser jobUser3 = new JobUser();

	  Job job = new Job();
	  jobUser.setJob(job);
	  Job job2 = new Job();
	  jobUser2.setJob(job2);
	  Job job3 = new Job();
	  jobUser3.setJob(job3);
	  jobUserList.add(jobUser);
	  jobUserList.add(jobUser2);
	  jobUserList.add(jobUser3);
	  
	  expect(mockJobUserDao.findByMapDistinctOrderBy(m, null, orderByColumnNames, "desc")).andReturn(jobUserList);
	  
	  expect(mockJobUserDao.findByMapDistinctOrderBy(m, null, orderByColumnNames, "desc")).andReturn(new ArrayList<JobUser>());

	  replay(mockJobUserDao);
	  
	  List<Job> jobList = new ArrayList<Job>();
	  jobList.add(job);
	  jobList.add(job2);
	  jobList.add(job3);
	  
	  Assert.assertEquals(jobServiceImpl.getJobsSubmittedOrViewableByUser(user), jobList);
	  Assert.assertEquals(jobServiceImpl.getJobsSubmittedOrViewableByUser(user), new ArrayList<Job>());

	  verify(mockJobUserDao);
		  
  }
  
  @Test
  public void isJobAwaitingLibraryCreation() {
	  
	  try {
		   jobServiceImpl.isJobAwaitingLibraryCreation(null, null);
	  }
	  catch (InvalidParameterException e){
		  Assert.assertEquals(e.getMessage(), "No Sample provided");		  
	  }
	  
	  try {
	   jobServiceImpl.isJobAwaitingLibraryCreation(new Job(), new Sample());
	  }
	  catch (InvalidParameterException e){
		  Assert.assertEquals(e.getMessage(), "Invalid Sample Provided");  
	  }
	  
	  Sample sample = new Sample();
	  sample.setSampleId(1);
	  try {
		  
		  jobServiceImpl.isJobAwaitingLibraryCreation(null, sample);
	  }
	  catch (InvalidParameterException e){
		  Assert.assertEquals(e.getMessage(), "No Job provided");		  
	  }
	  try {
	   jobServiceImpl.isJobAwaitingLibraryCreation(new Job(),sample);
	  }
	  catch (InvalidParameterException e){
		  Assert.assertEquals(e.getMessage(), "Invalid Job Provided");  
	  }
	  
	  
  }
  
  @Test (description="supplied sample is associated with supplied job")
  public void isJobAwaitingLibraryCreation2() {
	  
	  Sample sample = new Sample();
	  sample.setSampleId(1);
	  
	  List<Sample> samples = new ArrayList<Sample>();
	  Sample sample2 = new Sample();
	  sample2.setSampleId(1);
	  samples.add(sample2);
	  
	  Job job = new Job();
	  job.setJobId(1);
	  job.setSample(samples);

	  // TODO need to figure out how to mock logger and add a test for when the supplied sample is not associated with supplied job 
	  //expect(LoggerFactory.getLogger(WaspServiceImpl.class)).andReturn(loggerMock);
	  
	  mockJobServiceImpl.setSampleService(mockSampleService);
	  expect(mockSampleService.isSampleAwaitingLibraryCreation(sample)).andReturn(true);
	  replay(mockSampleService);

	  Assert.assertEquals(mockJobServiceImpl.isJobAwaitingLibraryCreation(job, sample), true);
	  
	  verify(mockSampleService);
  }

  /*
  @Test (description="WaspStatus.COMPLETED")
  public void updateJobPiApprovalStatus() {
	  WaspStatus status = WaspStatus.COMPLETED;
	  String task = "Sample Received";
	  
	  Job job = new Job();
	  job.setJobId(1);
	  
	  JobStatusMessageTemplate messageTemplate = new JobStatusMessageTemplate(job.getJobId());
	  messageTemplate.setTask(task);
	  messageTemplate.setStatus(status); 
	  
	  WaspMessageHandlingServiceImpl mockWaspMsgHdlSrvImpl = EasyMock.createStrictMock(WaspMessageHandlingServiceImpl.class);
      try {
    	  mockWaspMsgHdlSrvImpl.sendOutboundMessage(messageTemplate.build(), false);
	  } catch (WaspMessageBuildingException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	  }
	  EasyMock.expectLastCall();
	  try {
		jobServiceImpl.updateJobPiApprovalStatus(job, status);
	} catch (WaspMessageBuildingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
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
		         .addMockedMethods("getActiveJobs") // tell EasyMock to mock getActiveJobs() method
		         .createMock(); 
	  /*
	  mockSampleService = EasyMock
		         .createMockBuilder(SampleServiceImpl.class) //create builder first
		         .addMockedMethods("isSampleAwaitingLibraryCreation",
		        		 "isLibrary",
		        		 "isLibraryAwaitingPlatformUnitPlacement", 
		        		 "isLibraryPassQC") // tell EasyMock to mock getActiveJobs() method
		         .createMock(); 
	  */
	  mockSampleService = EasyMock
		         .createMockBuilder(SampleServiceImpl.class)
		         .addMockedMethods(SampleServiceImpl.class.getMethods()) 
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
