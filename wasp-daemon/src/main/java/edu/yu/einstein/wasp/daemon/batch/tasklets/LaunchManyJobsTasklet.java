/**
 * 
 */
package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.integration.Message;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;
import edu.yu.einstein.wasp.integration.messages.templates.StatusMessageTemplate;

/**
 * @author calder
 *
 */
public class LaunchManyJobsTasklet extends WaspTasklet {
    
    private StepExecution stepExecution;
    private JobExecution jobExecution;
    
    public void setLaunchMessagesToWaitFor(List<Message<?>> messages) {
        List<String> ids = new ArrayList<String>();
        for (Message<?> m : messages) {
            ids.add(m.getHeaders().getId().toString());
        }
        UUID uid = UUID.randomUUID();
        jobExecution.getExecutionContext().put(BatchJobHibernationManager.PARENT_JOB_ID_LIST_KEY_PREFIX + uid.toString(),
                StringUtils.collectionToDelimitedString(ids, BatchJobHibernationManager.PARENT_JON_ID_LIST_DELIMITER));
        
    }
    
    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        this.jobExecution = stepExecution.getJobExecution();
    }
    
    public static void decorateMessageWithParentID(StatusMessageTemplate template) {
        //template.
    }

}
