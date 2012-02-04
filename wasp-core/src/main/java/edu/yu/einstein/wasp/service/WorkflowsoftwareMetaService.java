
/**
 *
 * WorkflowsoftwareMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowsoftwareMetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.WorkflowsoftwareMetaDao;
import edu.yu.einstein.wasp.model.WorkflowsoftwareMeta;

@Service
public interface WorkflowsoftwareMetaService extends WaspService<WorkflowsoftwareMeta> {

	/**
	 * setWorkflowsoftwareMetaDao(WorkflowsoftwareMetaDao workflowsoftwareMetaDao)
	 *
	 * @param workflowsoftwareMetaDao
	 *
	 */
	public void setWorkflowsoftwareMetaDao(WorkflowsoftwareMetaDao workflowsoftwareMetaDao);

	/**
	 * getWorkflowsoftwareMetaDao();
	 *
	 * @return workflowsoftwareMetaDao
	 *
	 */
	public WorkflowsoftwareMetaDao getWorkflowsoftwareMetaDao();

  public WorkflowsoftwareMeta getWorkflowsoftwareMetaByWorkflowsoftwareMetaId (final Integer workflowsoftwareMetaId);

  public WorkflowsoftwareMeta getWorkflowsoftwareMetaByWorkflowsoftwareIdK (final Integer workflowsoftwareId, final String k);


}

