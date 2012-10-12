package edu.yu.einstein.wasp.batch.core.extension;

import java.util.List;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;

import edu.yu.einstein.wasp.batch.exceptions.BatchDaoDataRetrievalException;
import edu.yu.einstein.wasp.batch.exceptions.ParameterValueRetrievalException;

public interface JobExplorerWasp extends JobExplorer {
	
	// getStepExecution/s methods
	
	/**
	 * Expects a SINGLE StepExecution for the batch step identified by the provided step name and matching the provided parameters. Returns null if
	 * no results obtained or throws BatchDaoDataRetrievalException if more than one step matches the given name and parameter map.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public StepExecution getStepExecution(String name, Map<String, String> parameterMap, Boolean exclusive) throws BatchDaoDataRetrievalException;
	
	/**
	 * Expects a SINGLE StepExecution for the batch step identified by the provided parameters. Returns null if
	 * no results obtained or throws BatchDaoDataRetrievalException if more than one step matches the given name and parameter map.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap
	 * @param parameterMap
	 * @param exclusive
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public StepExecution getStepExecution(Map<String, String> parameterMap, Boolean exclusive) throws BatchDaoDataRetrievalException;
	
	/**
	 * Get ALL StepExecutions for the batch step identified by the provided step name and matching the provided parameters. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @return
	 */
	public List<StepExecution> getStepExecutions(String name, Map<String, String> parameterMap, Boolean exclusive);
	
	/**
	 * Get ALL StepExecutions for the batch step identified by the provided parameters. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus and/or exit status must match
	 * @param parameterMap
	 * @param exclusive
	 * @return
	 */
	public List<StepExecution> getStepExecutions(Map<String, String> parameterMap, Boolean exclusive);
	
	/**
	 * Get ALL StepExecutions for the batch step identified by the provided step name. 
	 * @param name
	 * @return
	 */
	public List<StepExecution> getStepExecutions(String name);
	
	/**
	 * Get ALL StepExecutions. Returns empty list if no results obtained.
	 */
	public List<StepExecution> getStepExecutions();
	
	/**
	 * Expects a SINGLE StepExecution for the batch step identified by the provided step name and matching the provided parameters. Returns null if
	 * no results obtained or throws BatchDaoDataRetrievalException if more than one step matches the given name and parameter map.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus and/or exit status must match
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @param batchStatus (may be null)
	 * @param exitStatus (may be null)
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public StepExecution getStepExecution(String name, Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus, ExitStatus exitStatus) throws BatchDaoDataRetrievalException;
	
	/**
	 * Expects a SINGLE StepExecution for the batch step identified by the provided parameters. Returns null if
	 * no results obtained or throws BatchDaoDataRetrievalException if more than one step matches the given name and parameter map.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus and/or exit status must match
	 * @param parameterMap
	 * @param exclusive
	 * @param batchStatus (may be null)
	 * @param exitStatus (may be null)
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public StepExecution getStepExecution(Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus, ExitStatus exitStatus) throws BatchDaoDataRetrievalException;
	
	/**
	 * Get ALL StepExecutions for the batch step identified by the provided step name and matching the provided parameters. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus and/or exit status must match
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @param batchStatus (may be null)
	 * @param exitStatus (may be null)
	 * @return
	 */
	public List<StepExecution> getStepExecutions(String name, Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus, ExitStatus exitStatus);
	
	/**
	 * Get ALL StepExecutions for the batch step identified by the provided parameters. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus and/or exit status must match
	 * @param parameterMap
	 * @param exclusive
	 * @param batchStatus (may be null)
	 * @param exitStatus (may be null)
	 * @return
	 */
	public List<StepExecution> getStepExecutions(Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus, ExitStatus exitStatus);
	
	/**
	 * Get ALL StepExecutions for the batch step identified by the provided step name. Returns empty list if no results obtained.
	 * If not null, the provided BatchStatus and/or exit status must match
	 * @param name
	 * @param batchStatus (may be null)
	 * @param exitStatus (may be null)
	 * @return
	 */
	public List<StepExecution> getStepExecutions(String name, BatchStatus batchStatus, ExitStatus exitStatus);
	
	/**
	 * Get ALL StepExecutions for the provided BatchStatus and exit status.
	 * @param batchStatus (may be null)
	 * @param exitStatus (may be null)
	 * @return
	 */
	public List<StepExecution> getStepExecutions(BatchStatus batchStatus, ExitStatus exitStatus);
	
