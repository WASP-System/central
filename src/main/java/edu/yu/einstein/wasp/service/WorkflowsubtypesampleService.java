
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

import edu.yu.einstein.wasp.dao.WorkflowsubtypesampleDao;
import edu.yu.einstein.wasp.model.Workflowsubtypesample;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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

  public Workflowsubtypesample getWorkflowsubtypesampleByWorkflowsubtypesampleId (final int workflowsubtypesampleId);

  public Workflowsubtypesample getWorkflowsubtypesampleByWorkflowIdSubtypeSampleId (final int workflowId, final int subtypeSampleId);


}

