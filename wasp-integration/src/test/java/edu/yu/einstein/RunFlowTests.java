package edu.yu.einstein;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
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
import org.springframework.batch.core.JobParameter;

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
	
	private final Integer PU_ID = 1;
	private final String PU_KEY = "platformUnitId";
	private final Integer RUN_ID = 10;
	private final String RUN_KEY = "runId";
	
	@Test (groups = "unit-tests")
	public void testJobLauncher() {
		Assert.assertNotNull(jobLauncher);
	}
	
	@Test (groups = "unit-tests", dependsOnMethods = "testJobLauncher")
	public void testRunJob() {
		try{
			// listen in on the waspRunPublishSubscribeChannel for messages
			SubscribableChannel waspRunPublishSubscribeChannel = context.getBean("waspRunPublishSubscribeChannel", SubscribableChannel.class);
			waspRunPublishSubscribeChannel.subscribe(this); // register as a message handler on the waspRunPublishSubscribeChannel
			
			// setup job execution for the 'runJob' job
			Job runJob = context.getBean("runJob", Job.class); // get the 'runJob' job from the context
			Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
			parameterMap.put( PU_KEY, new JobParameter(PU_ID.toString()) );
			parameterMap.put( RUN_KEY, new JobParameter(RUN_ID.toString()) );
			JobExecution jobExecution = jobLauncher.run(runJob, new JobParameters(parameterMap));
			
			// Check 'STARTED' message received
			logger.debug("Going to sleep 2s Whilst awaiting first message...");
			try{
				Thread.sleep(2000);
			} catch(InterruptedException e){
				logger.debug("Awoken by interrupt.");
			}
			if (message == null)
				Assert.fail("'Start' message was not received");
			
			// check headers as expected
			Assert.assertTrue(message.getHeaders().containsKey(PU_KEY));
			Assert.assertEquals(message.getHeaders().get(PU_KEY), PU_ID);
			Assert.assertTrue(message.getHeaders().containsKey(RUN_KEY));
			Assert.assertEquals(message.getHeaders().get(RUN_KEY), RUN_ID);
			
			// check payload as expected
			Assert.assertEquals(WaspRunStatus.class, message.getPayload().getClass());
			Assert.assertEquals(message.getPayload(), WaspRunStatus.STARTED);
			
			// Check 'COMPLETED' message received
			logger.debug("Going to sleep 8s Whilst awaiting second message...");
			try{
				Thread.sleep(8000);
			} catch(InterruptedException e){
				logger.debug("Awoken by interrupt.");
			}
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
	
	@Test (groups = "unit-tests", dependsOnMethods = "testJobLauncher")
	public void testRunLibraryJob() {
		try{
			// get a ref to the waspRunPriorityChannel to send test messages
			PollableChannel waspRunPriorityChannel = context.getBean("waspRunPriorityChannel", PollableChannel.class);
			
			// setup job execution for the  'runLibraryJob' job
			Job runJob = context.getBean("runLibraryJob", Job.class); // get the 'runLibraryJob' job from the context
			Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
			parameterMap.put( PU_KEY, new JobParameter(PU_ID.toString()) );
			JobExecution jobExecution = jobLauncher.run(runJob, new JobParameters(parameterMap));
			try{
				Thread.sleep(2000);
			} catch(InterruptedException e){
				logger.debug("Awoken by interrupt.");
			}
			// send run started message
			Message<WaspRunStatus> message =  WaspRunStatusMessage.build(RUN_ID, PU_ID, WaspRunStatus.STARTED);
			logger.debug("Sending message via 'waspRunPriorityChannel': "+message.toString());
			waspRunPriorityChannel.send(message);
			
			// send run completed message
			message =  WaspRunStatusMessage.build(RUN_ID, PU_ID, WaspRunStatus.COMPLETED);
			logger.debug("Sending message via 'waspRunPriorityChannel': "+message.toString());
			waspRunPriorityChannel.send(message);
			
			// Delay to allow message receiving and transitions
			try{
				Thread.sleep(40000);
			} catch(InterruptedException e){
				logger.debug("Awoken by interrupt.");
			}
			// check BatchStatus is as expected
			Assert.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
			
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
