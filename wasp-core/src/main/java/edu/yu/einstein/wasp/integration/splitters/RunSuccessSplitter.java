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
import org.springframework.integration.splitter.AbstractMessageSplitter;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.RunStatusMessageTemplate;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * Splitter to react to a successful run by generating launch messages for all libraries on successful cells
 * @author asmclellan
 *
 */
public class RunSuccessSplitter extends AbstractMessageSplitter{
	
	private WaspPluginRegistry waspPluginRegistry;
	
	@Autowired
	public void setWaspPluginRegistry(WaspPluginRegistry waspPluginRegistry) {
		this.waspPluginRegistry = waspPluginRegistry;
	}
	
	private RunService runService;
	
	@Autowired
	public void setRunService(RunService runService) {
		this.runService = runService;
	}
	
	private SampleService sampleService;
	
	@Autowired
	public void setSampleService(SampleService sampleService) {
		this.sampleService = sampleService;
	}


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
		Run run = runService.getRunDao().getRunByRunId(runStatusMessageTemplate.getRunId());
		Set<SampleSource> cellLibraries = runService.getLibraryCellPairsOnSuccessfulRunCellsWithoutControls(run);
		for (SampleSource cellLibrary :  cellLibraries){
			// send message to initiate job processing
			Sample cell = sampleService.getCell(cellLibrary);
			Sample library = sampleService.getLibrary(cellLibrary);
			Job job;
			try {
				job = sampleService.getJobOfLibraryOnCell(cell, library);
			} catch (SampleException e1) {
				logger.error(e1.getLocalizedMessage());
				continue;
			}
			Map<String, String> jobParameters = new HashMap<String, String>();
			jobParameters.put(WaspJobParameters.LIBRARY_ID, job.getJobId().toString());
			jobParameters.put(WaspJobParameters.JOB_ID, job.getJobId().toString());
			jobParameters.put(WaspJobParameters.LIBRARY_CELL, cellLibrary.getSampleSourceId().toString());
			for (WaspPlugin plugin : waspPluginRegistry.getPluginsHandlingArea(job.getWorkflow().getIName())) {
				String flowName = plugin.getBatchJobName(BatchJobTask.ANALYSIS_LIBRARY_PREPROCESS);
				BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( 
						new BatchJobLaunchContext(flowName, jobParameters) );
				try {
					Message<BatchJobLaunchContext> launchMessage = batchJobLaunchMessageTemplate.build();
					logger.debug("preparing new message to send: " + launchMessage);
					outputMessages.add(launchMessage);
				} catch (WaspMessageBuildingException e) {
					throw new MessagingException(e.getLocalizedMessage(), e);
				}
			}
		}
		return outputMessages;
	}

}
