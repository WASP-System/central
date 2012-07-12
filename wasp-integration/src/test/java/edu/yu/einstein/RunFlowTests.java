package edu.yu.einstein;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.core.PollableChannel;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.messages.WaspRunStatus;
import edu.yu.einstein.wasp.messages.WaspRunStatusMessage;


@ContextConfiguration(locations={"classpath:launch-context.xml"})
public class RunFlowTests extends AbstractTestNGSpringContextTests implements MessageHandler {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	ApplicationContext context;
	
	private final Logger logger = Logger.getLogger(RunFlowTests.class);
	
	private Message<?> message = null;
	
	private final Integer PU_ID1 = 1;
	private final Integer PU_ID2 = 2; // need more than one if running the same flow more than once due to re-run constraints in Batch
	private final String PU_KEY = "platformUnitId";
	private final Integer RUN_ID = 10;
	private final String RUN_KEY = "runId";
	
	/**
	 * Verifies that autowired objects are ok
	 */
	@Test (groups = "unit-tests")
	public void testAutowiringOk() {
		Assert.assertNotNull(jobLauncher);
		Assert.assertNotNull(context);
	}
	
	/**
	 * This test tests the runFlow.
	 * The method sets up a waspRunPublishSubscribeChannel and listens on it. it then launches the runFlowJob.
	 * The method verifies that a WaspRunStatus.STARTED is sent by the flow logic and a WaspRunStatus.COMPLETED 8s later.
	 * The headers and payload of the received messages are checked as is the order received.
	 */
	@Test (groups = "unit-tests", dependsOnMethods = "testAutowiringOk")
	public void testRunJob() {
		try{
			// listen in on the waspRunPublishSubscribeChannel for messages
			SubscribableChannel waspRunPublishSubscribeChannel = context.getBean("waspRunPublishSubscribeChannel", SubscribableChannel.class);
			waspRunPublishSubscribeChannel.subscribe(this); // register as a message handler on the waspRunPublishSubscribeChannel
			
			// setup job execution for the 'runJob' job
			Job runJob = context.getBean("runJob", Job.class); // get the 'runJob' job from the context
			Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
			parameterMap.put( PU_KEY, new JobParameter(PU_ID1.toString()) );
			parameterMap.put( RUN_KEY, new JobParameter(RUN_ID.toString()) );
			JobExecution jobExecution = jobLauncher.run(runJob, new JobParameters(parameterMap));
			
			// Check 'STARTED' message received
			// Going to sleep 2s Whilst awaiting first message
			Thread.sleep(2000);
			
			if (message == null)
				Assert.fail("'Start' message was not received");
			
			// check headers as expected
			Assert.assertTrue(message.getHeaders().containsKey(PU_KEY));
			Assert.assertEquals(message.getHeaders().get(PU_KEY), PU_ID1);
			Assert.assertTrue(message.getHeaders().containsKey(RUN_KEY));
			Assert.assertEquals(message.getHeaders().get(RUN_KEY), RUN_ID);
			
			// check payload as expected
			Assert.assertEquals(WaspRunStatus.class, message.getPayload().getClass());
			Assert.assertEquals(message.getPayload(), WaspRunStatus.STARTED);
			
			// Check 'COMPLETED' message received
			// Going to sleep 8s Whilst awaiting second message
			Thread.sleep(8000);
			
			if (message == null)
				Assert.fail("'Complete' message was not received");
			
			// check payload as expected (don't bother checking headers this time around)
			Assert.assertEquals(message.getPayload(), WaspRunStatus.COMPLETED);
			waspRunPublishSubscribeChannel.unsubscribe(this);
			
			// check BatchStatus is as expected
			Assert.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
		} catch (Exception e){
			// caught an unexpected exception
			Assert.fail("Caught Exception: "+e.getMessage());
		}
	}
	
