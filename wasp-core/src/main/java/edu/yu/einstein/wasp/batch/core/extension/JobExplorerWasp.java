package edu.yu.einstein.wasp.batch.core.extension;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;

import edu.yu.einstein.wasp.exception.BatchDaoDataRetrievalException;
import edu.yu.einstein.wasp.exception.ParameterValueRetrievalException;

/**
 * 
 * @author asmclellan
 *
 */
public interface JobExplorerWasp extends JobExplorer {
	
	// getStepExecution/s methods
	
	/**
	 * Expects a SINGLE StepExecution for the batch step identified by the provided step name and matching the provided parameters. Returns null if
	 * no results obtained or throws BatchDaoDataRetrievalException if more than one step matches the given name and parameter map.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap
	 * The name match is a prefix OR suffix match so 'aStep' or 'job.aStep' or or 'wasp.job' or 'wasp.job.aStep' will all match 
	 * a step name of 'wasp.job.aStep' in the database 
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public StepExecution getStepExecution(String name, Map<String, Set<String>> parameterMap, Boolean exclusive) throws BatchDaoDataRetrievalException;
	
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
	public StepExecution getStepExecution(Map<String, Set<String>> parameterMap, Boolean exclusive) throws BatchDaoDataRetrievalException;
	
	/**
	 * Get ALL StepExecutions for the batch step identified by the provided step name and matching the provided parameters. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap
	 * The name match is a prefix OR suffix match so 'aStep' or 'job.aStep' or or 'wasp.job' or 'wasp.job.aStep' will all match 
	 * a step name of 'wasp.job.aStep' in the database 
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @return
	 */
	public List<StepExecution> getStepExecutions(String name, Map<String, Set<String>> parameterMap, Boolean exclusive);
	
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
	public List<StepExecution> getStepExecutions(Map<String, Set<String>> parameterMap, Boolean exclusive);
	
	/**
	 * Get ALL StepExecutions for the batch step identified by the provided step name. 
	 * The name match is a prefix OR suffix match so 'aStep' or 'job.aStep' or or 'wasp.job' or 'wasp.job.aStep' will all match 
	 * a step name of 'wasp.job.aStep' in the database 
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
	 * If not null, the provided exit status must match
	 * The name match is a prefix OR suffix match so 'aStep' or 'job.aStep' or or 'wasp.job' or 'wasp.job.aStep' will all match 
	 * a step name of 'wasp.job.aStep' in the database 
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @param exitStatus (may be null)
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public StepExecution getStepExecution(String name, Map<String, Set<String>> parameterMap, Boolean exclusive, ExitStatus exitStatus) throws BatchDaoDataRetrievalException;
	
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
	public StepExecution getStepExecution(Map<String, Set<String>> parameterMap, Boolean exclusive, ExitStatus exitStatus) throws BatchDaoDataRetrievalException;
	
	/**
	 * Get ALL StepExecutions for the batch step identified by the provided step name and matching the provided parameters. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided exit status must match
	 * The name match is a prefix OR suffix match so 'aStep' or 'job.aStep' or or 'wasp.job' or 'wasp.job.aStep' will all match 
	 * a step name of 'wasp.job.aStep' in the database 
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @param exitStatus (may be null)
	 * @return
	 */
	public List<StepExecution> getStepExecutions(String name, Map<String, Set<String>> parameterMap, Boolean exclusive, ExitStatus exitStatus);
	
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
	public List<StepExecution> getStepExecutions(Map<String, Set<String>> parameterMap, Boolean exclusive, ExitStatus exitStatus);
	
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
	 * The name match is a prefix OR suffix match so 'aStep' or 'job.aStep' or or 'wasp.job' or 'wasp.job.aStep' will all match 
	 * a step name of 'wasp.job.aStep' in the database 
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public JobExecution getJobExecution(String name, Map<String, Set<String>> parameterMap, Boolean exclusive) throws BatchDaoDataRetrievalException;
	
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
	public JobExecution getJobExecution(Map<String, Set<String>> parameterMap, Boolean exclusive) throws BatchDaoDataRetrievalException;
	
