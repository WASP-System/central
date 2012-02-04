
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.WorkflowsoftwareMetaDao;
import edu.yu.einstein.wasp.model.WorkflowsoftwareMeta;
import edu.yu.einstein.wasp.service.WorkflowsoftwareMetaService;

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
	@Override
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
	@Override
	public WorkflowsoftwareMetaDao getWorkflowsoftwareMetaDao() {
		return this.workflowsoftwareMetaDao;
	}


  @Override
public WorkflowsoftwareMeta getWorkflowsoftwareMetaByWorkflowsoftwareMetaId (final Integer workflowsoftwareMetaId) {
    return this.getWorkflowsoftwareMetaDao().getWorkflowsoftwareMetaByWorkflowsoftwareMetaId(workflowsoftwareMetaId);
  }

  @Override
public WorkflowsoftwareMeta getWorkflowsoftwareMetaByWorkflowsoftwareIdK (final Integer workflowsoftwareId, final String k) {
    return this.getWorkflowsoftwareMetaDao().getWorkflowsoftwareMetaByWorkflowsoftwareIdK(workflowsoftwareId, k);
  }

}

