package edu.yu.einstein.wasp.batch.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.wasp.JobExplorerWasp;
import org.springframework.batch.core.repository.dao.wasp.BatchDaoDataRetrievalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.batch.core.extension.WaspBatchExitStatus;
import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;

@ContextConfiguration(locations={"classpath:batch/batch-test-context.xml"})

public class BatchAPIExtensionTests extends AbstractTestNGSpringContextTests {
	
	private JobExplorerWasp jobExplorer;
	
	@Autowired
	void setJobExplorer(JobExplorer jobExplorer){
		this.jobExplorer = (JobExplorerWasp) jobExplorer;
	}
	
	private final Logger logger = LoggerFactory.getLogger(BatchAPIExtensionTests.class);
	
	private final String JOB_ID_KEY = "jobId";
	
	private final String SAMPLE_ID_KEY = "sampleId";
	
	private final String SAMPLE_ID_KEY_WRONG = "wrongKey";
	
	private final Integer JOB_ID_1 = 1;
	
	private final Integer SAMPLE_ID_1 = 1;
	
	private final Integer SAMPLE_ID_2 = 2;

	
	
	@BeforeClass
	private void setup() {
		Assert.assertNotNull(jobExplorer);
	}
	
	/**
	 * API extension testing. Testing access of state information via API extension. The test Batch db tables should 
	 * contain two steps called 'wasp.sample.step.listenForSampleReceived' with a Batch Status of Completed.
	 * Only one of these should match the supplied jobId and sampleId.
	 */
	@Test(groups = "unit-tests")
	public void testGettingStepExecutionNormalTest1(){
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		
		Set<String> sampleIdStringSet = new HashSet<String>();
		sampleIdStringSet.add(SAMPLE_ID_1.toString());
		parameterMap.put(WaspJobParameters.SAMPLE_ID, sampleIdStringSet);
		
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(JOB_ID_1.toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		StepExecution stepExecution = null;
		try {
			stepExecution = jobExplorer.getStepExecution("listenForSampleReceived", parameterMap, false);
		} catch (BatchDaoDataRetrievalException e) {
			Assert.fail("Unable to get status");
		}
		Assert.assertNotNull(stepExecution);
		Assert.assertTrue(new WaspBatchExitStatus(stepExecution.getExitStatus()).isCompleted());
	}
	
	/**
	 * API extension testing. Testing access of state information via API extension.The test Batch db tables should contain two steps called 
	 * 'wasp.sample.step.listenForSampleReceived' with a Batch Status of Completed. The job parameters include both sample_id and job_id
	 * so if the 'exclusive' parameter is set to true, then nothing should be returned if only one of the two parameters is provided
	 */
	@Test(groups = "unit-tests")
	public void testGettingStepExecutionNormalTest2(){
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		
		Set<String> sampleIdStringSet = new HashSet<String>();
		sampleIdStringSet.add(SAMPLE_ID_1.toString());
		parameterMap.put(WaspJobParameters.SAMPLE_ID, sampleIdStringSet);
		
		StepExecution stepExecution = null;
		try {
			stepExecution = jobExplorer.getStepExecution("listenForSampleReceived", parameterMap, true);
		} catch (BatchDaoDataRetrievalException e) {
			Assert.fail("Unable to get status");
		}
		Assert.assertNull(stepExecution);
	}
	
	/**
	 * API extension testing. Testing access of state information via API extension.The test Batch db tables should contain two steps called 
	 * 'wasp.sample.step.listenForJobApproved' one with a WaspBatchExitStatus of COMPLETED (sample 2) and one with WaspBatchExitStatus of RUNNING (sample 1).
	 */
	@Test(groups = "unit-tests")
	public void testGettingStepExecutionNormalTest3(){
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		
		Set<String> sampleIdStringSet = new HashSet<String>();
		sampleIdStringSet.add(SAMPLE_ID_1.toString());
		parameterMap.put(WaspJobParameters.SAMPLE_ID, sampleIdStringSet);
		
		List<StepExecution> stepExecutions = jobExplorer.getStepExecutions("listenForJobApproved", parameterMap, false, WaspBatchExitStatus.RUNNING);
		Assert.assertNotNull(stepExecutions);
		Assert.assertEquals(stepExecutions.size(), 1); // expect to be STARTED
	}
	
	/**
	 * API extension testing. Testing access of state information via API extension.The test Batch db tables should contain two steps called 
	 * 'wasp.sample.step.listenForJobApproved' one with a WaspBatchExitStatus of COMPLETED (sample 2) and one with WaspBatchExitStatus of RUNNING (sample 1).
	 */
	@Test(groups = "unit-tests")
	public void testGettingStepExecutionNormalTest4(){
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		
		Set<String> sampleIdStringSet = new HashSet<String>();
		sampleIdStringSet.add(SAMPLE_ID_2.toString());
		parameterMap.put(WaspJobParameters.SAMPLE_ID, sampleIdStringSet);
		
		List<StepExecution> stepExecutions = jobExplorer.getStepExecutions("listenForJobApproved", parameterMap, false, WaspBatchExitStatus.RUNNING);
		Assert.assertNotNull(stepExecutions);
		Assert.assertTrue(stepExecutions.isEmpty()); //expect to be COMPLETE
	}
	
	/**
	 * API extension testing. Testing getting all StepExecutions
	 */
	@Test(groups = "unit-tests")
	public void testGettingStepExecutionNormalTest5(){
		List<StepExecution> stepExecutions = jobExplorer.getStepExecutions();
		Assert.assertNotNull(stepExecutions);
		Assert.assertEquals(stepExecutions.size(), 8); 
	}
	
	/**
	 * API extension testing. Testing getting all StepExecutions but not providing correct number of parameters
	 */
	@Test(groups = "unit-tests")
	public void testGettingStepExecutionNormalTest6(){
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		
		Set<String> sampleIdStringSet = new HashSet<String>();
		sampleIdStringSet.add("*");
		parameterMap.put(WaspJobParameters.SAMPLE_ID, sampleIdStringSet);
		
		List<StepExecution> stepExecutions = jobExplorer.getStepExecutions(parameterMap, true);
		Assert.assertNotNull(stepExecutions);
		Assert.assertTrue(stepExecutions.isEmpty()); 
	}
	
	/**
	 * API extension testing. Testing getting all StepExecutions 
	 */
	@Test(groups = "unit-tests")
	public void testGettingStepExecutionNormalTest7(){
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		
		Set<String> sampleIdStringSet = new HashSet<String>();
		sampleIdStringSet.add("*");
		parameterMap.put(WaspJobParameters.SAMPLE_ID, sampleIdStringSet);
		
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(JOB_ID_1.toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		
		List<StepExecution> stepExecutions = jobExplorer.getStepExecutions(parameterMap, true);
		Assert.assertNotNull(stepExecutions);
		Assert.assertEquals(stepExecutions.size(), 8); 
	}
	
	/**
	 * API extension testing. Testing getting all StepExecutions for sample 2
	 */
	@Test(groups = "unit-tests")
	public void testGettingStepExecutionNormalTest8(){
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		
		Set<String> sampleIdStringSet = new HashSet<String>();
		sampleIdStringSet.add(SAMPLE_ID_2.toString());
		parameterMap.put(WaspJobParameters.SAMPLE_ID, sampleIdStringSet);
		
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add("*");
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		List<StepExecution> stepExecutions = jobExplorer.getStepExecutions(parameterMap, true);
		Assert.assertNotNull(stepExecutions);
		Assert.assertEquals(stepExecutions.size(), 5); 
	}
	
		
	/**
	 * API extension testing. Testing access of state information via API extension.The test Batch db tables should contain two steps called 
	 * 'wasp.sample.step.listenForSampleReceived' with a Batch Status of Completed.
	 * Both of these should match the supplied jobId, so if the sampleId parameter is absent to distinguish them this test should throw
	 * an exception.
	 */
	@Test(groups = "unit-tests")
	public void testGettingStepExecutionFailureTest1(){
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(JOB_ID_1.toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		try {
			StepExecution stepExecution = jobExplorer.getStepExecution("listenForSampleReceived", parameterMap, false);
		} catch (BatchDaoDataRetrievalException e) {
			Assert.assertEquals(e.getMessage(), "More than one StepExecution object returned with given step name and parameter map");
			return;
		}
		Assert.fail("Expected an BatchDaoDataRetrievalException but got none");
	}
	
	
	/**
	 * Test extracting a job parameter given a step execution
	 */
	@Test(groups = "unit-tests", dependsOnMethods="testGettingStepExecutionNormalTest1")
	public void testGettingParametersFromStepExecution(){
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		
		Set<String> sampleIdStringSet = new HashSet<String>();
		sampleIdStringSet.add(SAMPLE_ID_1.toString());
		parameterMap.put(WaspJobParameters.SAMPLE_ID, sampleIdStringSet);
		
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(JOB_ID_1.toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		StepExecution stepExecution = null;
		try {
			stepExecution = jobExplorer.getStepExecution("listenForSampleReceived", parameterMap, false);
		} catch (BatchDaoDataRetrievalException e) {
			Assert.fail("Unable to get status");
		}
		String value = null;
		try{
			value = jobExplorer.getJobParameterValueByKey(stepExecution, SAMPLE_ID_KEY);
		} catch (ParameterValueRetrievalException e){
			Assert.fail("Caught unexpected ParameterValueRetrievalException: " + e.getMessage());
		}
		Assert.assertNotNull(value);
		Assert.assertEquals(value, SAMPLE_ID_1.toString());
	}
	
	/**
	 * Test attempting to extract a non-existent parameter from a StepExecution
	 */
	@Test(groups = "unit-tests", dependsOnMethods="testGettingStepExecutionNormalTest1")
	public void testGettingParametersFromStepExecutionNoKey(){
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		
		Set<String> sampleIdStringSet = new HashSet<String>();
		sampleIdStringSet.add(SAMPLE_ID_1.toString());
		parameterMap.put(WaspJobParameters.SAMPLE_ID, sampleIdStringSet);
		
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(JOB_ID_1.toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		StepExecution stepExecution = null;
		try {
			stepExecution = jobExplorer.getStepExecution("listenForSampleReceived", parameterMap, false);
		} catch (BatchDaoDataRetrievalException e) {
			Assert.fail("Unable to get status");
		}
		String value = null;
		try{
			value = jobExplorer.getJobParameterValueByKey(stepExecution, SAMPLE_ID_KEY_WRONG);
		} catch (ParameterValueRetrievalException e){
			logger.debug("Caught expected ParameterValueRetrievalException: " + e.getMessage());
			return;
		}
		Assert.fail("Didn't catch expected ParameterValueRetrievalException");
	}
	
	/**
	 * Test getting most recent StepExecution from list. Should be id's 6-8 matching the date but 8 is the highest id
	 */
	@Test(groups = "unit-tests", dependsOnMethods="testGettingStepExecutionNormalTest1")
	public void testGettingMostRecentStepExecution1(){
		StepExecution stepExecution = jobExplorer.getMostRecentlyStartedStepExecutionInList(jobExplorer.getStepExecutions());
		Assert.assertNotNull(stepExecution);
		Assert.assertEquals(stepExecution.getId(), new Long(8));
	}
	
	/**
	 * Test getting most recent StepExecution from list
	 */
	@Test(groups = "unit-tests", dependsOnMethods="testGettingStepExecutionNormalTest1")
	public void testGettingMostRecentStepExecution2(){
		StepExecution stepExecution = jobExplorer.getMostRecentlyStartedStepExecutionInList(jobExplorer.getStepExecutions("listenForExitCondition"));
		Assert.assertNotNull(stepExecution);
		Assert.assertEquals(stepExecution.getId(), new Long(6));
	}
	
	/**
	 * API extension testing. Testing access of state information via API extension. The test Batch db tables should contain two job instances
	 * Both of these should match the supplied jobId, so if the sampleId parameter is absent to distinguish them this test should throw
	 * an exception, but since only JobExecution 2 is in ExitStatus=UNKNOWN it should be ok
	 */
	@Test(groups = "unit-tests")
	public void testGettingJobExecutionNormalTest1(){
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(JOB_ID_1.toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		JobExecution jobExecution = null;
		try {
			jobExecution = jobExplorer.getJobExecution(parameterMap, false, ExitStatus.UNKNOWN);
		} catch (BatchDaoDataRetrievalException e) {
			Assert.fail("Got unexpected BatchDaoDataRetrievalException");
		}
		Assert.assertNotNull(jobExecution);
		Assert.assertEquals(jobExecution.getId(), new Long(2));
	}
	
	/**
	 * API extension testing. Testing getting all JobExecutions with WaspBatchExitStatus=COMPLETED. Should be 1
	 */
	@Test(groups = "unit-tests")
	public void testGettingJobExecutionNormalTest2(){
		List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(WaspBatchExitStatus.COMPLETED);
		Assert.assertNotNull(jobExecutions);
		Assert.assertEquals(jobExecutions.size(), 1); 
	}
	
	/**
	 * API extension testing. Testing getting all JobExecutions with a jobId parameter key
	 */
	@Test(groups = "unit-tests")
	public void testGettingJobExecutionNormalTest4(){
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add("*");
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(parameterMap, false);
		Assert.assertNotNull(jobExecutions);
		Assert.assertEquals(jobExecutions.size(), 2); 
	}
	
	/**
	 * API extension testing. Testing access of state information via API extension. The test Batch db tables should contain two wasp.sample.jobflow.v1 instances
	 * Both of these should match the supplied jobId, so if the sampleId parameter is absent to distinguish them this test should throw
	 * an exception.
	 */
	@Test(groups = "unit-tests")
	public void testGettingJobExecutionFailureTest1(){
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(JOB_ID_1.toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		try {
			JobExecution jobExecution = jobExplorer.getJobExecution("wasp.sample.jobflow.v1", parameterMap, false);
		} catch (BatchDaoDataRetrievalException e) {
			Assert.assertEquals(e.getMessage(), "More than one JobExecution object returned with given step name and parameter map");
			return;
		}
		Assert.fail("Expected an BatchDaoDataRetrievalException but got none");
	}
	
	/**
	 * Test extracting a job parameter given a job execution
	 */
	@Test(groups = "unit-tests", dependsOnMethods="testGettingJobExecutionNormalTest1")
	public void testGettingParametersFromJobExecution(){
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		
		Set<String> sampleIdStringSet = new HashSet<String>();
		sampleIdStringSet.add(SAMPLE_ID_1.toString());
		parameterMap.put(WaspJobParameters.SAMPLE_ID, sampleIdStringSet);
		
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(JOB_ID_1.toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		JobExecution jobExecution = null;
		try {
			jobExecution = jobExplorer.getJobExecution("wasp.sample.jobflow.v1", parameterMap, false);
		} catch (BatchDaoDataRetrievalException e) {
			Assert.fail("Unable to get status");
		}
		String value = null;
		try{
			value = jobExplorer.getJobParameterValueByKey(jobExecution, SAMPLE_ID_KEY);
		} catch (ParameterValueRetrievalException e){
			Assert.fail("Caught unexpected ParameterValueRetrievalException: " + e.getMessage());
		}
		Assert.assertNotNull(value);
		Assert.assertEquals(value, SAMPLE_ID_1.toString());
	}
	
	/**
	 * Test attempting to extract a non-existent parameter from a StepExecution
	 */
	@Test(groups = "unit-tests", dependsOnMethods="testGettingJobExecutionNormalTest1")
	public void testGettingParametersFromJobExecutionNoKey(){
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		
		Set<String> sampleIdStringSet = new HashSet<String>();
		sampleIdStringSet.add(SAMPLE_ID_1.toString());
		parameterMap.put(WaspJobParameters.SAMPLE_ID, sampleIdStringSet);
		
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(JOB_ID_1.toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		JobExecution JobExecution = null;
		try {
			JobExecution = jobExplorer.getJobExecution("wasp.sample.jobflow.v1", parameterMap, false);
		} catch (BatchDaoDataRetrievalException e) {
			Assert.fail("Unable to get status");
		}
		String value = null;
		try{
			value = jobExplorer.getJobParameterValueByKey(JobExecution, SAMPLE_ID_KEY_WRONG);
		} catch (ParameterValueRetrievalException e){
			logger.debug("Caught expected ParameterValueRetrievalException: " + e.getMessage());
			return;
		}
		Assert.fail("Didn't catch expected ParameterValueRetrievalException");
	}
	
	/**
	 * Test getting most recent JobExecution from list if the list is null
	 */
	@Test(groups = "unit-tests")
	public void testGettingMostRecentJobExecution(){
		StepExecution stepExecution = jobExplorer.getMostRecentlyStartedStepExecutionInList(null);
		Assert.assertNull(stepExecution);
	}
	
	
	
	 
	
}