		// batchStatus only
	
	/**
	 * Expects a SINGLE StepExecution for the batch step identified by the provided step name and matching the provided parameters. Returns null if
	 * no results obtained or throws BatchDaoDataRetrievalException if more than one step matches the given name and parameter map.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus must match
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @param batchStatus (may be null)
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public StepExecution getStepExecution(String name, Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus) throws BatchDaoDataRetrievalException;
	
	/**
	 * Expects a SINGLE StepExecution for the batch step identified by the provided parameters. Returns null if
	 * no results obtained or throws BatchDaoDataRetrievalException if more than one step matches the given name and parameter map.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus must match
	 * @param parameterMap
	 * @param exclusive
	 * @param batchStatus (may be null)
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public StepExecution getStepExecution(Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus) throws BatchDaoDataRetrievalException;
	
	/**
	 * Get ALL StepExecutions for the batch step identified by the provided step name and matching the provided parameters. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus  must match
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @param batchStatus (may be null)
	 * @return
	 */
	public List<StepExecution> getStepExecutions(String name, Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus);
	
	/**
	 * Get ALL StepExecutions for the batch step identified by the provided parameters. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus must match
	 * @param parameterMap
	 * @param exclusive
	 * @param batchStatus (may be null)
	 * @return
	 */
	public List<StepExecution> getStepExecutions(Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus);
	
	/**
	 * Get ALL StepExecutions for the batch step identified by the provided step name. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus must match
	 * @param name 
	 * @param batchStatus (may be null)
	 * @return
	 */
	public List<StepExecution> getStepExecutions(String name, BatchStatus batchStatus);
	
	/**
	 * Get ALL StepExecutions for the provided BatchStatus
	 * @param batchStatus (may be null)
	 * @return
	 */
	public List<StepExecution> getStepExecutions(BatchStatus batchStatus);
	
		// exit Status only
	
	/**
	 * Expects a SINGLE StepExecution for the batch step identified by the provided step name and matching the provided parameters. Returns null if
	 * no results obtained or throws BatchDaoDataRetrievalException if more than one step matches the given name and parameter map.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided exit status must match
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @param exitStatus (may be null)
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public StepExecution getStepExecution(String name, Map<String, String> parameterMap, Boolean exclusive, ExitStatus exitStatus) throws BatchDaoDataRetrievalException;
	
	/**
	 * Expects a SINGLE StepExecution for the batch step identified by the provided parameters. Returns null if
	 * no results obtained or throws BatchDaoDataRetrievalException if more than one step matches the given name and parameter map.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided exit status must match
	 * @param parameterMap
	 * @param exclusive
	 * @param exitStatus (may be null)
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public StepExecution getStepExecution(Map<String, String> parameterMap, Boolean exclusive, ExitStatus exitStatus) throws BatchDaoDataRetrievalException;
	
	/**
	 * Get ALL StepExecutions for the batch step identified by the provided step name and matching the provided parameters. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided exit status must match
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @param exitStatus (may be null)
	 * @return
	 */
	public List<StepExecution> getStepExecutions(String name, Map<String, String> parameterMap, Boolean exclusive, ExitStatus exitStatus);
	
	/**
	 * Get ALL StepExecutions for the batch step identified by the provided parameters. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided exit status must match
	 * @param parameterMap
	 * @param exclusive
	 * @param exitStatus (may be null)
	 * @return
	 */
	public List<StepExecution> getStepExecutions(Map<String, String> parameterMap, Boolean exclusive, ExitStatus exitStatus);
	
	/**
	 * Get ALL StepExecutions for the batch step identified by the provided step name. Returns empty list if
	 * no results obtained.
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided exit status must match
	 * @param name
	 * @param exitStatus (may be null)
	 * @return
	 */
	public List<StepExecution> getStepExecutions(String name, ExitStatus exitStatus);
	
	/**
	 * Get ALL StepExecutions for the provided exit status.
	 * @param exitStatus (may be null)
	 * @return
	 */
	public List<StepExecution> getStepExecutions(ExitStatus exitStatus);

	// getJobExecution/s methods
	
