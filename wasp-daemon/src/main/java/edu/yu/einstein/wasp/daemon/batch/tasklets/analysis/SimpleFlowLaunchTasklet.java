package edu.yu.einstein.wasp.daemon.batch.tasklets.analysis;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.daemon.batch.tasklets.AbandonMessageHandlingTasklet;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.service.RunService;

/**
 * Tasklet takes a job name and a job parameter map, sends a launch message to the job and returns.
 * 
 * @author calder
 *
 */
public class SimpleFlowLaunchTasklet extends AbandonMessageHandlingTasklet {
    
    private String flowName;
    private Map<String,String> jobParameters = new HashMap<String,String>();

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RunService runService;
    
    public SimpleFlowLaunchTasklet(String flowName) {
        logger.debug("creating message to launch " + flowName);
        this.flowName = flowName;
    }

    @Override
    public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
        
        BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate(new BatchJobLaunchContext(flowName, jobParameters));
        Message<BatchJobLaunchContext> message = batchJobLaunchMessageTemplate.build();
        
        String muid = message.getHeaders().getId().toString();
        JSONObject jp = new JSONObject(jobParameters);
        logger.debug("Sending launch message " + muid + " to " + flowName + " with parameters " + jp.toString());
        runService.sendOutboundMessage(message, true);
        logger.debug("launched " + flowName);
        return RepeatStatus.FINISHED;
    }

    /**
     * @return the jobParameters
     */
    public Map<String,String> getJobParameters() {
        return jobParameters;
    }

    /**
     * @param jobParameters the jobParameters to set
     */
    public void setJobParameters(Map<String,String> jobParameters) {
        this.jobParameters = jobParameters;
    }



}
