package edu.yu.einstein.wasp.batch.core.extension.dao;

import java.util.List;
import java.util.Map;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.dao.StepExecutionDao;

public interface WaspStepExecutionDao extends StepExecutionDao{
	
	
	/**
	 * Get the ids of all step executions that match the step name provided, and parameters provided as a Map.
	 * @param name
	 * @param parameterMap
	 * @return
	 */
	public List<StepExecution> getStepExecutionsByNameAndMatchingParameters(String name, Map<String, String> parameterMap);

}
