package edu.yu.einstein.wasp.integration.messages.templates;

import java.util.Set;

import org.springframework.batch.core.StepExecution;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.tasks.JobExecutionTask;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;

public class MessageAwokenHibernationMessageTemplate extends AbstractHibernationMessageTemplate {
	
	private Set<MessageTemplate> awakenJobExecutionOnMessages;

	public MessageAwokenHibernationMessageTemplate(StepExecution stepExecution) {
		super(stepExecution);
		super.setTask(JobExecutionTask.STOP_AND_AWAKE_ON_MESSAGE);
	}

	public MessageAwokenHibernationMessageTemplate(StepExecution stepExecution, String target) {
		super(stepExecution, target);
		super.setTask(JobExecutionTask.STOP_AND_AWAKE_ON_MESSAGE);
	}

	public MessageAwokenHibernationMessageTemplate(Message<?> message) {
		super(message);
		setAwakenJobExecutionOnMessages( (Set<MessageTemplate>) message.getPayload());
	}

	@Override
	public Message<Set<MessageTemplate>> build() throws WaspMessageBuildingException{
		if (this.awakenJobExecutionOnMessages == null)
			throw new WaspMessageBuildingException("no list of MessageTemplates message defined");
		Message<Set<MessageTemplate>> message = null;

		try {
			message = MessageBuilder.withPayload(awakenJobExecutionOnMessages)
					.copyHeaders(getHeaders())
					.build();
		} catch(Exception e){
			throw new WaspMessageBuildingException("build() failed to build message: "+e.getMessage());
		}
		return message;
	}

	public Set<MessageTemplate> getAwakenJobExecutionOnMessages() {
		return awakenJobExecutionOnMessages;
	}

	public void setAwakenJobExecutionOnMessages(Set<MessageTemplate> awakenJobExecutionOnMessages) {
		this.awakenJobExecutionOnMessages = awakenJobExecutionOnMessages;
	}
	
	public static boolean actUponMessageIgnoringExecutionIds(Message<?> message){
		if (isMessageOfCorrectType(message) && message.getHeaders().containsKey(WaspJobTask.HEADER_KEY) &&	
				message.getHeaders().get(WaspJobTask.HEADER_KEY).equals(JobExecutionTask.STOP_AND_AWAKE_ON_MESSAGE))
			return true;
		return false;
	}
	
	/**
	 * Does not change the task in this implementation. Task is pre-determined
	 */
	@Override
	public void setTask(String task){
		// do nothing
	}
	
	@Override
	public Object getPayload(){
		return getAwakenJobExecutionOnMessages();
	}
}
