
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

import edu.yu.einstein.wasp.service.WorkflowresourceService;
import edu.yu.einstein.wasp.dao.WorkflowresourceDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Workflowresource;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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
	public WorkflowresourceDao getWorkflowresourceDao() {
		return this.workflowresourceDao;
	}


  public Workflowresource getWorkflowresourceByWorkflowresourceId (final Integer workflowresourceId) {
    return this.getWorkflowresourceDao().getWorkflowresourceByWorkflowresourceId(workflowresourceId);
  }

  public Workflowresource getWorkflowresourceByWorkflowIdResourceId (final Integer workflowId, final Integer resourceId) {
    return this.getWorkflowresourceDao().getWorkflowresourceByWorkflowIdResourceId(workflowId, resourceId);
  }

}