	/**
	 * Get ALL JobExecutions for the batch step identified by the provided step name and matching the provided parameters. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap
	 * The name match is a prefix OR suffix match so 'aStep' or 'job.aStep' or or 'wasp.job' or 'wasp.job.aStep' will all match 
	 * a step name of 'wasp.job.aStep' in the database 
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @return
	 */
	public List<JobExecution> getJobExecutions(String name, Map<String, Set<String>> parameterMap, Boolean exclusive);
	
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
	public List<JobExecution> getJobExecutions(Map<String, Set<String>> parameterMap, Boolean exclusive);
	
	/**
	 * Get ALL JobExecutions for the batch step identified by the provided step name. 
	 * The name match is a prefix OR suffix match so 'aStep' or 'job.aStep' or or 'wasp.job' or 'wasp.job.aStep' will all match 
	 * a step name of 'wasp.job.aStep' in the database 
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
	 * If not null, the provided exit status must match
	 * The name match is a prefix OR suffix match so 'aStep' or 'job.aStep' or or 'wasp.job' or 'wasp.job.aStep' will all match 
	 * a step name of 'wasp.job.aStep' in the database 
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @param exitStatus (may be null)
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public JobExecution getJobExecution(String name, Map<String, Set<String>> parameterMap, Boolean exclusive, ExitStatus exitStatus) throws BatchDaoDataRetrievalException;
	
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
	public JobExecution getJobExecution(Map<String, Set<String>> parameterMap, Boolean exclusive, ExitStatus exitStatus) throws BatchDaoDataRetrievalException;
	
	/**
	 * Get ALL JobExecutions for the batch step identified by the provided step name and matching the provided parameters. Returns empty list if
	 * no results obtained.
	 * If 'exclusive' is false, a subset of the job parameters may be provided and others are ignored, otherwise
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided exit status must match
	 * The name match is a prefix OR suffix match so 'aStep' or 'job.aStep' or or 'wasp.job' or 'wasp.job.aStep' will all match 
	 * a step name of 'wasp.job.aStep' in the database 
	 * @param name
	 * @param parameterMap
	 * @param exclusive
	 * @param exitStatus (may be null)
	 * @return
	 */
	public List<JobExecution> getJobExecutions(String name, Map<String, Set<String>> parameterMap, Boolean exclusive, ExitStatus exitStatus);
	
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
	public List<JobExecution> getJobExecutions(Map<String, Set<String>> parameterMap, Boolean exclusive, ExitStatus exitStatus);
	
	/**
	 * Get ALL JobExecutions for the batch step identified by the provided job name. Returns empty list if
	 * no results obtained.
	 * if true, there must be an exact correlation between all job parameters and those in parameterMap.
	 * If not null, the provided exit status must match
	 * The name match is a prefix OR suffix match so 'aStep' or 'job.aStep' or or 'wasp.job' or 'wasp.job.aStep' will all match 
	 * a step name of 'wasp.job.aStep' in the database 
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
	 * Get all the job executions that match the parameters provided as a Map.
	 * @param parameterMap
	 * @return
	 */
	public List<JobExecution> getJobExecutionsMatchingParameters(Map<String, Set<String>> parameterMap);
	
	/**
	 * Given a parameter map key, attempt to get the requested parameter and cast to requested type.
	 * @param se
	 * @param key
	 * @param clazz
	 * @return
	 * @throws ParameterValueRetrievalException
	 */
	String getJobParameterValueByKey(StepExecution se, String key) throws ParameterValueRetrievalException;

	
	/**
	 * Given a parameter map key, attempt to get the requested parameter and cast to requested type.
	 * @param je
	 * @param key
	 * @param clazz
	 * @return
	 * @throws ParameterValueRetrievalException
	 */
	String getJobParameterValueByKey(JobExecution je, String key) throws ParameterValueRetrievalException;
	
	/**
	 * Get the most recently started StepExecution from the provided list or null.
	 * @param stepExecutions
	 * @return
	 */
	public StepExecution getMostRecentlyStartedStepExecutionInList(List<StepExecution> stepExecutions);
	
	/**
	 * Get the most recently started JobExecution from the provided list or null.
	 * @param stepExecutions
	 * @return
	 */
	public JobExecution getMostRecentlyStartedJobExecutionInList(List<JobExecution> jobExecutions);
	
	/**
	 * Obtains job name for a given JobExecution
	 * @param jobExecution
	 * @return
	 */
	public String getJobName(JobExecution jobExecution);

}
