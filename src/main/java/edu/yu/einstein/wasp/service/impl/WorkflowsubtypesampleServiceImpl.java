
/**
 *
 * WorkflowsubtypesampleServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowsubtypesampleService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.WorkflowsubtypesampleDao;
import edu.yu.einstein.wasp.model.Workflowsubtypesample;
import edu.yu.einstein.wasp.service.WorkflowsubtypesampleService;

@Service
public class WorkflowsubtypesampleServiceImpl extends WaspServiceImpl<Workflowsubtypesample> implements WorkflowsubtypesampleService {

	/**
	 * workflowsubtypesampleDao;
	 *
	 */
	private WorkflowsubtypesampleDao workflowsubtypesampleDao;

	/**
	 * setWorkflowsubtypesampleDao(WorkflowsubtypesampleDao workflowsubtypesampleDao)
	 *
	 * @param workflowsubtypesampleDao
	 *
	 */
	@Override
	@Autowired
	public void setWorkflowsubtypesampleDao(WorkflowsubtypesampleDao workflowsubtypesampleDao) {
		this.workflowsubtypesampleDao = workflowsubtypesampleDao;
		this.setWaspDao(workflowsubtypesampleDao);
	}

	/**
	 * getWorkflowsubtypesampleDao();
	 *
	 * @return workflowsubtypesampleDao
	 *
	 */
	@Override
	public WorkflowsubtypesampleDao getWorkflowsubtypesampleDao() {
		return this.workflowsubtypesampleDao;
	}


  @Override
public Workflowsubtypesample getWorkflowsubtypesampleByWorkflowsubtypesampleId (final int workflowsubtypesampleId) {
    return this.getWorkflowsubtypesampleDao().getWorkflowsubtypesampleByWorkflowsubtypesampleId(workflowsubtypesampleId);
  }

  @Override
public Workflowsubtypesample getWorkflowsubtypesampleByWorkflowIdSubtypeSampleId (final int workflowId, final int subtypeSampleId) {
    return this.getWorkflowsubtypesampleDao().getWorkflowsubtypesampleByWorkflowIdSubtypeSampleId(workflowId, subtypeSampleId);
  }

}

