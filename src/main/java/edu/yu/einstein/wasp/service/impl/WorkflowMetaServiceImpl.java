
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

import edu.yu.einstein.wasp.service.WorkflowMetaService;
import edu.yu.einstein.wasp.dao.WorkflowMetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.WorkflowMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkflowMetaServiceImpl extends WaspServiceImpl<WorkflowMeta> implements WorkflowMetaService {

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
	public WorkflowMetaDao getWorkflowMetaDao() {
		return this.workflowMetaDao;
	}


  public WorkflowMeta getWorkflowMetaByWorkflowMetaId (final int workflowMetaId) {
    return this.getWorkflowMetaDao().getWorkflowMetaByWorkflowMetaId(workflowMetaId);
  }

  public WorkflowMeta getWorkflowMetaByKWorkflowId (final String k, final int workflowId) {
    return this.getWorkflowMetaDao().getWorkflowMetaByKWorkflowId(k, workflowId);
  }

  public void updateByWorkflowId (final String area, final int workflowId, final List<WorkflowMeta> metaList) {
    this.getWorkflowMetaDao().updateByWorkflowId(area, workflowId, metaList); 
  }

  public void updateByWorkflowId (final int workflowId, final List<WorkflowMeta> metaList) {
    this.getWorkflowMetaDao().updateByWorkflowId(workflowId, metaList); 
  }


}

