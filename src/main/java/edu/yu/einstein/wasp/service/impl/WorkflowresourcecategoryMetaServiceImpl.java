
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.WorkflowresourcecategoryMetaDao;
import edu.yu.einstein.wasp.model.WorkflowresourcecategoryMeta;
import edu.yu.einstein.wasp.service.WorkflowresourcecategoryMetaService;

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
	@Override
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
	@Override
	public WorkflowresourcecategoryMetaDao getWorkflowresourcecategoryMetaDao() {
		return this.workflowresourcecategoryMetaDao;
	}


  @Override
public WorkflowresourcecategoryMeta getWorkflowresourcecategoryMetaByWorkflowresourcecategoryMetaId (final Integer workflowresourcecategoryMetaId) {
    return this.getWorkflowresourcecategoryMetaDao().getWorkflowresourcecategoryMetaByWorkflowresourcecategoryMetaId(workflowresourcecategoryMetaId);
  }

  @Override
public WorkflowresourcecategoryMeta getWorkflowresourcecategoryMetaByWorkflowresourcecategoryIdK (final Integer workflowresourcecategoryId, final String k) {
    return this.getWorkflowresourcecategoryMetaDao().getWorkflowresourcecategoryMetaByWorkflowresourcecategoryIdK(workflowresourcecategoryId, k);
  }

}

