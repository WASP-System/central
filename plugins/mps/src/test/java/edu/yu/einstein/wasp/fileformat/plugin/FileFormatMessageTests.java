package edu.yu.einstein.wasp.fileformat.plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.RunStatusMessageTemplate;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.FastqFileTypeAttribute;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;
import edu.yu.einstein.wasp.plugin.mps.integration.endpoints.RunSuccessFastqcSplitter;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;

@ContextConfiguration(locations={"/fileformat-test-context.xml"})

public class FileFormatMessageTests extends AbstractTestNGSpringContextTests implements MessageHandler {
	
	// mockRunService and mockRunDao are mocked in context to keep Spring happy when resolving dependencies on bean creation
	// but MUST be re-mocked here (not @Autowired in) otherwise there is autowiring issues with dependencies such as Entitymanager etc.
	
	@Mock private SampleService mockSampleService; 
	
	@Mock private RunService mockRunService; 
	
	@Mock private RunDao mockRunDao;
	
	@Mock private FastqService mockFastqService;
	
	@Mock private FileService mockFileService;
	
	@Autowired
	@Qualifier("wasp.channel.run.success")
	private DirectChannel outMessageChannel;
	
	@Autowired
	@Qualifier("wasp.channel.batch")
	private PublishSubscribeChannel inMessageChannel;
	
	private MessagingTemplate messagingTemplate;
	
	@Autowired
	private RunSuccessFastqcSplitter qcRunSuccessSplitter;
	
	private final Logger logger = LoggerFactory.getLogger(FileFormatMessageTests.class);
	
	private final Integer RUN_ID = 1;
	
	private final String CELL_LIBRARY_ID = "1";
	
	private Set<SampleSource> cellLibrarys;
	
	private SampleSource cellLibrary;
	
	private Set<FileGroup> fileGroups = new HashSet<>();
	
	private Job job;
	
	private FileType fastq;
	
	private List<Message<?>> messages = new ArrayList<>();
	
	@BeforeClass
	public void beforeClass(){
		MockitoAnnotations.initMocks(this);
		Assert.assertNotNull(mockRunDao);
		Assert.assertNotNull(mockRunService);
		Assert.assertNotNull(mockSampleService);
		Assert.assertNotNull(qcRunSuccessSplitter);
		Assert.assertNotNull(mockFastqService);
		inMessageChannel.subscribe(this);
		messagingTemplate = new MessagingTemplate();
		messagingTemplate.setReceiveTimeout(2000);
		
		
		Workflow wf = new Workflow();
		wf.setIName("test_workflow");
		
		Sample library = new Sample();
		library.setId(1);
		
		Sample cell = new Sample();
		cell.setId(2);
		
		cellLibrary = new SampleSource();
		cellLibrary.setId(Integer.valueOf(CELL_LIBRARY_ID));
		fastq = new FileType();
		FileGroup fileGroup = new FileGroup();
		fastq.doUpdate(); // ensure uuid set
		fileGroup.setFileType(fastq);
		fileGroup.setId(1);
		fileGroups.add(fileGroup);
		cellLibrary.getFileGroups().add(fileGroup);
		
		cellLibrarys = new HashSet<SampleSource>();
		cellLibrarys.add(cellLibrary);
		
		job = new Job();
		job.setId(1);
		job.setWorkflow(wf);
		
		// add mocks to qcRunSuccessSplitter (replacing Autowired versions)
		// could also use ReflectionTestUtils.setField(qcRunSuccessSplitter, "runService", mockRunService) - essential if no setters
		ReflectionTestUtils.setField(qcRunSuccessSplitter, "runService", mockRunService);
		ReflectionTestUtils.setField(qcRunSuccessSplitter, "sampleService", mockSampleService);
		ReflectionTestUtils.setField(qcRunSuccessSplitter, "fastqService", mockFastqService);
		ReflectionTestUtils.setField(qcRunSuccessSplitter, "fileService", mockFileService);
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
	public void runSuccessTest() throws Exception{
		// send run complete messages
		try {
			// add mocks to qcRunSuccessSplitter (replacing Autowired versions)
			// could also use ReflectionTestUtils.setField(qcRunSuccessSplitter, "runService", mockRunService) - essential if no setters
			Run run = new Run();
			run.setId(1);
			
			PowerMockito.when(mockRunService.getRunDao()).thenReturn(mockRunDao);
			PowerMockito.when(mockRunDao.getRunByRunId(1)).thenReturn(run);
			PowerMockito.when(mockRunService.getCellLibrariesOnSuccessfulRunCells(Mockito.any(Run.class))).thenReturn(cellLibrarys);
			PowerMockito.when(mockFileService.getFilesForCellLibraryByType(Mockito.any(SampleSource.class), Mockito.any(FileType.class))).thenReturn(fileGroups);
			PowerMockito.when(mockFastqService.hasAttribute(Mockito.any(FileGroup.class), Mockito.any(FastqFileTypeAttribute.class))).thenReturn(true);
			
			RunStatusMessageTemplate template = new RunStatusMessageTemplate(RUN_ID);
			template.setStatus(WaspStatus.COMPLETED);
			Message<WaspStatus> runCompletedMessage = template.build();
			logger.info("runSuccessTest(): Sending message via 'outbound rmi gateway': "+runCompletedMessage.toString());
			Message<?> replyMessage = messagingTemplate.sendAndReceive(outMessageChannel, runCompletedMessage);
			if (replyMessage != null)
				logger.debug("testJobApproved(): Got reply message: "+ replyMessage.toString());
			Thread.sleep(100);
			Assert.assertEquals(messages.size(), 1);
			Assert.assertEquals((WaspStatus) messages.get(0).getPayload(), WaspStatus.CREATED);
		} catch (Exception e){
			// caught an unexpected exception
			e.printStackTrace();
			Assert.fail("Caught Exception: "+e.getMessage());
		}
					
	}
	
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.debug("Message recieved by handleMessage(): " + message.toString());
		messages.add(message);
	}

}
