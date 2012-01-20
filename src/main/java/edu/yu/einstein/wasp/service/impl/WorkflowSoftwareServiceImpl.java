
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

import edu.yu.einstein.wasp.service.WorkflowSoftwareService;
import edu.yu.einstein.wasp.dao.WorkflowSoftwareDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.WorkflowSoftware;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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
	public WorkflowSoftwareDao getWorkflowSoftwareDao() {
		return this.workflowSoftwareDao;
	}


  public WorkflowSoftware getWorkflowSoftwareByWorkflowSoftwareId (final Integer workflowSoftwareId) {
    return this.getWorkflowSoftwareDao().getWorkflowSoftwareByWorkflowSoftwareId(workflowSoftwareId);
  }

  public WorkflowSoftware getWorkflowSoftwareByWorkflowIdSoftwareId (final Integer workflowId, final Integer softwareId) {
    return this.getWorkflowSoftwareDao().getWorkflowSoftwareByWorkflowIdSoftwareId(workflowId, softwareId);
  }

}

