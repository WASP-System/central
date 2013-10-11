package edu.yu.einstein.wasp.integration.messages.templates;

import org.springframework.batch.core.StepExecution;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.tasks.JobExecutionTask;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;

public class TimeoutAwokenHibernationMessageTemplate extends AbstractHibernationMessageTemplate {
	
	private Long timeout; // ms

	public TimeoutAwokenHibernationMessageTemplate(StepExecution stepExecution) {
		super(stepExecution);
		super.setTask(JobExecutionTask.STOP_AND_AWAKE_ON_TIMEOUT);
	}

	public TimeoutAwokenHibernationMessageTemplate(StepExecution stepExecution, String target) {
		super(stepExecution, target);
		super.setTask(JobExecutionTask.STOP_AND_AWAKE_ON_TIMEOUT);
	}

	public TimeoutAwokenHibernationMessageTemplate(Message<?> message) {
		super(message);
		setTimeout( (Long) message.getPayload());
	}

	@Override
	public Message<Long> build() throws WaspMessageBuildingException{
		if (this.timeout == null)
			throw new WaspMessageBuildingException("no list of MessageTemplates message defined");
		Message<Long> message = null;

		try {
			message = MessageBuilder.withPayload(timeout)
					.copyHeaders(getHeaders())
					.build();
		} catch(Exception e){
			throw new WaspMessageBuildingException("build() failed to build message: "+e.getMessage());
		}
		return message;
	}
	
	public static boolean actUponMessageIgnoringExecutionIds(Message<?> message){
		if (isMessageOfCorrectType(message) && message.getHeaders().containsKey(WaspJobTask.HEADER_KEY) &&	
				message.getHeaders().get(WaspJobTask.HEADER_KEY).equals(JobExecutionTask.STOP_AND_AWAKE_ON_TIMEOUT))
			return true;
		return false;
	}

	/**
	 * get timeout period in milliseconds
	 * @return
	 */
	public Long getTimeout() {
		return timeout;
	}

	/**
	 * set timeout period in milliseconds
	 * @param timeout
	 */
	public void setTimeout(Long timeout) {
		this.timeout = timeout;
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
		return getTimeout();
	}
}
