/**
 * 
 */
package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.WaspMessageTemplate;
import edu.yu.einstein.wasp.service.RunService;

/**
 * This tasklet offers methods to provide for launching arbitrarily many jobs, and then waiting
 * for their completion in a ListenForManyCompletionTasklet.
 * 
 * @author calder
 *
 */
@Transactional("entityManager")
public abstract class LaunchManyJobsTasklet implements Tasklet, StepExecutionListener {
    
    private StepExecution stepExecution;
    private ExecutionContext stepContext;
    
    @Autowired
    private RunService runService;
    
    private int child = 0;
    
    private UUID uid;
    
    private List<Message<BatchJobLaunchContext>> messages = new ArrayList<Message<BatchJobLaunchContext>>();
    private List<Integer> number = new ArrayList<Integer>();
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    public LaunchManyJobsTasklet() {
        super();
        uid = UUID.randomUUID();
    }
    
    /**
     * To be called prior to launching new job executions created by this step.  Marks messages with appropriate
     * annotations required to identify child processes in the wait for completion step.
     *  
     * @param messages
     * @throws WaspMessageBuildingException 
     */
    public synchronized void requestLaunch(String flowName, Map<String,String> jobParameters) throws WaspMessageBuildingException {
        
        jobParameters.put(WaspMessageTemplate.PARENT_ID, uid.toString());
        jobParameters.put(WaspMessageTemplate.CHILD_MESSAGE_ID, String.valueOf(child));
        
        logger.debug("Launching flow '" + flowName + "' with jobParameters: " + jobParameters);
        
        BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate(new BatchJobLaunchContext(flowName, jobParameters));
        Message<BatchJobLaunchContext> message = batchJobLaunchMessageTemplate.build();
        
        String muid = message.getHeaders().getId().toString();
        logger.debug("Adding a new message id (" + child + ": " + muid + ") to parent step (" + stepExecution.getId() + ") identified by " + uid.toString());
        
        messages.add(message);
        number.add(child++);
        
    }
    
    /**
     * Launches the messages.  Make sure this is called before you return FINISHED!
     */
    private void doLaunch() {
        logger.debug("doLaunch");
        for (Message<BatchJobLaunchContext> m : messages) {
            logger.debug("requesting sendOutboundMessage for: " + m.toString());
            runService.sendOutboundMessage(m, true);
        }
        
        logger.debug("setting " + BatchJobHibernationManager.PARENT_JOB_CHILD_LIST_KEY + " to " +
                StringUtils.collectionToDelimitedString(number, BatchJobHibernationManager.PARENT_JOB_CHILD_LIST_DELIMITER));
        
        stepContext.put(BatchJobHibernationManager.PARENT_JOB_CHILD_LIST_KEY,
                StringUtils.collectionToDelimitedString(number, BatchJobHibernationManager.PARENT_JOB_CHILD_LIST_DELIMITER));
    }
    
    
    /** 
     * {@inheritDoc}
     * 
     * This implementation provides a doExecute method for subclasses where work is done.  Upon completion, all
     * messages set by the requestLaunch method are sent and the tasklet returns immediately with
     * RepeatStatus.FINISHED.
     */
    @Override
    public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
        doExecute();
        doLaunch();
        return RepeatStatus.FINISHED;
    }

    /**
     * 
     */
    public abstract void doExecute();
    
    // BeforeStep
    @Override
    public void beforeStep(StepExecution stepExecution) {
        logger.debug("saving step execution");
        this.stepExecution = stepExecution;
        this.stepContext = stepExecution.getExecutionContext();
        this.stepContext.put(BatchJobHibernationManager.PARENT_JOB_ID_KEY, uid.toString());
    }

    @Override
    public ExitStatus afterStep(StepExecution arg0) {
        ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();
        
        jobContext.put(BatchJobHibernationManager.PARENT_JOB_ID_KEY, uid.toString());
        jobContext.put(BatchJobHibernationManager.PARENT_JOB_CHILD_LIST_KEY,
                StringUtils.collectionToDelimitedString(number, BatchJobHibernationManager.PARENT_JOB_CHILD_LIST_DELIMITER));
        return null;
    }

    public StepExecution getStepExecution() {
        return stepExecution;
    }

    public void setStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    public ExecutionContext getStepContext() {
        return stepContext;
    }

    public void setStepContext(ExecutionContext stepContext) {
        this.stepContext = stepContext;
    }
    


}
