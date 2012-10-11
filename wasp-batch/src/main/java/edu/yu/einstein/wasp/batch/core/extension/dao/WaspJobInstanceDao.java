package edu.yu.einstein.wasp.batch.core.extension.dao;

import java.util.List;
import java.util.Map;

import org.springframework.batch.core.repository.dao.JobInstanceDao;

public interface WaspJobInstanceDao extends JobInstanceDao{
	
	/**
	 * Get the ids of all the job instances that match the parameters provided as a Map.
	 * Will match a subset of the parameters associated with the job instance.
	 * @param parameterMap
	 * @return
	 */
	public List<Long> getJobInstanceIdsByMatchingParameters(Map<String, String> parameterMap);  
	
	/**
	 * Get the ids of all the job instances that match the parameters provided as a Map.
	 * Only returns jobInstanceIds from job instances with an exact match to all the parameters
	 * associated with that job instance.
	 * @param parameterMap
	 * @return
	 */
	public List<Long> getJobInstanceIdsByExclusivelyMatchingParameters(Map<String, String> parameterMap);  
	
	
	

}