	/**
	 * Expects a SINGLE JobExecution for the batch step identified by the provided step name and matching the provided parameters. Returns null if
	 * no results obtained or throws BatchDaoDataRetrievalException if more than one step matches the given name and parameter map.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public JobExecution getJobExecution(String name, Map<String, String> parameterMap, Boolean exclusive) throws BatchDaoDataRetrievalException;
	
	/**
	 * Expects a SINGLE JobExecution for the batch step identified by the provided parameters. Returns null if
	 * no results obtained or throws BatchDaoDataRetrievalException if more than one step matches the given name and parameter map.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap
	 * @param parameterMap
	 * @param exclusive
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public JobExecution getJobExecution(Map<String, String> parameterMap, Boolean exclusive) throws BatchDaoDataRetrievalException;
	
	/**
	 * Get ALL JobExecutions for the batch step identified by the provided step name and matching the provided parameters. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @return
	 */
	public List<JobExecution> getJobExecutions(String name, Map<String, String> parameterMap, Boolean exclusive);
	
	/**
	 * Get ALL JobExecutions for the batch step identified by the provided parameters. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus and/or exit status must match
	 * @param parameterMap
	 * @param exclusive
	 * @return
	 */
	public List<JobExecution> getJobExecutions(Map<String, String> parameterMap, Boolean exclusive);
	
	/**
	 * Get ALL JobExecutions for the batch step identified by the provided step name. 
	 * @param name
	 * @return
	 */
	public List<JobExecution> getJobExecutions(String name);
	
	/**
	 * Get ALL JobExecutions. Returns empty list if no results obtained.
	 */
	public List<JobExecution> getJobExecutions();
	
	/**
	 * Expects a SINGLE JobExecution for the batch step identified by the provided step name and matching the provided parameters. Returns null if
	 * no results obtained or throws BatchDaoDataRetrievalException if more than one step matches the given name and parameter map.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus and/or exit status must match
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @param batchStatus (may be null)
	 * @param exitStatus (may be null)
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public JobExecution getJobExecution(String name, Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus, ExitStatus exitStatus) throws BatchDaoDataRetrievalException;
	
	/**
	 * Expects a SINGLE JobExecution for the batch step identified by the provided parameters. Returns null if
	 * no results obtained or throws BatchDaoDataRetrievalException if more than one step matches the given name and parameter map.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus and/or exit status must match
	 * @param parameterMap
	 * @param exclusive
	 * @param batchStatus (may be null)
	 * @param exitStatus (may be null)
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public JobExecution getJobExecution(Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus, ExitStatus exitStatus) throws BatchDaoDataRetrievalException;
	
	/**
	 * Get ALL JobExecutions for the batch step identified by the provided step name and matching the provided parameters. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus and/or exit status must match
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @param batchStatus (may be null)
	 * @param exitStatus (may be null)
	 * @return
	 */
	public List<JobExecution> getJobExecutions(String name, Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus, ExitStatus exitStatus);
	
	/**
	 * Get ALL JobExecutions for the batch step identified by the provided parameters. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus and/or exit status must match
	 * @param parameterMap
	 * @param exclusive
	 * @param batchStatus (may be null)
	 * @param exitStatus (may be null)
	 * @return
	 */
	public List<JobExecution> getJobExecutions(Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus, ExitStatus exitStatus);
	
	/**
	 * Get ALL JobExecution for the batch step identified by the provided job name. Returns empty list if no results obtained.
	 * If not null, the provided BatchStatus and/or exit status must match
	 * @param name
	 * @param batchStatus (may be null)
	 * @param exitStatus (may be null)
	 * @return
	 */
	public List<JobExecution> getJobExecutions(String name, BatchStatus batchStatus, ExitStatus exitStatus);
	
	/**
	 * Get ALL JobExecution for the batch step identified by the provided BatchStatus and exit status
	 * @param batchStatus (may be null)
	 * @param exitStatus (may be null)
	 * @return
	 */
	public List<JobExecution> getJobExecutions(BatchStatus batchStatus, ExitStatus exitStatus);
	
		// batchStatus only
	
