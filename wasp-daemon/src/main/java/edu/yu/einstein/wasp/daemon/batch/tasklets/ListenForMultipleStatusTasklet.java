package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionFixed;
import edu.yu.einstein.wasp.exception.WaspBatchJobExecutionException;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager.LockType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.StatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;

/**
 * Listens on the provided subscribable channel for a set of messages, the
 * identity of which is obtained from a value promoted to the job execution
 * context. These message IDs are stored in the step context as "messageList" 
 * and delimited by ListenForMultipleStatusTasklet.ID_DELIMITER.  Individual
 * messages UUIDs are discovered by inspecting the return status message for a header
 * with a key of PARENT_MESSAGE_ID_KEY.
 * 
 * @author asmclellan
 */
public class ListenForMultipleStatusTasklet extends ListenForStatusTasklet {

    protected static final Logger logger = LoggerFactory.getLogger(ListenForMultipleStatusTasklet.class);

    public static final String ID_DELIMITER = "|";

    public static final String MESSAGE_LIST_KEY = "messageList";
    
    public static final String PARENT_MESSAGE_ID_KEY = "parentMessageID";

    private StepExecution stepExecution;

    private List<String> ids = new ArrayList<String>();

    public ListenForMultipleStatusTasklet(WaspStatusMessageTemplate messageTemplate) {
        super(messageTemplate);
    }

    public ListenForMultipleStatusTasklet(Collection<WaspStatusMessageTemplate> messageTemplates) {
        super(messageTemplates);
    }

    @PostConstruct
    protected void init() throws MessagingException {
        super.init();
    }

    @PreDestroy
    protected void destroy() throws Throwable {
        super.destroy();
    }

    @Override
    @RetryOnExceptionFixed
    public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
        StepExecution stepExecution = context.getStepContext().getStepExecution();
        Long stepExecutionId = stepExecution.getId();
        Long jobExecutionId = context.getStepContext().getStepExecution().getJobExecutionId();
        logger.trace(name + "execute() invoked");
        if (wasWokenOnMessage(context)) {
            logger.info("StepExecution (id=" + stepExecutionId + ", JobExecution id=" + jobExecutionId
                    + ") was woken up from hibernation for a message. Skipping to next step...");
            setStepStatusInJobExecutionContext(stepExecution, BatchStatus.COMPLETED);
            BatchJobHibernationManager.unlockJobExecution(stepExecution.getJobExecution(), LockType.WAKE);
            return RepeatStatus.FINISHED;
        }
        Set<Message<?>> allMessages = new HashSet<>();
        allMessages.addAll(messageQueue);
        allMessages.addAll(abandonMessageQueue);
        if ((!allMessages.isEmpty()) && context.getStepContext().getStepExecution().getJobExecution().getStatus().isRunning()) {
            if (wasHibernationRequested) {
                setHibernationRequestedForStep(stepExecution, false);
                hibernationManager.removeStepExecutionFromWakeMessageMap(stepExecution);
                hibernationManager.removeStepExecutionFromAbandonMessageMap(stepExecution);
            }
            logger.info("StepExecution (id=" + stepExecutionId + ", JobExecution id=" + jobExecutionId + ") received an expected message so finishing step.");
            setStepStatusInJobExecutionContext(stepExecution, BatchStatus.COMPLETED);
            // make sure all messages get replies
            sendSuccessReplyToAllMessagesInQueue(allMessages);
            return RepeatStatus.FINISHED;
        }
        if (isHibernationRequestedForJob(context.getStepContext().getStepExecution().getJobExecution())) {
            logger.trace("This JobExecution (id=" + jobExecutionId + ") is already undergoing hibernation. Awaiting hibernation...");
        } else if (!wasHibernationRequested) {
            // let cycle a few times before attempting hibernation so that all
            // steps and the job are fully awake and recorded in batch. Will not
            // hibernate
            // all steps if this isn't done.
            logger.info("Going to request hibernation from StepExecution (id=" + stepExecutionId + ", JobExecution id=" + jobExecutionId
                    + ") as not previously requested");
            addStatusMessagesToWakeStepToContext(context, messageTemplates);
            addStatusMessagesToAbandonStepToContext(context, abandonTemplates);
            requestHibernation(context);
        } else if (!wasHibernationRequestGranted) {
            logger.debug("Previous hibernation request made by this StepExecution (id=" + stepExecutionId + ", JobExecution id=" + jobExecutionId
                    + ") but we were still waiting for all steps to be ready. Going to retry request.");
            requestHibernation(context);
            logger.debug("Hibernate request made by this StepExecution (id=" + stepExecutionId + ", JobExecution id=" + jobExecutionId
                    + ") but JobExecution is not yet ready to hibernate");
        } else {
            logger.debug("Hibernate request was granted to this StepExecution (id=" + stepExecutionId + ", JobExecution id=" + jobExecutionId
                    + "). Awaiting hibernation...");
        }
        return RepeatStatus.CONTINUABLE;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        ExitStatus exitStatus = super.afterStep(stepExecution);
        exitStatus = exitStatus.and(getExitStatus(stepExecution, getWokenOnMessageStatus(stepExecution)));
        // set exit status to equal the most severe outcome of all received
        // messages
        this.messageQueue.clear(); // clean up in case of restart
        this.abandonMessageQueue.clear(); // clean up in case of restart
        logger.debug("Going to exit step with ExitStatus=" + exitStatus);
        return exitStatus;
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        logger.debug(name + "handleMessage() invoked. Received message: " + message.toString());
        if (!WaspStatus.class.isInstance(message.getPayload()))
            return;
        WaspStatus statusFromMessage = (WaspStatus) message.getPayload();

