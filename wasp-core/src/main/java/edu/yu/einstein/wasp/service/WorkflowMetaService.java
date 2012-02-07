
/**
 *
 * WorkflowMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowMetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.WorkflowMetaDao;
import edu.yu.einstein.wasp.model.WorkflowMeta;

@Service
public interface WorkflowMetaService extends WaspService<WorkflowMeta> {

	/**
	 * setWorkflowMetaDao(WorkflowMetaDao workflowMetaDao)
	 *
	 * @param workflowMetaDao
	 *
	 */
	public void setWorkflowMetaDao(WorkflowMetaDao workflowMetaDao);

	/**
	 * getWorkflowMetaDao();
	 *
	 * @return workflowMetaDao
	 *
	 */
	public WorkflowMetaDao getWorkflowMetaDao();

  public WorkflowMeta getWorkflowMetaByWorkflowMetaId (final Integer workflowMetaId);

  public WorkflowMeta getWorkflowMetaByKWorkflowId (final String k, final Integer workflowId);


  public void updateByWorkflowId (final String area, final int workflowId, final List<WorkflowMeta> metaList);

  public void updateByWorkflowId (final int workflowId, final List<WorkflowMeta> metaList);


}
