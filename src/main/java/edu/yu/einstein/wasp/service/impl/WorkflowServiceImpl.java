
/**
 *
 * WorkflowServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Workflow;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkflowServiceImpl extends WaspServiceImpl<Workflow> implements WorkflowService {

	/**
	 * workflowDao;
	 *
	 */
	private WorkflowDao workflowDao;

	/**
	 * setWorkflowDao(WorkflowDao workflowDao)
	 *
	 * @param workflowDao
	 *
	 */
	@Autowired
	public void setWorkflowDao(WorkflowDao workflowDao) {
		this.workflowDao = workflowDao;
		this.setWaspDao(workflowDao);
	}

	/**
	 * getWorkflowDao();
	 *
	 * @return workflowDao
	 *
	 */
	public WorkflowDao getWorkflowDao() {
		return this.workflowDao;
	}


  public Workflow getWorkflowByWorkflowId (final Integer workflowId) {
    return this.getWorkflowDao().getWorkflowByWorkflowId(workflowId);
  }

  public Workflow getWorkflowByIName (final String iName) {
    return this.getWorkflowDao().getWorkflowByIName(iName);
  }

  public Workflow getWorkflowByName (final String name) {
    return this.getWorkflowDao().getWorkflowByName(name);
  }

}