        // first check if any abort / failure messages have been delivered from
        // a monitored message template
        for (StatusMessageTemplate messageTemplate : abandonTemplates) {
            if (messageTemplate.actUponMessage(message)) {
                this.abandonMessageQueue.add(message);
                logger.debug(name + "handleMessage() found ABANDONED message for abort-monitored template " + messageTemplate.getClass().getName()
                        + ". Going to fail step.");
            }
        }

        // then check the messages and status against the status we are
        // interested in for a reportable match
        for (StatusMessageTemplate messageTemplate : messageTemplates) {
            if (messageTemplate.actUponMessage(message) && statusFromMessage.equals(messageTemplate.getStatus())) {
                this.messageQueue.add(message);
                logger.debug(name + "handleMessage() adding found message to be compatible so adding to queue: " + message.toString());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeStep(StepExecution stepExecution) {
        logger.debug("StepExecutionListener beforeStep saving StepExecution");
        this.stepExecution = stepExecution;
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        ids = getMessageIDListFromJobExecutionContext(jobContext);
        if (ids.size() == 0) {
            String message = "Job ExecutionContext must have a delimited list of UUIDs to wait for - " +
                    "set the list using ListenForMultipleStatusTasklet.insertMessageIDListInJobExecutionContext";
            logger.error(message);
            throw new WaspRuntimeException(message);
        }
    }
    
    public static void insertMessageIDListInJobExecutionContext(ExecutionContext jobExecutionContext, List<UUID> idList) {
        jobExecutionContext.put(MESSAGE_LIST_KEY, StringUtils.collectionToDelimitedString(idList, ID_DELIMITER));
    }
    
    public static List<String> getMessageIDListFromJobExecutionContext(ExecutionContext jobExecutionContext) {
        List<String> retval = new ArrayList<String>();
        if (jobExecutionContext.containsKey(MESSAGE_LIST_KEY)) {
            retval = Arrays.asList(StringUtils.delimitedListToStringArray((String)jobExecutionContext.get(MESSAGE_LIST_KEY), ID_DELIMITER));
        }
        return retval;
    }

}
