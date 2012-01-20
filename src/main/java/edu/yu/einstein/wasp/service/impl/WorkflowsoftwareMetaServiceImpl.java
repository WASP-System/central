
/**
 *
 * WorkflowsoftwareMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowsoftwareMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.WorkflowsoftwareMetaService;
import edu.yu.einstein.wasp.dao.WorkflowsoftwareMetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.WorkflowsoftwareMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkflowsoftwareMetaServiceImpl extends WaspServiceImpl<WorkflowsoftwareMeta> implements WorkflowsoftwareMetaService {

	/**
	 * workflowsoftwareMetaDao;
	 *
	 */
	private WorkflowsoftwareMetaDao workflowsoftwareMetaDao;

	/**
	 * setWorkflowsoftwareMetaDao(WorkflowsoftwareMetaDao workflowsoftwareMetaDao)
	 *
	 * @param workflowsoftwareMetaDao
	 *
	 */
	@Autowired
	public void setWorkflowsoftwareMetaDao(WorkflowsoftwareMetaDao workflowsoftwareMetaDao) {
		this.workflowsoftwareMetaDao = workflowsoftwareMetaDao;
		this.setWaspDao(workflowsoftwareMetaDao);
	}

	/**
	 * getWorkflowsoftwareMetaDao();
	 *
	 * @return workflowsoftwareMetaDao
	 *
	 */
	public WorkflowsoftwareMetaDao getWorkflowsoftwareMetaDao() {
		return this.workflowsoftwareMetaDao;
	}


  public WorkflowsoftwareMeta getWorkflowsoftwareMetaByWorkflowsoftwareMetaId (final Integer workflowsoftwareMetaId) {
    return this.getWorkflowsoftwareMetaDao().getWorkflowsoftwareMetaByWorkflowsoftwareMetaId(workflowsoftwareMetaId);
  }

  public WorkflowsoftwareMeta getWorkflowsoftwareMetaByWorkflowsoftwareIdK (final Integer workflowsoftwareId, final String k) {
    return this.getWorkflowsoftwareMetaDao().getWorkflowsoftwareMetaByWorkflowsoftwareIdK(workflowsoftwareId, k);
  }

}

