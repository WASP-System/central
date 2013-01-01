package edu.yu.einstein.wasp.load.service;

import java.util.List;

import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;

public interface WorkflowLoadService extends WaspLoadService {

	
	public Workflow update(String iname, String name, int isActive,	List<WorkflowMeta> meta, List<ResourceType> dependencies, List<SampleSubtype> sampleSubtypes, List<String> pageFlowOrder,
			String jobFlowBatchJobName);
	
}
