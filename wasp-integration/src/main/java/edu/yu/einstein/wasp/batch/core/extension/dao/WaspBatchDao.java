package edu.yu.einstein.wasp.batch.core.extension.dao;

import java.util.List;
import java.util.Map;

public interface WaspBatchDao {
	
	/**
	 * Get the ids of all the job instances that match the parameters provided as a Map.
	 * @param parameterMap
	 * @return
	 */
	public List<Long> getJobInstanceIdsByMatchingParameters(Map<String, String> parameterMap);  

}
