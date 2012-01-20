
/**
 *
 * WorkflowService.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.model.Workflow;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface WorkflowService extends WaspService<Workflow> {

	/**
	 * setWorkflowDao(WorkflowDao workflowDao)
	 *
	 * @param workflowDao
	 *
	 */
	public void setWorkflowDao(WorkflowDao workflowDao);

	/**
	 * getWorkflowDao();
	 *
	 * @return workflowDao
	 *
	 */
	public WorkflowDao getWorkflowDao();

  public Workflow getWorkflowByWorkflowId (final Integer workflowId);

  public Workflow getWorkflowByIName (final String iName);

  public Workflow getWorkflowByName (final String name);


}