	/**
	 * This test test the runLibraryFlow
	 * Test With Expected messages. Send a WaspRunStatus.STARTED and WaspRunStatus.COMPLETED message. The STARTED one has higher priority so
	 * should arrive first followed by the COMPLETE message 5s later (due to the poller on the bridge between waspRunPriorityChannel and waspRunPublishSubscribeChannel).
	 */
	@Test (groups = "unit-tests", dependsOnMethods = "testAutowiringOk")
	public void testRunLibraryJob() {
		try{
			// get a ref to the waspRunPriorityChannel to send test messages
			PollableChannel waspRunPriorityChannel = context.getBean("waspRunPriorityChannel", PollableChannel.class);
			
			// setup job execution for the  'runLibraryJob' job
			Job runJob = context.getBean("runLibraryJob", Job.class); // get the 'runLibraryJob' job from the context
			Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
			parameterMap.put( PU_KEY, new JobParameter(PU_ID1.toString()) );
			JobExecution jobExecution = jobLauncher.run(runJob, new JobParameters(parameterMap));
			
			// Short delay before sending messages to allow flow setup and subscribing to waspRunPublishSubscribeChannel
			Thread.sleep(1000);
				
			// send run completed message
			message =  WaspRunStatusMessage.build(RUN_ID, PU_ID1, WaspRunStatus.COMPLETED);
			logger.debug("Sending message via 'waspRunPriorityChannel': "+message.toString());
			waspRunPriorityChannel.send(message);
			
			// send run started message
			Message<WaspRunStatus> message =  WaspRunStatusMessage.build(RUN_ID, PU_ID1, WaspRunStatus.STARTED);
			logger.debug("Sending message via 'waspRunPriorityChannel': "+message.toString());
			waspRunPriorityChannel.send(message);
			
			// Delay to allow message receiving and transitions
			int repeat = 0;
			while (jobExecution.getStatus().isLessThanOrEqualTo(BatchStatus.STARTED) && repeat < 40){
				Thread.sleep(500);
				repeat++;
			}
			
			// check BatchStatus is as expected
			Assert.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
			
		} catch (Exception e){
			// caught an unexpected exception
			Assert.fail("Caught Exception: "+e.getMessage());
		}
	}
	
	/**
	 * This is a failure test for the runLibraryFlow
	 * Test With Unexpected message. Send a WaspRunStatus.COMPLETED message. 
	 * As a STARTED message is expected first it should fail
	 */
	@Test (groups = "unit-tests", dependsOnMethods = "testAutowiringOk")
	public void testRunLibraryJobFail() {
		try{
			// get a ref to the waspRunPriorityChannel to send test messages
			PollableChannel waspRunPriorityChannel = context.getBean("waspRunPriorityChannel", PollableChannel.class);
			
			// setup job execution for the  'runLibraryJob' job
			Job runJob = context.getBean("runLibraryJob", Job.class); // get the 'runLibraryJob' job from the context
			Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
			parameterMap.put( PU_KEY, new JobParameter(PU_ID2.toString()) );
			JobExecution jobExecution = jobLauncher.run(runJob, new JobParameters(parameterMap));
			
			// Short delay before sending messages to allow flow setup and subscribing to waspRunPublishSubscribeChannel
			Thread.sleep(1000);
				
			// send run completed message
			message =  WaspRunStatusMessage.build(RUN_ID, PU_ID2, WaspRunStatus.COMPLETED);
			logger.debug("Sending message via 'waspRunPriorityChannel': "+message.toString());
			waspRunPriorityChannel.send(message);
			
			// Delay to allow message receiving and transitions
			int repeat = 0;
			while (jobExecution.getStatus().isLessThanOrEqualTo(BatchStatus.STARTED) && repeat < 40){
				Thread.sleep(500);
				repeat++;
			}
			
			// check BatchStatus is as expected
			Assert.assertEquals(jobExecution.getStatus(), BatchStatus.FAILED);
			
		} catch (Exception e){
			// caught an unexpected exception
			Assert.fail("Caught Exception: "+e.getMessage());
		}
	}
	

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.debug("Message recieved by handleMessage(): "+message.toString());
		this.message = message; 
	}
	
}
