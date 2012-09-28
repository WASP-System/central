package edu.yu.einstein.wasp.batch.core.extension;

import java.util.List;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;

import edu.yu.einstein.wasp.batch.exceptions.BatchDaoDataRetrievalException;

public interface JobExplorerWasp extends JobExplorer {
	
	/**
	 * Get all the job instances that match the parameters provided as a Map.
	 * @param parameterMap
	 * @return
	 */
	public List<JobInstance> getJobInstancesMatchingParameters(Map<String, String> parameterMap);
	
	/**
	 * Get StepExecution for the batch step identified by the provided step name and matching the provided parameters. Returns null if
	 * no results obtained or throws BatchDaoDataRetrievalException if more than one step matches the given name and parameter map.
	 *  
	 * @param name
	 * @param parameterMap
	 * @return
	 * @throws BatchDaoDataRetrievalException
	 */
	public StepExecution getStepExecutionByStepNameAndParameterMap(String name, Map<String, String> parameterMap) throws BatchDaoDataRetrievalException;

}
