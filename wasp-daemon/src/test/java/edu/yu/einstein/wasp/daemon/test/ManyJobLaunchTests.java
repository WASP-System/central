package edu.yu.einstein.wasp.daemon.test;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;

// @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
// @TestExecutionListeners({DirtiesContextTestExecutionListener.class})
@ContextConfiguration(locations = { "/daemon-test-manyJob.xml", "/daemon-test-launch-context.xml", "/dummyBatchJobFlow.xml" })
public class ManyJobLaunchTests extends BatchDatabaseIntegrationTest implements MessageHandler {

    @Autowired
    private MessageChannelRegistry messageChannelRegistry;

    @Autowired
    private MessageChannelRegistry channelRegistry;

    private SubscribableChannel listeningChannel;
    private SubscribableChannel abortChannel;

    private final Logger logger = LoggerFactory.getLogger(ManyJobLaunchTests.class);

    private MessagingTemplate messagingTemplate;

    private final String BATCH_JOB_NAME = "test.launchManyJob";
   
    private final String OUTBOUND_MESSAGE_CHANNEL = "wasp.channel.remoting.outbound";
    
    private volatile Message<?> message = null;

    @BeforeClass
    @Override
    protected void setup() {
        super.setup();
        Assert.assertNotNull(messageChannelRegistry);
        listeningChannel = channelRegistry.getChannel("wasp.channel.notification.analysis", SubscribableChannel.class);
        listeningChannel.subscribe(this); // register as a message handler on
                                          // the listeningChannel
        abortChannel = channelRegistry.getChannel("wasp.channel.notification.abort", SubscribableChannel.class);
        abortChannel.subscribe(this); // register as a message handler on the
                                      // listeningChannel
        messagingTemplate = new MessagingTemplate();
        messagingTemplate.setReceiveTimeout(2000);
    }

    @BeforeMethod
    protected void beforeMethodSetup() throws Exception {
        super.cleanDB();
        listeningChannel = channelRegistry.getChannel("wasp.channel.notification.analysis", SubscribableChannel.class);
        listeningChannel.subscribe(this);
    }

    @AfterMethod
    protected void afterMethodTeardown() {
        message = null;
        listeningChannel.unsubscribe(this);
        abortChannel.unsubscribe(this);
        stopRunningJobExecutions();
    }
    
    @AfterClass 
    private void tearDown(){
            listeningChannel.unsubscribe(this);
            abortChannel.unsubscribe(this);
    }

