package edu.yu.einstein.wasp.service;

import java.util.List;
import java.util.Map;

import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;

public interface JobExplorerWasp extends JobExplorer {
	
	/**
	 * Get all the job instances that match the parameters provided as a Map.
	 * @param parameterMap
	 * @return
	 */
	public List<JobInstance> getJobInstancesMatchingParameters(Map<String, String> parameterMap);

}
