package edu.yu.einstein.wasp.batch.core.extension.dao;

import java.util.List;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.dao.StepExecutionDao;

public interface WaspStepExecutionDao extends StepExecutionDao{
	
	/**
	 * Get the step executions that match the job name provided, and parameters provided as a Map.
	 * The name match is a suffix match so 'aStep' or 'job.aStep' or 'wasp.job.aStep' will all 
	 * match a step name of 'wasp.job.aStep' in the database 
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus and/or exit status must match
	 * @param name (may be null)
	 * @param parameterMap (may be null)
	 * @Param exclusive (defaults to false if null)
	 * @param batchStatus (may be null)
	 * @param exitStatus (may be null)
	 * @return
	 */
	public List<StepExecution> getStepExecutions(String name, Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus, ExitStatus exitStatus);

	
	/**
	 * Obtains the list of job parameters for a given StepExecution
	 * @param stepExecution
	 * @return
	 */
	public JobParameters getJobParameters(StepExecution stepExecution);

}
