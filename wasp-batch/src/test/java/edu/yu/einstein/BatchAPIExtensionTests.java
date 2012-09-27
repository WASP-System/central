package edu.yu.einstein;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.batch.core.extension.JobExplorerWasp;
import edu.yu.einstein.wasp.exceptions.BatchDaoDataRetrievalException;

@ContextConfiguration(locations={"classpath:test-launch-context.xml"})

public class BatchAPIExtensionTests extends AbstractTestNGSpringContextTests {
	
	private JobExplorerWasp jobExplorer;
	
	@Autowired
	void setJobExplorer(JobExplorer jobExplorer){
		this.jobExplorer = (JobExplorerWasp) jobExplorer;
	}
	
	private final Logger logger = Logger.getLogger(BatchAPIExtensionTests.class);
	
	private final String JOB_ID_KEY = "jobId";
	
	private final String SAMPLE_ID_KEY = "sampleId";
	
	private final Integer JOB_ID = 1;

	private final Integer SAMPLE_ID = 1;

	
	
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
	public void testGettingStateWithAPI(){
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put(SAMPLE_ID_KEY, SAMPLE_ID.toString());
		parameterMap.put(JOB_ID_KEY, JOB_ID.toString());
		StepExecution stepExecution = null;
		try {
			stepExecution = jobExplorer.getStepExecutionByStepNameAndParameterMap("listenForSampleReceived", parameterMap);
		} catch (BatchDaoDataRetrievalException e) {
			Assert.fail("Unable to get status");
		}
		Assert.assertNotNull(stepExecution);
		Assert.assertEquals(stepExecution.getStatus(), BatchStatus.COMPLETED);
	}
	
	/**
	 * API extension testing. Testing access of state information via API extension.The test Batch db tables should contain two steps called 
	 * 'wasp.sample.step.listenForSampleReceived' with a Batch Status of Completed.
	 * Both of these should match the supplied jobId, so if the sampleId parameter is absent to distinguish them this test should throw
	 * an exception.
	 */
	@Test(groups = "unit-tests")
	public void testGettingStateWithAPIFailureOnMoreThanOneResult(){
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put(JOB_ID_KEY, JOB_ID.toString());
		try {
			StepExecution stepExecution = jobExplorer.getStepExecutionByStepNameAndParameterMap("listenForSampleReceived", parameterMap);
		} catch (BatchDaoDataRetrievalException e) {
			Assert.assertEquals(e.getMessage(), "More than one StepExecution object returned with given step name and parameter map");
			return;
		}
		Assert.fail("Expected an BatchDaoDataRetrievalException but got none");
	}
	
}
