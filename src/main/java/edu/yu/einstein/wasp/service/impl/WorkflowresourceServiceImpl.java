
/**
 *
 * WorkflowresourceServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowresourceService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.WorkflowresourceDao;
import edu.yu.einstein.wasp.model.Workflowresource;
import edu.yu.einstein.wasp.service.WorkflowresourceService;

@Service
public class WorkflowresourceServiceImpl extends WaspServiceImpl<Workflowresource> implements WorkflowresourceService {

	/**
	 * workflowresourceDao;
	 *
	 */
	private WorkflowresourceDao workflowresourceDao;

	/**
	 * setWorkflowresourceDao(WorkflowresourceDao workflowresourceDao)
	 *
	 * @param workflowresourceDao
	 *
	 */
	@Override
	@Autowired
	public void setWorkflowresourceDao(WorkflowresourceDao workflowresourceDao) {
		this.workflowresourceDao = workflowresourceDao;
		this.setWaspDao(workflowresourceDao);
	}

	/**
	 * getWorkflowresourceDao();
	 *
	 * @return workflowresourceDao
	 *
	 */
	@Override
	public WorkflowresourceDao getWorkflowresourceDao() {
		return this.workflowresourceDao;
	}


  @Override
public Workflowresource getWorkflowresourceByWorkflowresourceId (final int workflowresourceId) {
    return this.getWorkflowresourceDao().getWorkflowresourceByWorkflowresourceId(workflowresourceId);
  }

  @Override
public Workflowresource getWorkflowresourceByWorkflowIdResourceId (final int workflowId, final int resourceId) {
    return this.getWorkflowresourceDao().getWorkflowresourceByWorkflowIdResourceId(workflowId, resourceId);
  }

}

