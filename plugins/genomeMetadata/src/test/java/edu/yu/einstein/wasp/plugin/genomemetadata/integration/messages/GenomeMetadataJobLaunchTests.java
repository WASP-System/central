/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.genomemetadata.integration.messages;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.wasp.JobExplorerWasp;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

// The test context is created using the configuration files provided in the @ContextConfiguration locations list
// temp ban tests ContextConfiguration(locations={"/genomemetadata-test-launch-context.xml","/flows/genomemetadata.mainFlow.v1.xml"})

/**
 * TestNG Test of Launching and successful completion of the GenomeMetadataPlugin.FLOW_NAME batch job flow (defined in /flows/genomemetadata.mainFlow.v1.xml)
 * @author 
 * 
 */
public class GenomeMetadataJobLaunchTests { // extends AbstractTestNGSpringContextTests implements MessageHandler {
	
//	private final Logger logger = LoggerFactory.getLogger(GenomeMetadataJobLaunchTests.class);
//	
//	// define constants
//	private final Integer TEST_ID = 1;
//	private final long MESSAGE_TIMEOUT = 2000L; // ms
//	private final int MESSAGE_WAIT_INTERVAL = 50; // ms
//	
//	//@Autowired
//	private JobLauncher jobLauncher;
//	
//	private JobExplorerWasp jobExplorer;
//	
//	//@Autowired
//	void setJobExplorer(JobExplorer jobExplorer){
//		this.jobExplorer = (JobExplorerWasp) jobExplorer;
//	}
//	
//	// Wire up message channel for sending messages to remote message handling daemon
//	//@Autowired
//	@Qualifier("wasp.channel.remoting.outbound")
//	private DirectChannel outboundMessageChannel;
//	
//	// Wire up channel on which to receive messages broadcast from the plugin.
//	// Plugin-derived messages are ultimately broadcast on the 'wasp.channel.notification.default' channel
//	//@Autowired
//	@Qualifier("wasp.channel.notification.default")
//	private PublishSubscribeChannel listeningMessageChannel;
//	
//	private MessagingTemplate messagingTemplate;
//	
//	// list in which to store messages received
//	private List<Message<?>> receivedMessages = new ArrayList<>();
//	
//	/**
//	 * Code to execute before running any tests
//	 * @throws SecurityException
//	 * @throws NoSuchMethodException
//	 */
//	@BeforeClass 
//	private void setup() throws SecurityException, NoSuchMethodException{
//		// make sure we autowired everything ok
//		Assert.assertNotNull(jobLauncher);
//		Assert.assertNotNull(jobExplorer);
//		Assert.assertNotNull(outboundMessageChannel);
//		Assert.assertNotNull(listeningMessageChannel);
//		
//		// setup a messagingTemplate to send messages
//		messagingTemplate = new MessagingTemplate();
//		messagingTemplate.setReceiveTimeout(MESSAGE_TIMEOUT);
//		
//		// subscribe to the "wasp.channel.notification.default" broadcast channel
//		listeningMessageChannel.subscribe(this);
//	}
//	
//	/**
//	 * Code to execute after running all tests
//	 */
//	@AfterClass 
//	public void teardown(){
//		// unsubscribe from the "wasp.channel.notification.default" broadcast channel
//		listeningMessageChannel.unsubscribe(this);
//	}
//
//		
////	/**
////	 * This test involves sending a message to the remote wasp-daemon to initiate the GenomeMetadataPlugin.FLOW_NAME job flow. 
////	 * We check (from the response)	that this was successful, then verify that we recieve the two messages sent by the first and third steps of the job-flow 
////	 * in the correct order. 
////	 * Finally we check that the job execution exited with a success status of COMPLETED.
////	 */
////	@Test (groups = "genomemetadata-tests")
////	public void testSuccessfulGenomemetadataJobLaunch() throws Exception{
////		try{
////			Map<String, String> jobParameters = new HashMap<String, String>();
////			// The genomemetadata flow doesn't actually require any job parameters but we'll add one for demonstration purposes. 
////			// Any parameters supplied to a batch job are available from within steps
////			jobParameters.put(WaspJobParameters.TEST_ID, TEST_ID.toString());
////			BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( new BatchJobLaunchContext(GenomeMetadataPlugin.FLOW_NAME, jobParameters) );
////			Message<BatchJobLaunchContext> messageToSend = batchJobLaunchMessageTemplate.build();
////			logger.debug("testSuccessfulJobLaunch(): Sending message : "+messageToSend.toString());
////			Message<?> replyMessage = messagingTemplate.sendAndReceive(outboundMessageChannel, messageToSend);
////			
////			// verify message successfully sent
////			if (replyMessage == null)
////				Assert.fail("testSuccessfulJobLaunch(): Failed to send message " + messageToSend.toString() + " within timeout period");
////			if (BatchJobLaunchContext.class.isInstance(replyMessage.getPayload()))
////				Assert.fail("testSuccessfulJobLaunch(): Message bouced");
////			if (replyMessage.getHeaders().containsKey(WaspTask.EXCEPTION))
////				Assert.fail("testSuccessfulJobLaunch(): Failed to launch job. Returned message: " + replyMessage.toString());
////			Assert.assertEquals(replyMessage.getPayload(), WaspStatus.COMPLETED);
////			
////			// verify that batch flow ran and completed normally. 
////			// The GenomeMetadataPlugin.FLOW_NAME job flow sends two messages which we will catch and verify
////			int repeats = 0;
////			final int EXPECTED_MESSAGE_COUNT = 2; 
////			while (receivedMessages.size() < EXPECTED_MESSAGE_COUNT && repeats++ < (MESSAGE_TIMEOUT / MESSAGE_WAIT_INTERVAL) )
////				Thread.sleep(MESSAGE_WAIT_INTERVAL); // allow time for spring batch job execution and message sending
////			
////			// Check the receivedMessages list for receiving of the two messages sent by the GenomeMetadataPlugin.FLOW_NAME job flow.
////			Assert.assertEquals(receivedMessages.size(), EXPECTED_MESSAGE_COUNT);
////			Assert.assertEquals(receivedMessages.get(0).getPayload(), WaspStatus.STARTED);
////			Assert.assertEquals(receivedMessages.get(1).getPayload(), WaspStatus.COMPLETED);
////			
////			// get the JobExecution for the job we just executed and verify that it completed successfully.
////			// We can use the jobExplorer to get this.
////			ExitStatus jobExecutionStatus = jobExplorer.getMostRecentlyStartedJobExecutionInList(jobExplorer.getJobExecutions(GenomeMetadataPlugin.FLOW_NAME)).getExitStatus();
////			Assert.assertTrue(jobExecutionStatus.isCompleted());
////		} catch (Exception e){
////			logger.error("Caught unexpected exception: " + e.getLocalizedMessage());
////			throw e; // re-throw the exception
////		}
////	}
////	
//	/**
//	 * Asynchronously handles any messages received on channels this class is subscribed to in real time 
//	 * (in this case only the "wasp.channel.notification.default" channel is monitored).
//	 */
//	@Override
//	public void handleMessage(Message<?> messageIn) throws MessagingException {
//		logger.debug("Message received by handleMessage(): " + messageIn.toString());
//		// We can use static methods of the SimpleGenomeMetadataStatusMessageTemplate to check if the message is one we might be interested in.
//		// In this case we just test whether the message was generated by the SimpleGenomeMetadataStatusMessageTemplate.
//		if (SimpleGenomeMetadataStatusMessageTemplate.isMessageOfCorrectType(messageIn))
//			receivedMessages.add(messageIn); // keep GenomeMetadata messages only
//	}
	
}
