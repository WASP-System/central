
/**
 *
 * WorkflowMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.WorkflowMetaDao;
import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.service.WorkflowMetaService;

@Service
public class WorkflowMetaServiceImpl extends WaspMetaServiceImpl<WorkflowMeta> implements WorkflowMetaService {

	/**
	 * workflowMetaDao;
	 *
	 */
	private WorkflowMetaDao workflowMetaDao;

	/**
	 * setWorkflowMetaDao(WorkflowMetaDao workflowMetaDao)
	 *
	 * @param workflowMetaDao
	 *
	 */
	@Override
	@Autowired
	public void setWorkflowMetaDao(WorkflowMetaDao workflowMetaDao) {
		this.workflowMetaDao = workflowMetaDao;
		this.setWaspDao(workflowMetaDao);
	}

	/**
	 * getWorkflowMetaDao();
	 *
	 * @return workflowMetaDao
	 *
	 */
	@Override
	public WorkflowMetaDao getWorkflowMetaDao() {
		return this.workflowMetaDao;
	}


  @Override
public WorkflowMeta getWorkflowMetaByWorkflowMetaId (final int workflowMetaId) {
    return this.getWorkflowMetaDao().getWorkflowMetaByWorkflowMetaId(workflowMetaId);
  }

  @Override
public WorkflowMeta getWorkflowMetaByKWorkflowId (final String k, final int workflowId) {
    return this.getWorkflowMetaDao().getWorkflowMetaByKWorkflowId(k, workflowId);
  }

  @Override
public void updateByWorkflowId (final String area, final int workflowId, final List<WorkflowMeta> metaList) {
    this.getWorkflowMetaDao().updateByWorkflowId(area, workflowId, metaList); 
  }

  @Override
public void updateByWorkflowId (final int workflowId, final List<WorkflowMeta> metaList) {
    this.getWorkflowMetaDao().updateByWorkflowId(workflowId, metaList); 
  }


}

