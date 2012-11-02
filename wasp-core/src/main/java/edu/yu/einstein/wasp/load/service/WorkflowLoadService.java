package edu.yu.einstein.wasp.load.service;

import java.util.List;
import java.util.Set;

import edu.yu.einstein.wasp.model.WorkflowMeta;

public interface WorkflowLoadService extends WaspLoadService {

	public void update(String iname, String name, Integer isActive, List<WorkflowMeta> meta, List<String> dependencies, 
			Set<String> sampleSubtypes, List<String> pageFlowOrder, String jobFlowBatchJobName);
	
}
