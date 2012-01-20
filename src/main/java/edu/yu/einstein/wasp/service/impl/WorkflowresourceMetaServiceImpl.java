
/**
 *
 * WorkflowresourceMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowresourceMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.WorkflowresourceMetaService;
import edu.yu.einstein.wasp.dao.WorkflowresourceMetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.WorkflowresourceMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkflowresourceMetaServiceImpl extends WaspServiceImpl<WorkflowresourceMeta> implements WorkflowresourceMetaService {

	/**
	 * workflowresourceMetaDao;
	 *
	 */
	private WorkflowresourceMetaDao workflowresourceMetaDao;

	/**
	 * setWorkflowresourceMetaDao(WorkflowresourceMetaDao workflowresourceMetaDao)
	 *
	 * @param workflowresourceMetaDao
	 *
	 */
	@Autowired
	public void setWorkflowresourceMetaDao(WorkflowresourceMetaDao workflowresourceMetaDao) {
		this.workflowresourceMetaDao = workflowresourceMetaDao;
		this.setWaspDao(workflowresourceMetaDao);
	}

	/**
	 * getWorkflowresourceMetaDao();
	 *
	 * @return workflowresourceMetaDao
	 *
	 */
	public WorkflowresourceMetaDao getWorkflowresourceMetaDao() {
		return this.workflowresourceMetaDao;
	}


  public WorkflowresourceMeta getWorkflowresourceMetaByWorkflowresourceMetaId (final Integer workflowresourceMetaId) {
    return this.getWorkflowresourceMetaDao().getWorkflowresourceMetaByWorkflowresourceMetaId(workflowresourceMetaId);
  }

  public WorkflowresourceMeta getWorkflowresourceMetaByWorkflowresourceIdK (final Integer workflowresourceId, final String k) {
    return this.getWorkflowresourceMetaDao().getWorkflowresourceMetaByWorkflowresourceIdK(workflowresourceId, k);
  }

}

