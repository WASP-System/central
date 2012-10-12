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
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.Task;

public class TestJobServiceImpl {
	
  TaskDao mockTaskDao;
	
  JobServiceImpl jobServiceImpl = new JobServiceImpl();
  
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
