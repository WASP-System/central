
/**
 *
 * WorkflowresourcecategoryServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowresourcecategoryService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.WorkflowresourcecategoryDao;
import edu.yu.einstein.wasp.model.Workflowresourcecategory;
import edu.yu.einstein.wasp.service.WorkflowresourcecategoryService;

@Service
public class WorkflowresourcecategoryServiceImpl extends WaspServiceImpl<Workflowresourcecategory> implements WorkflowresourcecategoryService {

	/**
	 * workflowresourcecategoryDao;
	 *
	 */
	private WorkflowresourcecategoryDao workflowresourcecategoryDao;

	/**
	 * setWorkflowresourcecategoryDao(WorkflowresourcecategoryDao workflowresourcecategoryDao)
	 *
	 * @param workflowresourcecategoryDao
	 *
	 */
	@Override
	@Autowired
	public void setWorkflowresourcecategoryDao(WorkflowresourcecategoryDao workflowresourcecategoryDao) {
		this.workflowresourcecategoryDao = workflowresourcecategoryDao;
		this.setWaspDao(workflowresourcecategoryDao);
	}

	/**
	 * getWorkflowresourcecategoryDao();
	 *
	 * @return workflowresourcecategoryDao
	 *
	 */
	@Override
	public WorkflowresourcecategoryDao getWorkflowresourcecategoryDao() {
		return this.workflowresourcecategoryDao;
	}


  @Override
public Workflowresourcecategory getWorkflowresourcecategoryByWorkflowresourcecategoryId (final Integer workflowresourcecategoryId) {
    return this.getWorkflowresourcecategoryDao().getWorkflowresourcecategoryByWorkflowresourcecategoryId(workflowresourcecategoryId);
  }

  @Override
public Workflowresourcecategory getWorkflowresourcecategoryByWorkflowIdResourcecategoryId (final Integer workflowId, final Integer resourcecategoryId) {
    return this.getWorkflowresourcecategoryDao().getWorkflowresourcecategoryByWorkflowIdResourcecategoryId(workflowId, resourcecategoryId);
  }

}

