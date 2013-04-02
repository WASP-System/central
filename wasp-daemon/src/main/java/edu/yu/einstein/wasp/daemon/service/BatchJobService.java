package edu.yu.einstein.wasp.daemon.service;

import java.util.List;
import java.util.Set;

import org.springframework.batch.core.JobExecution;
import org.springframework.integration.Message;

import edu.yu.einstein.wasp.exception.JobContextInitializationException;
import edu.yu.einstein.wasp.exception.SoftwareConfigurationException;
import edu.yu.einstein.wasp.model.ResourceType;

public interface BatchJobService {

	public abstract Set<JobExecution> findAllRunningJobExecutions();

	public Message<?> launch(Integer jobId, ResourceType softwareResourceType, List<Integer> libraryCellIds, 
			String batchJobType, int messageTimeoutInMillis) throws JobContextInitializationException, SoftwareConfigurationException;

}
