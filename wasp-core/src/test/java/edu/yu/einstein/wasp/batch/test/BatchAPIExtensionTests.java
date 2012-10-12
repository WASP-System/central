package edu.yu.einstein.wasp.batch.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.batch.core.extension.JobExplorerWasp;
import edu.yu.einstein.wasp.batch.exceptions.BatchDaoDataRetrievalException;
import edu.yu.einstein.wasp.batch.exceptions.ParameterValueRetrievalException;

@ContextConfiguration(locations={"classpath:batch/batch-test-context.xml"})

public class BatchAPIExtensionTests extends AbstractTestNGSpringContextTests {
	
	private JobExplorerWasp jobExplorer;
	
	@Autowired
	void setJobExplorer(JobExplorer jobExplorer){
		this.jobExplorer = (JobExplorerWasp) jobExplorer;
	}
	
	private final Logger logger = Logger.getLogger(BatchAPIExtensionTests.class);
	
	private final String JOB_ID_KEY = "jobId";
	
	private final String SAMPLE_ID_KEY = "sampleId";
	
	private final String SAMPLE_ID_KEY_WRONG = "wrongKey";
	
	private final Integer JOB_ID_1 = 1;
	
	private final Integer JOB_ID_2 = 2;

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
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put(SAMPLE_ID_KEY, SAMPLE_ID_1.toString());
		parameterMap.put(JOB_ID_KEY, JOB_ID_1.toString());
		StepExecution stepExecution = null;
		try {
			stepExecution = jobExplorer.getStepExecution("listenForSampleReceived", parameterMap, false);
		} catch (BatchDaoDataRetrievalException e) {
			Assert.fail("Unable to get status");
		}
		Assert.assertNotNull(stepExecution);
		Assert.assertEquals(stepExecution.getStatus(), BatchStatus.COMPLETED);
	}
	
	/**
	 * API extension testing. Testing access of state information via API extension.The test Batch db tables should contain two steps called 
	 * 'wasp.sample.step.listenForSampleReceived' with a Batch Status of Completed. The job parameters include both sample_id and job_id
	 * so if the 'exclusive' parameter is set to true, then nothing should be returned if only one of the two parameters is provided
	 */
	@Test(groups = "unit-tests")
	public void testGettingStepExecutionNormalTest2(){
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put(SAMPLE_ID_KEY, SAMPLE_ID_1.toString());
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
	 * 'wasp.sample.step.listenForJobApproved' one with a BatchStatus of COMPLETED (sample 2) and one with BatchStatus of STARTED (sample 1).
	 */
	@Test(groups = "unit-tests")
	public void testGettingStepExecutionNormalTest3(){
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put(SAMPLE_ID_KEY, SAMPLE_ID_1.toString());
		List<StepExecution> stepExecutions = jobExplorer.getStepExecutions("listenForJobApproved", parameterMap, false, BatchStatus.STARTED);
		Assert.assertTrue(stepExecutions.size() == 1); // expect to be STARTED
	}
	
	/**
	 * API extension testing. Testing access of state information via API extension.The test Batch db tables should contain two steps called 
	 * 'wasp.sample.step.listenForJobApproved' one with a BatchStatus of COMPLETED (sample 2) and one with BatchStatus of STARTED (sample 1).
	 */
	@Test(groups = "unit-tests")
	public void testGettingStepExecutionNormalTest4(){
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put(SAMPLE_ID_KEY, SAMPLE_ID_2.toString());
		List<StepExecution> stepExecutions = jobExplorer.getStepExecutions("listenForJobApproved", parameterMap, false, BatchStatus.STARTED);
		Assert.assertTrue(stepExecutions.size() == 0); //expect to be COMPLETE
	}
	
	/**
	 * API extension testing. Testing access of state information via API extension.The test Batch db tables should contain two steps called 
	 * 'wasp.sample.step.listenForSampleReceived' with a Batch Status of Completed.
	 * Both of these should match the supplied jobId, so if the sampleId parameter is absent to distinguish them this test should throw
	 * an exception.
	 */
	@Test(groups = "unit-tests")
	public void testGettingStepExecutionFailureTest1(){
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put(JOB_ID_KEY, JOB_ID_1.toString());
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
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put(SAMPLE_ID_KEY, SAMPLE_ID_1.toString());
		parameterMap.put(JOB_ID_KEY, JOB_ID_1.toString());
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
		Assert.assertEquals(value, SAMPLE_ID_1.toString());
	}
	
	/**
	 * Test attempting to extract a non-existent parameter from a StepExecution
	 */
	@Test(groups = "unit-tests", dependsOnMethods="testGettingStepExecutionNormalTest1")
	public void testGettingParametersFromStepExecutionNoKey(){
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put(SAMPLE_ID_KEY, SAMPLE_ID_1.toString());
		parameterMap.put(JOB_ID_KEY, JOB_ID_1.toString());
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
	 * API extension testing. Testing access of state information via API extension. The test Batch db tables should contain two job instances
	 * Both of these should match the supplied jobId, so if the sampleId parameter is absent to distinguish them this test should throw
	 * an exception, but since only JobExecution 2 is in ExitStatus=UNKNOWN it should be ok
	 */
	@Test(groups = "unit-tests")
	public void testGettingJobExecutionNormalTest1(){
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put(JOB_ID_KEY, JOB_ID_1.toString());
		JobExecution jobExecution = null;
		try {
			jobExecution = jobExplorer.getJobExecution(parameterMap, false, ExitStatus.UNKNOWN);
		} catch (BatchDaoDataRetrievalException e) {
			Assert.fail("Got unexpected BatchDaoDataRetrievalException");
		}
		Assert.assertEquals(jobExecution.getId(), new Long(2));
	}
	
	/**
	 * API extension testing. Testing access of state information via API extension. The test Batch db tables should contain two wasp.sample.jobflow.v1 instances
	 * Both of these should match the supplied jobId, so if the sampleId parameter is absent to distinguish them this test should throw
	 * an exception.
	 */
	@Test(groups = "unit-tests")
	public void testGettingJobExecutionFailureTest1(){
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put(JOB_ID_KEY, JOB_ID_1.toString());
		try {
			JobExecution jobExecution = jobExplorer.getJobExecution("wasp.sample.jobflow.v1", parameterMap, false);
		} catch (BatchDaoDataRetrievalException e) {
			Assert.assertEquals(e.getMessage(), "More than one JobExecution object returned with given step name and parameter map");
			return;
		}
		Assert.fail("Expected an BatchDaoDataRetrievalException but got none");
	}
	
	
	
	 
	
}
