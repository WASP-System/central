
/**
 *
 * WorkflowsubtypesampleService.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowsubtypesampleService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.WorkflowsubtypesampleDao;
import edu.yu.einstein.wasp.model.Workflowsubtypesample;

@Service
public interface WorkflowsubtypesampleService extends WaspService<Workflowsubtypesample> {

	/**
	 * setWorkflowsubtypesampleDao(WorkflowsubtypesampleDao workflowsubtypesampleDao)
	 *
	 * @param workflowsubtypesampleDao
	 *
	 */
	public void setWorkflowsubtypesampleDao(WorkflowsubtypesampleDao workflowsubtypesampleDao);

	/**
	 * getWorkflowsubtypesampleDao();
	 *
	 * @return workflowsubtypesampleDao
	 *
	 */
	public WorkflowsubtypesampleDao getWorkflowsubtypesampleDao();

  public Workflowsubtypesample getWorkflowsubtypesampleByWorkflowsubtypesampleId (final Integer workflowsubtypesampleId);

  public Workflowsubtypesample getWorkflowsubtypesampleByWorkflowIdSubtypeSampleId (final Integer workflowId, final Integer subtypeSampleId);


}

