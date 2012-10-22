package edu.yu.einstein.wasp.service;

import java.util.List;

import edu.yu.einstein.wasp.model.Workflow;

public interface WorkflowService extends WaspService {
	
	/**
	 * Get name of Batch Flow Job specified for the current workflow.
	 * @return
	 */
	public String getJobFlowBatchJobName(Workflow workflow);
	
	/**
	 * Set name of Batch Flow Job specified for the current workflow. 
	 * @param workflow
	 * @param name
	 */
	public void setJobFlowBatchJobName(Workflow workflow, String name);
	
	/**
	 * Get the list of pages specified for the current workflow. 
	 * @param workflow
	 * @return
	 */
	public List<String> getPageFlowOrder(Workflow workflow);
	
	/**
	 * Set the list of pages specified for the current workflow. 
	 * @param workflow
	 * @param pageList
	 */
	public void setPageFlowOrder(Workflow workflow, List<String> pageList);
	
	

}
