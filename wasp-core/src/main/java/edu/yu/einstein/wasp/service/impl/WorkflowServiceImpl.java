
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.service.WorkflowService;

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
	@Override
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
	@Override
	public WorkflowDao getWorkflowDao() {
		return this.workflowDao;
	}


  @Override
public Workflow getWorkflowByWorkflowId (final Integer workflowId) {
    return this.getWorkflowDao().getWorkflowByWorkflowId(workflowId);
  }

  @Override
public Workflow getWorkflowByIName (final String iName) {
    return this.getWorkflowDao().getWorkflowByIName(iName);
  }

  @Override
public Workflow getWorkflowByName (final String name) {
    return this.getWorkflowDao().getWorkflowByName(name);
  }
  
  @Override
  public List<Workflow> getActiveWorkflows(){
	  Map workflowQueryMap = new HashMap();
	  workflowQueryMap.put("isActive", 1);
	  return this.findByMap(workflowQueryMap);
  }

}

