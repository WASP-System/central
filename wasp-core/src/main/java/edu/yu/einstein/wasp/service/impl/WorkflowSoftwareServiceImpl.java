
/**
 *
 * WorkflowSoftwareServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowSoftwareService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.WorkflowSoftwareDao;
import edu.yu.einstein.wasp.model.WorkflowSoftware;
import edu.yu.einstein.wasp.service.WorkflowSoftwareService;

@Service
public class WorkflowSoftwareServiceImpl extends WaspServiceImpl<WorkflowSoftware> implements WorkflowSoftwareService {

	/**
	 * workflowSoftwareDao;
	 *
	 */
	private WorkflowSoftwareDao workflowSoftwareDao;

	/**
	 * setWorkflowSoftwareDao(WorkflowSoftwareDao workflowSoftwareDao)
	 *
	 * @param workflowSoftwareDao
	 *
	 */
	@Override
	@Autowired
	public void setWorkflowSoftwareDao(WorkflowSoftwareDao workflowSoftwareDao) {
		this.workflowSoftwareDao = workflowSoftwareDao;
		this.setWaspDao(workflowSoftwareDao);
	}

	/**
	 * getWorkflowSoftwareDao();
	 *
	 * @return workflowSoftwareDao
	 *
	 */
	@Override
	public WorkflowSoftwareDao getWorkflowSoftwareDao() {
		return this.workflowSoftwareDao;
	}


  @Override
public WorkflowSoftware getWorkflowSoftwareByWorkflowSoftwareId (final Integer workflowSoftwareId) {
    return this.getWorkflowSoftwareDao().getWorkflowSoftwareByWorkflowSoftwareId(workflowSoftwareId);
  }

  @Override
public WorkflowSoftware getWorkflowSoftwareByWorkflowIdSoftwareId (final Integer workflowId, final Integer softwareId) {
    return this.getWorkflowSoftwareDao().getWorkflowSoftwareByWorkflowIdSoftwareId(workflowId, softwareId);
  }

}

