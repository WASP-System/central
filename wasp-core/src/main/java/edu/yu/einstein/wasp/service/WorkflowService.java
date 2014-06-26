package edu.yu.einstein.wasp.service;

import java.util.List;
import java.util.Map;

import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.dao.WorkflowMetaDao;
import edu.yu.einstein.wasp.dao.WorkflowSoftwareDao;
import edu.yu.einstein.wasp.dao.WorkflowresourcecategoryDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.MetaAttribute.Control.Option;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Software;
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

	/**
	 * retrun list of workflows
	 * @return
	 */
	public List<Workflow> getWorkflows();

	public void setWorkflowMetaDao(WorkflowMetaDao workflowMetaDao);

	public WorkflowMetaDao getWorkflowMetaDao();

	public void setWorkflowDao(WorkflowDao workflowDao);

	public WorkflowDao getWorkflowDao();

	public WorkflowresourcecategoryDao getWorkflowresourcecategoryDao();

	public void setWorkflowresourcecategoryDao(WorkflowresourcecategoryDao workflowresourcecategoryDao);

	/**
	 * 
	 * @param workflow
	 * @param resourceCategory
	 * @return
	 * @throws MetadataException
	 */
	public Map<String, List<Option>> getConfiguredOptions(Workflow workflow, ResourceCategory resourceCategory) throws MetadataException;
	
	/**
	 * 
	 * @param workflow
	 * @param resourceCategory
	 * @return
	 * @throws MetadataException
	 */
	public Map<String, List<Option>> getConfiguredOptions(Workflow workflow, Software software) throws MetadataException;

	public WorkflowSoftwareDao getWorkflowSoftwareDao();

	public void setWorkflowSoftwareDao(WorkflowSoftwareDao workflowSoftwareDao);

	
	

}
