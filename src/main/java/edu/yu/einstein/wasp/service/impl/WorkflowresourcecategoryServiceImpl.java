
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

import edu.yu.einstein.wasp.service.WorkflowresourcecategoryService;
import edu.yu.einstein.wasp.dao.WorkflowresourcecategoryDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Workflowresourcecategory;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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
	public WorkflowresourcecategoryDao getWorkflowresourcecategoryDao() {
		return this.workflowresourcecategoryDao;
	}


  public Workflowresourcecategory getWorkflowresourcecategoryByWorkflowresourcecategoryId (final Integer workflowresourcecategoryId) {
    return this.getWorkflowresourcecategoryDao().getWorkflowresourcecategoryByWorkflowresourcecategoryId(workflowresourcecategoryId);
  }

  public Workflowresourcecategory getWorkflowresourcecategoryByWorkflowIdResourcecategoryId (final Integer workflowId, final Integer resourcecategoryId) {
    return this.getWorkflowresourcecategoryDao().getWorkflowresourcecategoryByWorkflowIdResourcecategoryId(workflowId, resourcecategoryId);
  }

}

