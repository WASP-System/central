package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.Sample;

public class TestJobServiceImpl {
	
  JobServiceImpl jobServiceImpl = new JobServiceImpl();
  
  @Test
  public void getSubmittedSamples() {
	  
	  List<JobSample> jobSamples = new ArrayList<JobSample>();
	  
	  Job job = new Job();
	  Sample sample = new Sample();
	  Sample sampleParent  = new Sample();
	  //SampleSubtype sampleSubtype = new SampleSubtype();
	  sample.setParent(sampleParent);
	    
	  JobSample jobSample = new JobSample();
	  jobSample.setSample(sample);
	  jobSamples.add(jobSample);
	  job.setJobId(0001);
	  job.setJobSample(jobSamples);
	  
	  //expected
	  List<Sample> submittedSamplesList = new ArrayList<Sample>();
	  
	  Assert.assertEquals(jobServiceImpl.getSubmittedSamples(job), submittedSamplesList);
	  
  }
  @BeforeMethod
  public void beforeMethod() {
  }

  @AfterMethod
  public void afterMethod() {
  }

  @BeforeClass
  public void beforeClass() {
  }

  @AfterClass
  public void afterClass() {
  }

  @BeforeTest
  public void beforeTest() {
  }

  @AfterTest
  public void afterTest() {
  }

}
