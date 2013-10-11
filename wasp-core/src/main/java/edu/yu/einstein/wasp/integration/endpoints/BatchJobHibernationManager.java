package edu.yu.einstein.wasp.integration.endpoints;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.annotation.ServiceActivator;

import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.MessageAwokenHibernationMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.MessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.TimeoutAwokenHibernationMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.WaspMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;


/**
 * 
 * @author asmclellan
 *
 */
public class BatchJobHibernationManager {
	
	public static final String HIBERNATING_CODE = "HIBERNATING";
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private JobOperator jobOperator;
	
	private JobExplorer jobExplorer;
	
	private JobRepository jobRepository;
	
	private Map<MessageTemplate, Set<Long>> messageTemplatesForJob = new HashMap<>();

	public BatchJobHibernationManager() {}
	
	@Autowired
	public void setJobExplorer(JobExplorer jobExplorer){
		this.jobExplorer = jobExplorer;
	}
	
	@Autowired
	public void setJobRepository(JobRepository jobRepository){
		this.jobRepository = jobRepository;
	}
	
	
	@Autowired
	public void setJobOperator(JobOperator jobOperator){
		this.jobOperator = jobOperator;
	}
	
	public void addMessageTemplatesForJob(Long jobExecutionId, Set<MessageTemplate> messages) {
		for (MessageTemplate message: messages){
			if (!messageTemplatesForJob.containsKey(message))
				messageTemplatesForJob.put(message, new HashSet<Long>());
			messageTemplatesForJob.get(message).add(jobExecutionId);
		}
	}
	
	public void addMessageTemplateForJob(Long jobExecutionId, MessageTemplate message) {
		Set<MessageTemplate> messages = new HashSet<>();
		messages.add(message);
		addMessageTemplatesForJob(jobExecutionId, messages);
	}
	
	@ServiceActivator
	public void handleMessage(Message<?> message){
		logger.debug("handling message: " + message);
		if (MessageAwokenHibernationMessageTemplate.actUponMessageIgnoringExecutionIds(message)){
			logger.debug("Message is to request stop and re-awaken on message");
			MessageAwokenHibernationMessageTemplate messageTemplate = new MessageAwokenHibernationMessageTemplate(message);
			addMessageTemplatesForJob(messageTemplate.getJobExecutionId(), messageTemplate.getAwakenJobExecutionOnMessages());
			stopJobExecution(messageTemplate.getJobExecutionId());
		} else if (TimeoutAwokenHibernationMessageTemplate.actUponMessageIgnoringExecutionIds(message)){
			logger.debug("Message is to request stop and re-awaken on timeout");
			// TODO: functionality here
		} else {
			logger.debug("Message is not a request stop message");
			for (MessageTemplate testMessageTemplate : messageTemplatesForJob.keySet()){
				if (testMessageTemplate.actUponMessage(message) && 
						testMessageTemplate.getPayload().getClass().isInstance(message) && 
						testMessageTemplate.getPayload().equals(message.getPayload())){
					for (Long jobExecutionId : messageTemplatesForJob.get(testMessageTemplate)){
						logger.debug("restarting job with JobExecution id=" + jobExecutionId + " on receiving message " + message);
						restartJobExecution(jobExecutionId);
					}
				}
			}
		}
		
	}
	
	private void restartJobExecution(Long jobExecutionId){
		try {
			jobOperator.restart(jobExecutionId);
		} catch (JobInstanceAlreadyCompleteException | NoSuchJobExecutionException | NoSuchJobException | JobRestartException | JobParametersInvalidException e) {
			logger.warn("Unable to restart job with JobExecution id=" + jobExecutionId + " (got " + e.getClass().getName() + " Exception :" + 
					e.getLocalizedMessage() + ")");
		}
	}
	
	private void stopJobExecution(Long jobExecutionId){
		logger.debug("Going to stop JobExecution id=" + jobExecutionId);
		try {
			jobOperator.stop(jobExecutionId);
			logger.debug("updating exit status of stopped job execution with id=" + jobExecutionId);
			JobExecution je = jobExplorer.getJobExecution(jobExecutionId);
			ExitStatus newExitStatus = new ExitStatus(BatchJobHibernationManager.HIBERNATING_CODE);
			while (!je.getExitStatus().getExitCode().equals(ExitStatus.STOPPED.getExitCode())){
				try {
					Thread.sleep(50); // defend against delay shutting down job
				} catch (InterruptedException e) {} 
				je = jobExplorer.getJobExecution(jobExecutionId);
			}
			je = jobExplorer.getJobExecution(jobExecutionId); // get fresh
			for (StepExecution se : je.getStepExecutions()){
				se.setExitStatus(newExitStatus);
				jobRepository.update(se);
			}
			je.setExitStatus(newExitStatus);
			jobRepository.update(je);
			je.getExecutionContext().put(BatchJobHibernationManager.HIBERNATING_CODE, true);
			jobRepository.updateExecutionContext(je);
			logger.debug("status updated :)");
		} catch (NoSuchJobExecutionException | JobExecutionNotRunningException e1) {
			logger.warn("Unable to stop job with JobExecution id=" + jobExecutionId + " (got " + e1.getClass().getName() + " Exception :" + 
					e1.getLocalizedMessage() + ")");
		}
	}

}
