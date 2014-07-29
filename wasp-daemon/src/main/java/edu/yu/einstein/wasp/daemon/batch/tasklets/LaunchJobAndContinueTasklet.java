/**
 * 
 */
package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.service.RunService;

/**
 * Tasklet to launch a job without regard to consequence.
 * 
 * @author calder
 *
 */
public class LaunchJobAndContinueTasklet implements Tasklet {
	
	private String flowName;
	private Map<String,String> jobParameters;
	
	@Autowired
	private RunService runService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public LaunchJobAndContinueTasklet(String flowName, Map<String,String> jobParameters) {
		this.jobParameters = jobParameters;
		this.flowName = flowName;
	}

	@Override
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		logger.debug("launching flow " + flowName + " and proceeding to the next step");
		BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate(new BatchJobLaunchContext(flowName, jobParameters));
        Message<BatchJobLaunchContext> message = batchJobLaunchMessageTemplate.build();
        logger.debug("requesting sendOutboundMessage for: " + message.toString());
        runService.sendOutboundMessage(message, true);
		return RepeatStatus.FINISHED;
	}

}
