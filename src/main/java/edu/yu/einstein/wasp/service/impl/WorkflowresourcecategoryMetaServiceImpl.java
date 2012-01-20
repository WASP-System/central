
/**
 *
 * WorkflowresourcecategoryMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowresourcecategoryMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.WorkflowresourcecategoryMetaService;
import edu.yu.einstein.wasp.dao.WorkflowresourcecategoryMetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.WorkflowresourcecategoryMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkflowresourcecategoryMetaServiceImpl extends WaspServiceImpl<WorkflowresourcecategoryMeta> implements WorkflowresourcecategoryMetaService {

	/**
	 * workflowresourcecategoryMetaDao;
	 *
	 */
	private WorkflowresourcecategoryMetaDao workflowresourcecategoryMetaDao;

	/**
	 * setWorkflowresourcecategoryMetaDao(WorkflowresourcecategoryMetaDao workflowresourcecategoryMetaDao)
	 *
	 * @param workflowresourcecategoryMetaDao
	 *
	 */
	@Autowired
	public void setWorkflowresourcecategoryMetaDao(WorkflowresourcecategoryMetaDao workflowresourcecategoryMetaDao) {
		this.workflowresourcecategoryMetaDao = workflowresourcecategoryMetaDao;
		this.setWaspDao(workflowresourcecategoryMetaDao);
	}

	/**
	 * getWorkflowresourcecategoryMetaDao();
	 *
	 * @return workflowresourcecategoryMetaDao
	 *
	 */
	public WorkflowresourcecategoryMetaDao getWorkflowresourcecategoryMetaDao() {
		return this.workflowresourcecategoryMetaDao;
	}


  public WorkflowresourcecategoryMeta getWorkflowresourcecategoryMetaByWorkflowresourcecategoryMetaId (final Integer workflowresourcecategoryMetaId) {
    return this.getWorkflowresourcecategoryMetaDao().getWorkflowresourcecategoryMetaByWorkflowresourcecategoryMetaId(workflowresourcecategoryMetaId);
  }

  public WorkflowresourcecategoryMeta getWorkflowresourcecategoryMetaByWorkflowresourcecategoryIdK (final Integer workflowresourcecategoryId, final String k) {
    return this.getWorkflowresourcecategoryMetaDao().getWorkflowresourcecategoryMetaByWorkflowresourcecategoryIdK(workflowresourcecategoryId, k);
  }

}

