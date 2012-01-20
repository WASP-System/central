
/**
 *
 * WorkflowresourcecategoryMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowresourcecategoryMetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.WorkflowresourcecategoryMetaDao;
import edu.yu.einstein.wasp.model.WorkflowresourcecategoryMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface WorkflowresourcecategoryMetaService extends WaspService<WorkflowresourcecategoryMeta> {

	/**
	 * setWorkflowresourcecategoryMetaDao(WorkflowresourcecategoryMetaDao workflowresourcecategoryMetaDao)
	 *
	 * @param workflowresourcecategoryMetaDao
	 *
	 */
	public void setWorkflowresourcecategoryMetaDao(WorkflowresourcecategoryMetaDao workflowresourcecategoryMetaDao);

	/**
	 * getWorkflowresourcecategoryMetaDao();
	 *
	 * @return workflowresourcecategoryMetaDao
	 *
	 */
	public WorkflowresourcecategoryMetaDao getWorkflowresourcecategoryMetaDao();

  public WorkflowresourcecategoryMeta getWorkflowresourcecategoryMetaByWorkflowresourcecategoryMetaId (final Integer workflowresourcecategoryMetaId);

  public WorkflowresourcecategoryMeta getWorkflowresourcecategoryMetaByWorkflowresourcecategoryIdK (final Integer workflowresourcecategoryId, final String k);


}

