package edu.yu.einstein.wasp.plugin.babraham.plugin;

import java.util.HashSet;
import java.util.Set;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageHandler;
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
import edu.yu.einstein.wasp.fileformat.plugin.RunSuccessFastqcSplitter;
import edu.yu.einstein.wasp.fileformat.service.FastqService;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.RunStatusMessageTemplate;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;

@ContextConfiguration(locations={"/babraham-test-context.xml"})

public class BabrahamMessageTests extends AbstractTestNGSpringContextTests implements MessageHandler {
	
	// mockRunService and mockRunDao are mocked in context to keep Spring happy when resolving dependencies on bean creation
	// but MUST be re-mocked here (not @Autowired in) otherwise there is autowiring issues with dependencies such as Entitymanager etc.
	
	@Mock private SampleService mockSampleService; 
	
	@Mock private RunService mockRunService; 
	
	@Mock private RunDao mockRunDao;
	
	@Mock private FastqService fastqService;
	
	@Autowired
	@Qualifier("wasp.channel.run.success")
	private DirectChannel messageChannel;
	
	private MessagingTemplate messagingTemplate;
	
	@Autowired
	private RunSuccessFastqcSplitter qcRunSuccessSplitter;
	
	private final Logger logger = LoggerFactory.getLogger(BabrahamMessageTests.class);
	
	private final Integer RUN_ID = 1;
	
	private final String CELL_LIBRARY_ID = "1";
	
	private Set<SampleSource> libraryCells;
	
	private SampleSource libraryCell;
	
	private FileGroup fileGroup;
	
	private Job job;
	
	private FileType fastq;
	
	@BeforeClass
	public void beforeClass(){
		MockitoAnnotations.initMocks(this);
		Assert.assertNotNull(mockRunDao);
		Assert.assertNotNull(mockRunService);
		Assert.assertNotNull(mockSampleService);
		Assert.assertNotNull(qcRunSuccessSplitter);
		Assert.assertNotNull(fastqService);
		
		messagingTemplate = new MessagingTemplate();
		messagingTemplate.setReceiveTimeout(2000);
		
		
		Workflow wf = new Workflow();
		wf.setIName("test_workflow");
		
		Sample library = new Sample();
		library.setId(1);
		
		Sample cell = new Sample();
		cell.setId(2);
		
		libraryCell = new SampleSource();
		libraryCell.setId(Integer.valueOf(CELL_LIBRARY_ID));
		fastq = new FileType();
		fileGroup = new FileGroup();
		fastq.doUpdate(); // ensure uuid set
		fileGroup.setFileType(fastq);
		fileGroup.setId(1);
		libraryCell.getFileGroups().add(fileGroup);
		
		libraryCells = new HashSet<SampleSource>();
		libraryCells.add(libraryCell);
		
		job = new Job();
		job.setId(1);
		job.setWorkflow(wf);
		
		// add mocks to qcRunSuccessSplitter (replacing Autowired versions)
		// could also use ReflectionTestUtils.setField(qcRunSuccessSplitter, "runService", mockRunService) - essential if no setters
		ReflectionTestUtils.setField(qcRunSuccessSplitter, "runService", mockRunService);
		ReflectionTestUtils.setField(qcRunSuccessSplitter, "sampleService", mockSampleService);
		ReflectionTestUtils.setField(qcRunSuccessSplitter, "fastqService", fastqService);
		
	}
	
	@AfterClass
	public void afterClass(){
		//
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
			PowerMockito.when(mockRunService.getCellLibrariesOnSuccessfulRunCells(Mockito.any(Run.class))).thenReturn(libraryCells);
			
			PowerMockito.when(fastqService.getFastqFileType()).thenReturn(fastq);
			
			RunStatusMessageTemplate template = new RunStatusMessageTemplate(RUN_ID);
			template.setStatus(WaspStatus.COMPLETED);
			Message<WaspStatus> runCompletedMessage = template.build();
			logger.info("runSuccessTest(): Sending message via 'outbound rmi gateway': "+runCompletedMessage.toString());
			Message<?> replyMessage = messagingTemplate.sendAndReceive(messageChannel, runCompletedMessage);
			if (replyMessage != null)
				logger.debug("testJobApproved(): Got reply message: "+ replyMessage.toString());
			Thread.sleep(500); // wait for message receiving and job completion events
		} catch (Exception e){
			// caught an unexpected exception
			e.printStackTrace();
			Assert.fail("Caught Exception: "+e.getMessage());
		}
					
	}
	
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.debug("Message recieved by handleMessage(): "+message.toString());
		WaspStatus status = (WaspStatus) message.getPayload();
		logger.debug(status.toString());
	}

}
