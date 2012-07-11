package edu.yu.einstein;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
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
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.springframework.batch.core.JobParameter;

import edu.yu.einstein.wasp.messages.WaspRunStatus;


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
			Job runJob = context.getBean("runJob", Job.class); // get the 'runJob' job from the context
			Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
			parameterMap.put( PU_KEY, new JobParameter(PU_ID.toString()) );
			jobLauncher.run(runJob, new JobParameters(parameterMap));
			logger.debug("Going to sleep 2s Whilst awaiting first message...");
			try{
				Thread.sleep(2000);
			} catch(InterruptedException e){
				logger.debug("Awoken by interrupt.");
			}
			if (message == null)
				Assert.fail("Message was not received");
		
			Assert.assertTrue(message.getHeaders().containsKey(PU_KEY));
			Assert.assertNotNull(message.getHeaders().get(PU_KEY));
			Assert.assertEquals(message.getHeaders().get(PU_KEY), PU_ID);
			Assert.assertEquals(WaspRunStatus.class, message.getPayload().getClass());
			Assert.assertEquals(message.getPayload(), WaspRunStatus.STARTED);
			logger.debug("Going to sleep 8s Whilst awaiting second message...");
			try{
				Thread.sleep(8000);
			} catch(InterruptedException e){
				logger.debug("Awoken by interrupt.");
			}
			if (message == null)
				Assert.fail("Message was not received");
		
			Assert.assertTrue(message.getHeaders().containsKey(PU_KEY));
			Assert.assertNotNull(message.getHeaders().get(PU_KEY));
			Assert.assertEquals(message.getHeaders().get(PU_KEY), PU_ID);
			Assert.assertEquals(WaspRunStatus.class, message.getPayload().getClass());
			Assert.assertEquals(message.getPayload(), WaspRunStatus.COMPLETED);
			waspRunPublishSubscribeChannel.unsubscribe(this);
		} catch (JobExecutionAlreadyRunningException e){
			Assert.fail("Caught JobExecutionAlreadyRunningException exception: "+e.getMessage());
		}
		catch (JobRestartException e){
			Assert.fail("Caught JobRestartException exception: "+e.getMessage());
		}
		catch (JobInstanceAlreadyCompleteException e){
			Assert.fail("Caught JobInstanceAlreadyCompleteException exception: "+e.getMessage());
		}
		catch (JobParametersInvalidException e){
			Assert.fail("Caught JobParametersInvalidException exception: "+e.getMessage());
		}
		
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.debug("Message recieved by handleMessage(): "+message.toString());
		this.message = message; 
	}
	
}
