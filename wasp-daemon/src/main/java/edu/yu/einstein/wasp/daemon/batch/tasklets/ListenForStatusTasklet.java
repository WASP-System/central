package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionFixed;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager.LockType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.StatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;

/**
 * Listens on the provided subscribable channel for a message with a task and status specified in the
 * provided message template. Will try and hibernate, but will keep responding to messages whilst waiting.
 * @author asmclellan
 */
@Transactional
public class ListenForStatusTasklet extends WaspHibernatingTasklet implements MessageHandler {
	
	protected static final Logger logger = LoggerFactory.getLogger(ListenForStatusTasklet.class);
	
	protected Set<WaspStatusMessageTemplate> messageTemplates = new HashSet<>();
	
	private String failOnStatuses = "";
	
	private List<Message<?>> messageQueue = new ArrayList<>();
	
	public ListenForStatusTasklet() {
		// proxy
	}
	
	public ListenForStatusTasklet(WaspStatusMessageTemplate messageTemplate) {
		setMessageToListenFor(messageTemplate);
	}
	
	public ListenForStatusTasklet(Collection<WaspStatusMessageTemplate> messageTemplates) {
		setMessagesToListenFor(messageTemplates);
	}
	
	public void setMessageToListenFor(WaspStatusMessageTemplate messageTemplate) {
		Set<WaspStatusMessageTemplate> templates = new HashSet<>();
		templates.add(messageTemplate);
		setMessagesToListenFor(templates);
	}
	
	public void setMessagesToListenFor(Collection<WaspStatusMessageTemplate> messageTemplates) {
		this.messageTemplates.clear();
		this.messageTemplates.addAll(messageTemplates);
	}
	
	/**
	 * Will exit step with an exit code of FAILED instead of COMPLETED if any of the provided WaspStatus' are set.
	 * @param status
	 */
	public void setFailOnStatuses(String statuses) {
		this.failOnStatuses = statuses;
	}
	
	private Set<WaspStatus> getFailOnStatusSet(){
		Set<WaspStatus> failOnStatusSet = new HashSet<>();
		if (failOnStatuses.isEmpty())
			return failOnStatusSet;
		for (String status : failOnStatuses.split(","))
			failOnStatusSet.add(WaspStatus.valueOf(status.trim()));
		return failOnStatusSet;
	}
	
	@Override
	@PostConstruct
	protected void init() throws MessagingException{
		if (messageTemplates == null)
			throw new MessagingException("No message templates defined to check against");
		super.init();
	}
	
