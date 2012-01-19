
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.WorkflowresourceMetaDao;
import edu.yu.einstein.wasp.model.WorkflowresourceMeta;
import edu.yu.einstein.wasp.service.WorkflowresourceMetaService;

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
	@Override
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
	@Override
	public WorkflowresourceMetaDao getWorkflowresourceMetaDao() {
		return this.workflowresourceMetaDao;
	}


  @Override
public WorkflowresourceMeta getWorkflowresourceMetaByWorkflowresourceoptionId (final Integer workflowresourceoptionId) {
    return this.getWorkflowresourceMetaDao().getWorkflowresourceMetaByWorkflowresourceoptionId(workflowresourceoptionId);
  }

  @Override
public WorkflowresourceMeta getWorkflowresourceMetaByWorkflowresourceIdK (final Integer workflowresourceId, final Integer k) {
    return this.getWorkflowresourceMetaDao().getWorkflowresourceMetaByWorkflowresourceIdK(workflowresourceId, k);
  }

}

