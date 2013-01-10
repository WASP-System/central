package edu.yu.einstein.wasp.integration.splitters;

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
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.splitter.AbstractMessageSplitter;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.RunStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.WaspTask;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.service.SampleService;

public class RunSuccessSplitter extends AbstractMessageSplitter{
	
	private SampleService sampleService;
	
	@Autowired
	public void setSampleService(SampleService sampleService) {
		this.sampleService = sampleService;
	}


	private static final String ANALYSIS_FLOW_NAME = "wasp.analysis.libraryPreprocessFlow.v1";
	
	private static final Logger logger = LoggerFactory.getLogger(RunSuccessSplitter.class);


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
		Set<Sample> libraries = sampleService.getLibrariesOnSuccessfulRunCellsWithoutControls(runStatusMessageTemplate.getRunId());
		logger.debug("Based on successful run (" + runStatusMessageTemplate.getRunId() + "), preparing to send " + libraries.size() + " launch messages for " + ANALYSIS_FLOW_NAME);
		for (Sample library: libraries){
			// send message to initiate job processing
			Map<String, String> jobParameters = new HashMap<String, String>();
			jobParameters.put(WaspJobParameters.LIBRARY_ID, library.getSampleId().toString());
			BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( new BatchJobLaunchContext(ANALYSIS_FLOW_NAME, jobParameters) );
			try {
				Message<BatchJobLaunchContext> launchMessage = batchJobLaunchMessageTemplate.build();
				logger.debug("preparing new message to send: " + launchMessage);
				outputMessages.add(launchMessage);
			} catch (WaspMessageBuildingException e) {
				logger.warn("Message Building Exception caught. Abandoning sending current message:" + e.getLocalizedMessage());
				//throw new MessagingException(e.getLocalizedMessage(), e);
			}
		}
		return outputMessages;
	}

}
