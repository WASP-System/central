package edu.yu.einstein.wasp.service.impl;

import static org.easymock.EasyMock.createMockBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.easymock.EasyMock;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.dao.impl.TaskDaoImpl;
import edu.yu.einstein.wasp.model.AcctJobquotecurrent;
import edu.yu.einstein.wasp.model.AcctQuote;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.service.TaskService;

public class TestJobServiceImpl {
	
  TaskDao mockTaskDao;
	
  JobServiceImpl jobServiceImpl = new JobServiceImpl();
  TaskServiceImpl taskServiceImpl = new TaskServiceImpl();
  

  
  //Test when sample.getParent() is NOT null (a facility-generated library)
  @Test
  public void getSubmittedSamples() {
	  
	  List<JobSample> jobSamples = new ArrayList<JobSample>();
	 
	  Job job = new Job();
	  Sample sample = new Sample();
	  Sample sampleParent  = new Sample();
	  sample.setParent(sampleParent);
	    
	  JobSample jobSample = new JobSample();
	  jobSample.setSample(sample);
	  jobSamples.add(jobSample);
	  job.setJobId(0001);
	  job.setJobSample(jobSamples);
	  
	  //expected
	  List<Sample> submittedSamplesList = new ArrayList<Sample>();
	  
	  Assert.assertEquals(submittedSamplesList, jobServiceImpl.getSubmittedSamples(job));
	  
  }
  
  //Test when sample.getParent() is null (not a facility-generated library)
  @Test
  public void getSubmittedSamples2() {
	  
	  List<JobSample> jobSamples = new ArrayList<JobSample>();
	  
	  Job job = new Job();
	  Sample sample = new Sample();
	  JobSample jobSample = new JobSample();
	  jobSample.setSample(sample);
	  jobSamples.add(jobSample);
	  job.setJobId(0001);
	  job.setJobSample(jobSamples);
	  
	  //expected
	  List<Sample> submittedSamplesList = new ArrayList<Sample>();
	  submittedSamplesList.add(sample);
	  
	  Assert.assertEquals(submittedSamplesList, jobServiceImpl.getSubmittedSamples(job));
	  
  }
  
  //Test when job is null
  @Test
  public void getSubmittedSamples3() {

	  //expected
	  List<Sample> submittedSamplesList = new ArrayList<Sample>();
	  
	  Assert.assertEquals(submittedSamplesList, jobServiceImpl.getSubmittedSamples(null));
	  
  }

  @Test
  public void getSubmittedSamplesAwaitingSubmission() {
	  
	  List<JobSample> jobSamples = new ArrayList<JobSample>();
	  List<Statesample> stateSamples = new ArrayList<Statesample>();
	  Statesample stateSample = new Statesample();
	  State state = new State();
	  state.setStatus("CREATED");
	  Task task = new Task();
	  task.setIName("abc");
	  state.setTask(task);
	  stateSample.setState(state);
	  stateSamples.add(stateSample);
	  
	  Sample sample = new Sample();
	  //Sample sampleParent  = new Sample();
	  //sample.setParent(sampleParent);
	  sample.setStatesample(stateSamples);
	  
	  JobSample jobSample = new JobSample();
	  jobSample.setSample(sample);
	  jobSamples.add(jobSample);
	  Job job = new Job();
	  job.setJobId(0001);
	  job.setJobSample(jobSamples);
	  	  
	  Task receiveSampleTask = new Task();
	  receiveSampleTask.setIName("abc");
	  
	  jobServiceImpl.setTaskDao(mockTaskDao);
	  expect(mockTaskDao.getTaskByIName("Receive Sample")).andReturn(receiveSampleTask);
	  replay(mockTaskDao);
 
	  //expected
	  List<Sample> submittedSamplesAwaitingSubmissionList = new ArrayList<Sample>();
	  submittedSamplesAwaitingSubmissionList.add(sample);
	  
	  Assert.assertEquals(submittedSamplesAwaitingSubmissionList, jobServiceImpl.getSubmittedSamplesAwaitingSubmission(job));
	  
	  verify(mockTaskDao);
	  
  }
  
  @Test
  public void getActiveJobs() {
	  
	  
	  //mockDao returns
	  List<State> states = new ArrayList<State>();
	  List<Statejob> stateJobs = new ArrayList<Statejob>();
	  Statejob stateJob = new Statejob();
	  Job job = new Job();
	  stateJob.setJob(job);
	  stateJobs.add(stateJob);
	  State state = new State();
	  state.setStatejob(stateJobs);
	  states.add(state);
	  
	  taskServiceImpl.setTaskDao(mockTaskDao);
	  jobServiceImpl.setTaskService(taskServiceImpl);
	  expect(mockTaskDao.getStatesByTaskIName("Start Job", "CREATED")).andReturn(states);
	  replay(mockTaskDao);
	  
	  //expected
	  List<Job> activeJobList = new ArrayList<Job>();
	  activeJobList.add(job);
	  Assert.assertEquals(activeJobList, jobServiceImpl.getActiveJobs());
	  verify(mockTaskDao);
	  
  }
  
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

  
  @BeforeMethod
  public void beforeMethod() {
  }

  @AfterMethod
  public void afterMethod() {
	  EasyMock.reset(mockTaskDao);

  }

  @BeforeClass
  public void beforeClass() {
  }

  @AfterClass
  public void afterClass() {
  }

  @BeforeTest
  public void beforeTest() {
	  mockTaskDao = createMockBuilder(TaskDaoImpl.class).addMockedMethods(TaskDaoImpl.class.getMethods()).createMock();
	  	  
	  Assert.assertNotNull(mockTaskDao);
  }

  @AfterTest
  public void afterTest() {
  }

}
