
/**
 *
 * WorkflowresourceMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowresourceMetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.WorkflowresourceMetaDao;
import edu.yu.einstein.wasp.model.WorkflowresourceMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface WorkflowresourceMetaService extends WaspService<WorkflowresourceMeta> {

	/**
	 * setWorkflowresourceMetaDao(WorkflowresourceMetaDao workflowresourceMetaDao)
	 *
	 * @param workflowresourceMetaDao
	 *
	 */
	public void setWorkflowresourceMetaDao(WorkflowresourceMetaDao workflowresourceMetaDao);

	/**
	 * getWorkflowresourceMetaDao();
	 *
	 * @return workflowresourceMetaDao
	 *
	 */
	public WorkflowresourceMetaDao getWorkflowresourceMetaDao();

  public WorkflowresourceMeta getWorkflowresourceMetaByWorkflowresourceoptionId (final Integer workflowresourceoptionId);

  public WorkflowresourceMeta getWorkflowresourceMetaByWorkflowresourceIdK (final Integer workflowresourceId, final Integer k);


}

