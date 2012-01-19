
/**
 *
 * WorkflowresourceService.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowresourceService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.WorkflowresourceDao;
import edu.yu.einstein.wasp.model.Workflowresource;

@Service
public interface WorkflowresourceService extends WaspService<Workflowresource> {

	/**
	 * setWorkflowresourceDao(WorkflowresourceDao workflowresourceDao)
	 *
	 * @param workflowresourceDao
	 *
	 */
	public void setWorkflowresourceDao(WorkflowresourceDao workflowresourceDao);

	/**
	 * getWorkflowresourceDao();
	 *
	 * @return workflowresourceDao
	 *
	 */
	public WorkflowresourceDao getWorkflowresourceDao();

  public Workflowresource getWorkflowresourceByWorkflowresourceId (final Integer workflowresourceId);

  public Workflowresource getWorkflowresourceByWorkflowIdResourceId (final Integer workflowId, final Integer resourceId);


}

