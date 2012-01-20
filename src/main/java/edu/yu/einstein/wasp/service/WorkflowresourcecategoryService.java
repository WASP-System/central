
/**
 *
 * WorkflowresourcecategoryService.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowresourcecategoryService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.WorkflowresourcecategoryDao;
import edu.yu.einstein.wasp.model.Workflowresourcecategory;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface WorkflowresourcecategoryService extends WaspService<Workflowresourcecategory> {

	/**
	 * setWorkflowresourcecategoryDao(WorkflowresourcecategoryDao workflowresourcecategoryDao)
	 *
	 * @param workflowresourcecategoryDao
	 *
	 */
	public void setWorkflowresourcecategoryDao(WorkflowresourcecategoryDao workflowresourcecategoryDao);

	/**
	 * getWorkflowresourcecategoryDao();
	 *
	 * @return workflowresourcecategoryDao
	 *
	 */
	public WorkflowresourcecategoryDao getWorkflowresourcecategoryDao();

  public Workflowresourcecategory getWorkflowresourcecategoryByWorkflowresourcecategoryId (final Integer workflowresourcecategoryId);

  public Workflowresourcecategory getWorkflowresourcecategoryByWorkflowIdResourcecategoryId (final Integer workflowId, final Integer resourcecategoryId);


}

