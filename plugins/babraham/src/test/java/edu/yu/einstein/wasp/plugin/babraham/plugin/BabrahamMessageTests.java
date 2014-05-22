package edu.yu.einstein.wasp.plugin.babraham.plugin;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.FileStatusMessageTemplate;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.plugin.babraham.integration.endpoints.BabrahamFastqMessageSplitter;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;
import edu.yu.einstein.wasp.service.FileService;

@ContextConfiguration(locations={"/babraham-test-context.xml"})

public class BabrahamMessageTests extends AbstractTestNGSpringContextTests implements MessageHandler{
	
	// mockRunService and mockRunDao are mocked in context to keep Spring happy when resolving dependencies on bean creation
	// but MUST be re-mocked here (not @Autowired in) otherwise there is autowiring issues with dependencies such as Entitymanager etc.
		
	@Mock private FileService mockFileService; 
	
	@Mock private FastqService mockFastqService;
	
	
	@Autowired
	@Qualifier("wasp.channel.notification.file")
	private MessageChannel outMessageChannel;
	
	@Autowired
	@Qualifier("wasp.channel.batch")
	private SubscribableChannel inMessageChannel;
	
	private MessagingTemplate messagingTemplate;
	
	@Autowired
	private BabrahamFastqMessageSplitter splitter;
	
	private final Logger logger = LoggerFactory.getLogger(BabrahamMessageTests.class);
	
	private final int ID = 1;
	
	private FileType fastq;
	
	private FileType bam;
	
	private FileGroup fastqFg;
	
	private List<Message<?>> messages = new ArrayList<>();
	
	@BeforeClass
	public void beforeClass(){
		MockitoAnnotations.initMocks(this);
		Assert.assertNotNull(mockFileService);
		Assert.assertNotNull(mockFastqService);
		Assert.assertNotNull(splitter);
		
		messagingTemplate = new MessagingTemplate();
		messagingTemplate.setReceiveTimeout(2000);
			
		// add mocks to splitter (replacing Autowired versions)
		// could also use ReflectionTestUtils.setField(splitter, "runService", mockRunService) - essential if no setters
		ReflectionTestUtils.setField(splitter, "fileService", mockFileService);
		ReflectionTestUtils.setField(splitter, "fastqService", mockFastqService);
		
		fastq = new FileType();
		fastq.setId(ID);
		fastq.doUpdate(); // ensure uuid set
		
		bam = new FileType();
		bam.setId(ID+1);
		bam.doUpdate(); // ensure uuid set
		
		fastqFg = new FileGroup();
		fastqFg.setId(ID);
		fastqFg.setFileType(fastq);
		inMessageChannel.subscribe(this);
	}
	
	@AfterClass
	public void afterClass(){
		inMessageChannel.unsubscribe(this);
	}
	
	@BeforeMethod
	public void beforeMethod() {
		//
	}
	
	@Test (groups = "unit-tests-batch-integration")
	public void normalFastQCCreatedTest() throws Exception{
		try {
			// add mocks to splitter (replacing Autowired versions)
			// could also use ReflectionTestUtils.setField(splitter, "runService", mockRunService) - essential if no setters
			PowerMockito.when(mockFastqService.getFastqFileType()).thenReturn(fastq);
			PowerMockito.when(mockFileService.getFileGroupById(ID)).thenReturn(fastqFg);
			
			FileStatusMessageTemplate template = new FileStatusMessageTemplate(ID);
			template.setStatus(WaspStatus.CREATED);
			Message<WaspStatus> fastqCreatedMessage = template.build();
			logger.info("runSuccessTest(): Sending message via 'outbound rmi gateway': "+fastqCreatedMessage.toString());
			Message<?> replyMessage = messagingTemplate.sendAndReceive(outMessageChannel, fastqCreatedMessage);
			if (replyMessage != null)
				logger.debug("testJobApproved(): Got reply message: "+ replyMessage.toString());
			int repeats = 0;
			while (messages.size() < 2 && repeats++ < 10)
				Thread.sleep(50);
			Assert.assertEquals(messages.size(), 2);
		} catch (Exception e){
			// caught an unexpected exception
			e.printStackTrace();
			Assert.fail("Caught Exception: "+e.getMessage());
		}
					
	}
	
	@Test (groups = "unit-tests-batch-integration")
	public void NotFastQFileTest() throws Exception{
		try {
			// add mocks to splitter (replacing Autowired versions)
			// could also use ReflectionTestUtils.setField(splitter, "runService", mockRunService) - essential if no setters
			PowerMockito.when(mockFastqService.getFastqFileType()).thenReturn(bam);
			PowerMockito.when(mockFileService.getFileGroupById(ID)).thenReturn(fastqFg);
			FileStatusMessageTemplate template = new FileStatusMessageTemplate(ID);
			template.setStatus(WaspStatus.CREATED);
			Message<WaspStatus> fastqCreatedMessage = template.build();
			logger.info("runSuccessTest(): Sending message via 'outbound rmi gateway': "+fastqCreatedMessage.toString());
			Message<?> replyMessage = messagingTemplate.sendAndReceive(outMessageChannel, fastqCreatedMessage);
			if (replyMessage != null)
				logger.debug("testJobApproved(): Got reply message: "+ replyMessage.toString());
			int repeats = 0;
			while (messages.size() < 2 && repeats++ < 10)
				Thread.sleep(50);
			Assert.assertEquals(messages.size(), 0);
		} catch (Exception e){
			// caught an unexpected exception
			e.printStackTrace();
			Assert.fail("Caught Exception: "+e.getMessage());
		}
					
	}
	
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		BatchJobLaunchContext launchContext = (BatchJobLaunchContext) message.getPayload();
		logger.debug("Message recieved by handleMessage(): " + message.toString() + 
				" with payload: [job name='" + launchContext.getJobName() + "', parameters=(" + launchContext.getJobParameters().toString() + ")]" );
		messages.add(message);
	}

}