    /**
     * Test getting correct reply from BatchJobLaunchServiceImpl service
     * activator after sending a message to start a batch job
     */
    @Test(groups = "unit-tests-batch-integration")
    public void testSuccessfulManyJobLaunch() throws Exception {
        Map<String, String> jobParameters = new HashMap<String, String>();
        jobParameters.put("testMethod", "sleep");
        jobParameters.put("timestamp", String.valueOf(System.currentTimeMillis()));
        BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate(
                new BatchJobLaunchContext(BATCH_JOB_NAME, jobParameters));
        Message<BatchJobLaunchContext> messageToSend = batchJobLaunchMessageTemplate.build();
        logger.debug("testSuccessfulJobLaunch(): Sending message : " + messageToSend.toString());
        Message<?> replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class),
                messageToSend);
        if (replyMessage == null)
            Assert.fail("testSuccessfulManyJobLaunch(): Failed to send message " + messageToSend.toString() + " within timeout period");
        if (BatchJobLaunchContext.class.isInstance(replyMessage.getPayload()))
            Assert.fail("testSuccessfulManyJobLaunch(): Message bouced");
        if (replyMessage.getHeaders().containsKey(WaspTask.EXCEPTION))
            Assert.fail("testSuccessfulManyJobLaunch(): Failed to launch job. Returned message: " + replyMessage.toString());
        Assert.assertEquals(replyMessage.getPayload(), WaspStatus.COMPLETED);
        
        
        int n = 0;
        while(true) {
            if (message != null) {
                if (message.getHeaders().get("task") == "notifyStatus" && message.getHeaders().get("messagetype") == "analysis") {
                    Assert.assertEquals(message.getPayload(), WaspStatus.COMPLETED);
                    logger.debug("test successful");
                    break;
                }
            }
            Thread.sleep(200);
            if (n++ > 30)
                Assert.fail("too long for response");
        }
    }
    
    /**
     * Test getting correct reply from BatchJobLaunchServiceImpl service
     * activator after sending a message to start a batch job
     */
    @Test(groups = "unit-tests-batch-integration")
    public void testAbandoningManyJobLaunch() throws Exception {
        Map<String, String> jobParameters = new HashMap<String, String>();
        jobParameters.put("testMethod", "abandon");
        jobParameters.put("timestamp", String.valueOf(System.currentTimeMillis()));
        BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate(
                new BatchJobLaunchContext(BATCH_JOB_NAME, jobParameters));
        Message<BatchJobLaunchContext> messageToSend = batchJobLaunchMessageTemplate.build();
        logger.debug("testSuccessfulJobLaunch(): Sending message : " + messageToSend.toString());
        Message<?> replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class),
                messageToSend);
        if (replyMessage == null)
            Assert.fail("testAbandoningManyJobLaunch(): Failed to send message " + messageToSend.toString() + " within timeout period");
        if (BatchJobLaunchContext.class.isInstance(replyMessage.getPayload()))
            Assert.fail("testAbandoningManyJobLaunch(): Message bouced");
        if (replyMessage.getHeaders().containsKey(WaspTask.EXCEPTION))
            Assert.fail("testAbandoningManyJobLaunch(): Failed to launch job. Returned message: " + replyMessage.toString());
        Assert.assertEquals(replyMessage.getPayload(), WaspStatus.COMPLETED);
        
        int n = 0;
        while(true) {
            if (message != null) {
                if (message.getHeaders().get("task") == "notifyStatus" && message.getHeaders().get("messagetype") == "analysis") {
                    Assert.assertEquals(message.getPayload(), WaspStatus.FAILED);
                    logger.debug("test successful");
                    break;
                }
            }
            Thread.sleep(200);
            if (n++ > 30)
                Assert.fail("too long for response");
        }
    }
    
    /**
     * Test getting correct reply from BatchJobLaunchServiceImpl service
     * activator after sending a message to start a batch job. No many jobs will be launched in this test
     */
    @Test(groups = "unit-tests-batch-integration")
    public void testNoJobLaunch() throws Exception {
        Map<String, String> jobParameters = new HashMap<String, String>();
        jobParameters.put("testMethod", "none");
        jobParameters.put("timestamp", String.valueOf(System.currentTimeMillis()));
        BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate(
                new BatchJobLaunchContext(BATCH_JOB_NAME, jobParameters));
        Message<BatchJobLaunchContext> messageToSend = batchJobLaunchMessageTemplate.build();
        logger.debug("testSuccessfulJobLaunch(): Sending message : " + messageToSend.toString());
        Message<?> replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class),
                messageToSend);
        if (replyMessage == null)
            Assert.fail("testSuccessfulManyJobLaunch(): Failed to send message " + messageToSend.toString() + " within timeout period");
        if (BatchJobLaunchContext.class.isInstance(replyMessage.getPayload()))
            Assert.fail("testSuccessfulManyJobLaunch(): Message bouced");
        if (replyMessage.getHeaders().containsKey(WaspTask.EXCEPTION))
            Assert.fail("testSuccessfulManyJobLaunch(): Failed to launch job. Returned message: " + replyMessage.toString());
        Assert.assertEquals(replyMessage.getPayload(), WaspStatus.COMPLETED);
        
        
        int n = 0;
        while(true) {
            if (message != null) {
                if (message.getHeaders().get("task") == "notifyStatus" && message.getHeaders().get("messagetype") == "analysis") {
                    Assert.assertEquals(message.getPayload(), WaspStatus.COMPLETED);
                    logger.debug("test successful");
                    break;
                }
            }
            Thread.sleep(200);
            if (n++ > 30)
                Assert.fail("too long for response");
        }
    }

    @Override
    public synchronized void handleMessage(Message<?> message) throws MessagingException {
        logger.debug("Message received by handleMessage(): " + message.toString());
        if (message.getHeaders().get("task") != "notifyStatus" || message.getHeaders().get("messagetype") != "analysis")
            return;
        this.message = message;
    }
    
}
