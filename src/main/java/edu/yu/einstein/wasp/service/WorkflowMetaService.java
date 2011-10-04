
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

import edu.yu.einstein.wasp.dao.WorkflowMetaDao;
import edu.yu.einstein.wasp.model.WorkflowMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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

  public WorkflowMeta getWorkflowMetaByWorkflowMetaId (final int workflowMetaId);

  public WorkflowMeta getWorkflowMetaByKWorkflowId (final String k, final int workflowId);


  public void updateByWorkflowId (final String area, final int workflowId, final List<WorkflowMeta> metaList);

  public void updateByWorkflowId (final int workflowId, final List<WorkflowMeta> metaList);


}

