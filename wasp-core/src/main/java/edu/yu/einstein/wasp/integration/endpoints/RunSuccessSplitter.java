package edu.yu.einstein.wasp.integration.endpoints;

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

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.RunStatusMessageTemplate;
import edu.yu.einstein.wasp.interfacing.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * Splitter to react to a successful run by generating launch messages for all libraries on successful cells
 * @author asmclellan
 *
 */
public class RunSuccessSplitter extends WaspAbstractMessageSplitter{
	
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
	
	@Autowired
	private GenomeService genomeService;


	private static final Logger logger = LoggerFactory.getLogger(RunSuccessSplitter.class);


	@SuppressWarnings("unchecked")
	@Override
	protected List<Message<BatchJobLaunchContext>> splitMessage(Message<?> message) {
		List<Message<BatchJobLaunchContext>> outputMessages = new ArrayList<Message<BatchJobLaunchContext>>();
		if (isInDemoMode){
			logger.warn("Jobs are not started when in demo mode");
			return outputMessages;
		}
		if (!RunStatusMessageTemplate.isMessageOfCorrectType(message)){
			logger.warn("Message is not of the correct type (a Run message). Check filter and imput channel are correct");
			return outputMessages; // empty list
		}
		RunStatusMessageTemplate runStatusMessageTemplate = new RunStatusMessageTemplate((Message<WaspStatus>) message);
		if (!runStatusMessageTemplate.getStatus().equals(WaspStatus.COMPLETED) || !runStatusMessageTemplate.getTask().equals(WaspTask.NOTIFY_STATUS)){
			logger.warn("Message has the wrong status or payload value. Check filter and input channel are correct");
			return outputMessages; // empty list
		}
		Run run = runService.getRunDao().getRunByRunId(runStatusMessageTemplate.getRunId());
		Set<SampleSource> cellLibraries = runService.getCellLibrariesOnSuccessfulRunCellsWithoutControls(run);
		for (SampleSource cellLibrary :  cellLibraries){
			// send message to initiate job processing
			Job job = sampleService.getJobOfLibraryOnCell(cellLibrary);
			    
			Map<String, String> jobParameters = new HashMap<String, String>();
			jobParameters.put(WaspJobParameters.CELL_LIBRARY_ID, cellLibrary.getId().toString());
			jobParameters.put(WaspJobParameters.BATCH_JOB_TASK, BatchJobTask.ANALYSIS_LIBRARY_PREPROCESS);
			String worflowIname = job.getWorkflow().getIName();
			for (BatchJobProviding plugin : waspPluginRegistry.getPluginsHandlingArea(worflowIname, BatchJobProviding.class)) {
				String flowName = plugin.getBatchJobName(BatchJobTask.ANALYSIS_LIBRARY_PREPROCESS);
				if (flowName == null){
					logger.warn("No generic flow found for plugin handling workflow " + worflowIname);
					continue;
				}
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