	@Override
	@RetryOnExceptionFixed
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		StepExecution stepExecution =  context.getStepContext().getStepExecution();
		Long stepExecutionId =stepExecution.getId();
		Long jobExecutionId = context.getStepContext().getStepExecution().getJobExecutionId();
		logger.trace(name + "execute() invoked");
		if (wasWokenOnMessage(context)){
			logger.info("StepExecution (id=" + stepExecutionId + ", JobExecution id=" + jobExecutionId + 
					") was woken up from hibernation for a message. Skipping to next step...");
			setStepStatusInJobExecutionContext(stepExecution, BatchStatus.COMPLETED);
			BatchJobHibernationManager.unlockJobExecution(stepExecution.getJobExecution(), LockType.WAKE);
			return RepeatStatus.FINISHED;
		}
		if (isExpectedMessageReceived(context))
			return RepeatStatus.FINISHED;
		if (isHibernationRequestedForJob(context.getStepContext().getStepExecution().getJobExecution())){
			logger.trace("This JobExecution (id=" + jobExecutionId + ") is already undergoing hibernation. Awaiting hibernation...");
		} else if (!wasHibernationRequested){
			// let cycle a few times before attempting hibernation so that all steps and the job are fully awake and recorded in batch. Will not hibernate
			// all steps if this isn't done.
			logger.info("Going to request hibernation from StepExecution (id=" + stepExecutionId + ", JobExecution id=" + jobExecutionId + 
					") as not previously requested");
			addStatusMessagesToWakeStepToContext(context, messageTemplates);
			addStatusMessagesToAbandonStepToContext(context, abandonTemplates);
			requestHibernation(context);
		} else if (!wasHibernationRequestGranted){
				logger.debug("Previous hibernation request made by this StepExecution (id=" + stepExecutionId + ", JobExecution id=" + jobExecutionId + 
						") but we were still waiting for all steps to be ready. Going to retry request.");
			requestHibernation(context);
			logger.debug("Hibernate request made by this StepExecution (id=" + stepExecutionId + ", JobExecution id=" + jobExecutionId + 
					") but JobExecution is not yet ready to hibernate");
		} else {
			logger.debug("Hibernate request was granted to this StepExecution (id=" + stepExecutionId + ", JobExecution id=" + jobExecutionId + 
					"). Awaiting hibernation...");
		}
		return RepeatStatus.CONTINUABLE;	
	}
	
	@Override
	protected boolean isExpectedMessageReceived(ChunkContext context){
		StepExecution stepExecution =  context.getStepContext().getStepExecution();
		Long stepExecutionId =stepExecution.getId();
		Long jobExecutionId = context.getStepContext().getStepExecution().getJobExecutionId();
		Set<Message<?>> allMessages = new HashSet<>();
		allMessages.addAll(messageQueue);
		allMessages.addAll(abandonMessageQueue);
		if ((!allMessages.isEmpty()) && context.getStepContext().getStepExecution().getJobExecution().getStatus().isRunning()){
			if (wasHibernationRequested){
				setHibernationRequestedForStep(stepExecution, false);
				hibernationManager.removeStepExecutionFromWakeMessageMap(stepExecution);
				hibernationManager.removeStepExecutionFromAbandonMessageMap(stepExecution);
			}
			logger.info("StepExecution (id=" + stepExecutionId + ", JobExecution id=" + jobExecutionId + ") received an expected message so finishing step.");
			setStepStatusInJobExecutionContext(stepExecution, BatchStatus.COMPLETED);
			// make sure all messages get replies
			sendSuccessReplyToAllMessagesInQueue(allMessages);
			return true;
		}
		return false;
	}
	
	private ExitStatus getExitStatus(StepExecution stepExecution, WaspStatus waspStatus){
		ExitStatus status = stepExecution.getExitStatus();
		logger.debug("exit status entering: " + status);
		if (getFailOnStatusSet().contains(waspStatus))
			status = status.and(ExitStatus.FAILED);
		if (waspStatus.equals(WaspStatus.ABANDONED) && stepExecution.getStatus().equals(BatchStatus.COMPLETED))
			status = status.and(ExitStatus.TERMINATED);
		logger.debug("exit status exiting: " + status);
		return status;
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		ExitStatus exitStatus = super.afterStep(stepExecution);
		if (wasWokenOnMessage(stepExecution))
			exitStatus = exitStatus.and(getExitStatus(stepExecution, getWokenOnMessageStatus(stepExecution)));
		else {
			for (Message<?> message : messageQueue){
				WaspStatusMessageTemplate messageTemplate = new WaspStatusMessageTemplate((Message<WaspStatus>) message);
				exitStatus = exitStatus.and(getExitStatus(stepExecution, messageTemplate.getStatus()));
			}
		}
		// set exit status to equal the most severe outcome of all received messages
		this.messageQueue.clear(); // clean up in case of restart
		logger.debug(stepExecution.getStepName() + " going to exit step with ExitStatus=" + exitStatus);
		return exitStatus;
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		super.handleMessage(message);
		WaspStatus statusFromMessage = (WaspStatus) message.getPayload();

		// then check the messages and status against the status we are interested in for a reportable match
		for (StatusMessageTemplate messageTemplate: messageTemplates){
			if (messageTemplate.actUponMessage(message) && statusFromMessage.equals(messageTemplate.getStatus()) ){
				this.messageQueue.add(message);
				logger.debug(name + "handleMessage() adding found message to be compatible so adding to queue: " + message.toString());
			}
		}
	}

}
