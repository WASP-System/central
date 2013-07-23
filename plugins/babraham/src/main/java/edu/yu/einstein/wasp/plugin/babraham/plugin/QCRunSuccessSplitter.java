/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.splitter.AbstractMessageSplitter;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.fileformat.service.FastqService;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.RunStatusMessageTemplate;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * QCRunSuccessSplitter listens for run success messages, obtains all of the FASTQ file groups
 * and Launches the Babraham QC pipelines for each.
 * 
 * @author calder
 *
 */
public class QCRunSuccessSplitter extends AbstractMessageSplitter {
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private RunService runService;
	
	@Autowired
	private FastqService fastqService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 
	 */
	public QCRunSuccessSplitter() {
		//
	}

	/** 
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<Message<BatchJobLaunchContext>> splitMessage(Message<?> message) {
		List<Message<BatchJobLaunchContext>> outputMessages = new ArrayList<Message<BatchJobLaunchContext>>();
		if (!RunStatusMessageTemplate.isMessageOfCorrectType(message)){
			logger.warn("Message is not of the correct type (a Run message). Check filter and imput channel are correct");
			return outputMessages; // empty list
		}
		RunStatusMessageTemplate runStatusMessageTemplate = new RunStatusMessageTemplate((Message<WaspStatus>) message);
		if (!runStatusMessageTemplate.getStatus().equals(WaspStatus.COMPLETED) || !runStatusMessageTemplate.getTask().equals(WaspTask.NOTIFY_STATUS)){
			logger.warn("Message has the wrong status or payload value. Check filter and imput channel are correct");
			return outputMessages; // empty list
		}
		
		Run run = runService.getRunDao().getRunByRunId(runStatusMessageTemplate.getRunId());
		Set<SampleSource> cellLibraries = runService.getCellLibrariesOnSuccessfulRunCells(run);
		
		for (SampleSource cellLib : cellLibraries) {
			Set<FileGroup> fgs = cellLib.getFileGroups();
			for (FileGroup fg : fgs) {
				if (fg.getFileType().equals(fastqService.getFastqFileType())) {
					Map<String, String> jobParameters = new HashMap<String, String>();
					jobParameters.put(WaspJobParameters.FILE_GROUP_ID, fg.getId().toString());
					outputMessages.add(prepareMessage(FastQCPlugin.FLOW_NAME, jobParameters));
					outputMessages.add(prepareMessage(FastQScreenPlugin.FLOW_NAME, jobParameters));
				}
			}
			
		}
		return outputMessages;
	}
	
	private Message<BatchJobLaunchContext> prepareMessage(String flowname, Map<String, String> jobParameters){
		BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( 
				new BatchJobLaunchContext(flowname, jobParameters) );
		try {
			Message<BatchJobLaunchContext> launchMessage = batchJobLaunchMessageTemplate.build();
			logger.debug("preparing new message to send: " + launchMessage);
			return launchMessage;
		} catch (WaspMessageBuildingException e) {
			throw new MessagingException(e.getLocalizedMessage(), e);
		}
	}

}
