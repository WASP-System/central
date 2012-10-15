package edu.yu.einstein.wasp.service.impl;

import static org.easymock.EasyMock.createMockBuilder;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.easymock.EasyMock;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.dao.impl.DepartmentUserDaoImpl;
import edu.yu.einstein.wasp.dao.impl.TaskDaoImpl;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobSample;
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
