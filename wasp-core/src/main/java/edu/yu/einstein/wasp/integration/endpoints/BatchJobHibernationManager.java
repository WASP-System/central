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
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.annotation.ServiceActivator;

import edu.yu.einstein.wasp.integration.messages.templates.MessageAwokenHibernationMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.MessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.TimeoutAwokenHibernationMessageTemplate;


/**
 * 
 * @author asmclellan
 *
 */
public class BatchJobHibernationManager {
	
	public static final String HIBERNATING_KEY = "hibernating";
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private JobOperator jobOperator;
	
	private JobExplorer jobExplorer;
	
	private Map<MessageTemplate, Set<Long>> messagesForJob = new HashMap<>();

	public BatchJobHibernationManager() {}
	
	@Autowired
	public void setJobExplorer(JobExplorer jobExplorer){
		this.jobExplorer = jobExplorer;
	}
	
	
	@Autowired
	public void setJobOperator(JobOperator jobOperator){
		this.jobOperator = jobOperator;
	}
	
	public void addMessagesForJob(Long jobExecutionId, Set<MessageTemplate> messages) {
		for (MessageTemplate message: messages){
			if (!messagesForJob.containsKey(message))
				messagesForJob.put(message, new HashSet<Long>());
			messagesForJob.get(message).add(jobExecutionId);
		}
	}
	
	public void addMessageForJob(Long jobExecutionId, MessageTemplate message) {
		Set<MessageTemplate> messages = new HashSet<>();
		messages.add(message);
		addMessagesForJob(jobExecutionId, messages);
	}
	
	@ServiceActivator
	public void handleMessage(Message<?> message){
		if (MessageAwokenHibernationMessageTemplate.actUponMessageIgnoringJobExecutionId(message)){
			MessageAwokenHibernationMessageTemplate messageTemplate = new MessageAwokenHibernationMessageTemplate(message);
			addMessagesForJob(messageTemplate.getJobExecutionId(), messageTemplate.getAwakenJobExecutionOnMessages());
			stopJobExecution(messageTemplate.getJobExecutionId());
		} else if (TimeoutAwokenHibernationMessageTemplate.actUponMessageIgnoringJobExecutionId(message)){
			// TODO: functionality here
		} else {
			for (MessageTemplate testMessage : messagesForJob.keySet())
				if (testMessage.actUponMessage(message))
					for (Long jobExecutionId : messagesForJob.get(testMessage))
						restartJobExecution(jobExecutionId);
		}
		
	}
	
	private void restartJobExecution(Long jobExecutionId){
		try {
			jobOperator.restart(jobExecutionId);
		} catch (JobInstanceAlreadyCompleteException | NoSuchJobExecutionException | NoSuchJobException | JobRestartException | JobParametersInvalidException e) {
			logger.warn("Unable to re-start JobExecution with id=" + jobExecutionId);
			e.printStackTrace();
		}
	}
	
	private void stopJobExecution(Long jobExecutionId){
		try {
			jobOperator.stop(jobExecutionId);
		} catch (NoSuchJobExecutionException | JobExecutionNotRunningException e) {
			logger.warn("Unable to stop JobExecution with id=" + jobExecutionId);
			e.printStackTrace();
		}
		logger.debug("updating exit status of stopped job exectuion with id=" + jobExecutionId);
		JobExecution je = jobExplorer.getJobExecution(jobExecutionId);
		je.getExecutionContext().put(BatchJobHibernationManager.HIBERNATING_KEY, true);
		ExitStatus newExitStatus = ExitStatus.EXECUTING;
		newExitStatus.addExitDescription(HIBERNATING_KEY);
		while (!je.getExitStatus().getExitCode().equals(ExitStatus.STOPPED)){
			try {
				Thread.sleep(500); // defend against delay shutting down job
			} catch (InterruptedException e) {} 
		}
		je.setExitStatus(newExitStatus);
		for (StepExecution se : je.getStepExecutions())
			se.setExitStatus(newExitStatus);
		logger.debug("status updated :)");
	}

}
