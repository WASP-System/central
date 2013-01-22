package edu.yu.einstein.wasp.service;

import java.util.List;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.Workflow;

public interface WorkflowService extends WaspService {
	
	/**
	 * Get name of Batch Flow Job specified for the current workflow.
	 * Returns null if not found.
	 * @return
	 */
	public String getJobFlowBatchJobName(Workflow workflow);
	
	/**
	 * Set name of Batch Flow Job specified for the current workflow. 
	 * @param workflow
	 * @param name
	 * @throws MetadataException 
	 */
	public void setJobFlowBatchJobName(Workflow workflow, String name) throws MetadataException;
	
	/**
	 * Get the list of pages specified for the current workflow. 
	 * Returns an empty array if nothing found.
	 * @param workflow
	 * @return
	 */
	public String[] getPageFlowOrder(Workflow workflow);
	
	/**
	 * Set the list of pages specified for the current workflow. 
	 * @param workflow
	 * @param pageList
	 * @throws MetadataException 
	 */
	public void setPageFlowOrder(Workflow workflow, List<String> pageList) throws MetadataException;
	
	

}