	/**
	 * Expects a SINGLE JobExecution for the batch step identified by the provided step name and matching the provided parameters. Returns null if
	 * no results obtained or throws BatchDaoDataRetrievalException if more than one step matches the given name and parameter map.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus must match
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @param batchStatus (may be null)
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public JobExecution getJobExecution(String name, Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus) throws BatchDaoDataRetrievalException;
	
	/**
	 * Expects a SINGLE JobExecution for the batch step identified by the provided parameters. Returns null if
	 * no results obtained or throws BatchDaoDataRetrievalException if more than one step matches the given name and parameter map.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus must match
	 * @param parameterMap
	 * @param exclusive
	 * @param batchStatus (may be null)
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public JobExecution getJobExecution(Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus) throws BatchDaoDataRetrievalException;
	
	/**
	 * Get ALL JobExecutions for the batch step identified by the provided step name and matching the provided parameters. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus  must match
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @param batchStatus (may be null)
	 * @return
	 */
	public List<JobExecution> getJobExecutions(String name, Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus);
	
	/**
	 * Get ALL JobExecutions for the batch step identified by the provided parameters. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus must match
	 * @param parameterMap
	 * @param exclusive
	 * @param batchStatus (may be null)
	 * @return
	 */
	public List<JobExecution> getJobExecutions(Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus);
	
	/**
	 * Get ALL JobExecutions for the batch step identified by the provided job name. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided BatchStatus must match
	 * @param name 
	 * @param batchStatus (may be null)
	 * @return
	 */
	public List<JobExecution> getJobExecutions(String name, BatchStatus batchStatus);
	
	/**
	 * Get ALL JobExecutions with the provided BatchStatus
	 * @param batchStatus (may be null)
	 * @return
	 */
	public List<JobExecution> getJobExecutions(BatchStatus batchStatus);
	
		// exit Status only
	
	/**
	 * Expects a SINGLE JobExecution for the batch step identified by the provided step name and matching the provided parameters. Returns null if
	 * no results obtained or throws BatchDaoDataRetrievalException if more than one step matches the given name and parameter map.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided exit status must match
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @param exitStatus (may be null)
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public JobExecution getJobExecution(String name, Map<String, String> parameterMap, Boolean exclusive, ExitStatus exitStatus) throws BatchDaoDataRetrievalException;
	
	/**
	 * Expects a SINGLE JobExecution for the batch step identified by the provided parameters. Returns null if
	 * no results obtained or throws BatchDaoDataRetrievalException if more than one step matches the given name and parameter map.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided exit status must match
	 * @param parameterMap
	 * @param exclusive
	 * @param exitStatus (may be null)
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public JobExecution getJobExecution(Map<String, String> parameterMap, Boolean exclusive, ExitStatus exitStatus) throws BatchDaoDataRetrievalException;
	
	/**
	 * Get ALL JobExecutions for the batch step identified by the provided step name and matching the provided parameters. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided exit status must match
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @param exitStatus (may be null)
	 * @return
	 */
	public List<JobExecution> getJobExecutions(String name, Map<String, String> parameterMap, Boolean exclusive, ExitStatus exitStatus);
	
	/**
	 * Get ALL JobExecutions for the batch step identified by the provided parameters. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided exit status must match
	 * @param parameterMap
	 * @param exclusive
	 * @param exitStatus (may be null)
	 * @return
	 */
	public List<JobExecution> getJobExecutions(Map<String, String> parameterMap, Boolean exclusive, ExitStatus exitStatus);
	
	/**
	 * Get ALL JobExecutions for the batch step identified by the provided job name. Returns empty list if
	 * no results obtained.
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided exit status must match
	 * @param name
	 * @param exitStatus (may be null)
	 * @return
	 */
	public List<JobExecution> getJobExecutions(String name, ExitStatus exitStatus);
	
	/**
	 * Get ALL JobExecutions for the provided exit status must match
	 * @param name
	 * @param exitStatus (may be null)
	 * @return
	 */
	public List<JobExecution> getJobExecutions(ExitStatus exitStatus);
	
	// other methods
	
	/**
	 * Get all the job instances that match the parameters provided as a Map.
	 * @param parameterMap
	 * @return
	 */
	public List<JobInstance> getJobInstancesMatchingParameters(Map<String, String> parameterMap);
	
	/**
	 * Given a parameter map key, attempt to get the requested parameter and cast to requested type.
	 * @param se
	 * @param key
	 * @param clazz
	 * @return
	 * @throws ParameterValueRetrievalException
	 */
	String getJobParameterValueByKey(StepExecution se, String key) throws ParameterValueRetrievalException;
	
	

}
