package edu.yu.einstein.wasp.batch;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.integration.Message;

import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.MessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;

public class MessageAwokenBatchJobStep implements Serializable{
	
	private static final long serialVersionUID = -6076868403762580794L;
	
	private Long jobExecutionId;
	private Long stepExecutionId;
	private Set<WaspStatusMessageTemplate> messageTemplatesToWakeJob = new HashSet<>();
	
	public MessageAwokenBatchJobStep(){
	}
	
	public MessageAwokenBatchJobStep(Long jobExecutionId, Long stepExecutionId, Collection<WaspStatusMessageTemplate> messageTemplates){
		setJobExecutionId(jobExecutionId);
		setStepExecutionId(stepExecutionId);
		setMessageTemplatesToWakeJob(messageTemplates);
	}
	
	public MessageAwokenBatchJobStep(Long jobExecutionId, Long stepExecutionId, WaspStatusMessageTemplate messageTemplate){
		setJobExecutionId(jobExecutionId);
		setStepExecutionId(stepExecutionId);
		addMessageTemplateToWakeJob(messageTemplate);
	}
	
	public Long getJobExecutionId() {
		return jobExecutionId;
	}
	
	public void setJobExecutionId(Long jobExecutionId) {
		this.jobExecutionId = jobExecutionId;
	}
	
	public Long getStepExecutionId() {
		return stepExecutionId;
	}
	
	public void setStepExecutionId(Long stepExecutionId) {
		this.stepExecutionId = stepExecutionId;
	}
	
	public Set<WaspStatusMessageTemplate> getMessageTemplatesToWakeJob() {
		return messageTemplatesToWakeJob;
	}
	
	public void setMessageTemplatesToWakeJob(Collection<WaspStatusMessageTemplate> messageTemplatesToWakeJob) {
		this.messageTemplatesToWakeJob = new HashSet<>();
		addMessageTemplatesToWakeJob(messageTemplatesToWakeJob);
	}
	
	public void addMessageTemplatesToWakeJob(Collection<WaspStatusMessageTemplate> messageTemplatesToWakeJob) {
		for (WaspStatusMessageTemplate template : messageTemplatesToWakeJob)
			this.messageTemplatesToWakeJob.add(template);
	}
	
	public void addMessageTemplateToWakeJob(WaspStatusMessageTemplate messageTemplateToWakeJob) {
		this.messageTemplatesToWakeJob.add(messageTemplateToWakeJob);
	}
	
	public boolean doesMessageWakeJob(Message<WaspStatus> message){
		WaspStatusMessageTemplate incomingMessageTemplate = new WaspStatusMessageTemplate(message);
		return messageTemplatesToWakeJob.contains(incomingMessageTemplate);
	}
	
	@Override
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj == null || this.getClass() != obj.getClass())
			return false;
		MessageAwokenBatchJobStep js = (MessageAwokenBatchJobStep) obj;
		if (js.jobExecutionId.equals(this.jobExecutionId) && js.stepExecutionId.equals(this.stepExecutionId))
			return true;
		return false;
	}
	
	@Override
	public int hashCode(){
		int hash = 7;
		hash = 31 * hash + (null == stepExecutionId ? 0 : stepExecutionId.hashCode());
		hash = 31 * hash + (null == jobExecutionId ? 0 : jobExecutionId.hashCode());
		return hash;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("[jobExecutionId: " + jobExecutionId);
		sb.append(", ");
		sb.append("stepExecutionId: " + stepExecutionId);
		sb.append(", ");
		sb.append("MessageTemplates: {");
		for (WaspStatusMessageTemplate template : messageTemplatesToWakeJob){
			sb.append(template);
			sb.append(", ");
		}
		sb.delete(sb.length() - 2, sb.length()); // trim last ", "
		sb.append("}]");
		return sb.toString();
	}
}
