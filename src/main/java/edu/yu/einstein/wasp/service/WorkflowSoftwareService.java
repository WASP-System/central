
/**
 *
 * WorkflowSoftwareService.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowSoftwareService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.WorkflowSoftwareDao;
import edu.yu.einstein.wasp.model.WorkflowSoftware;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface WorkflowSoftwareService extends WaspService<WorkflowSoftware> {

	/**
	 * setWorkflowSoftwareDao(WorkflowSoftwareDao workflowSoftwareDao)
	 *
	 * @param workflowSoftwareDao
	 *
	 */
	public void setWorkflowSoftwareDao(WorkflowSoftwareDao workflowSoftwareDao);

	/**
	 * getWorkflowSoftwareDao();
	 *
	 * @return workflowSoftwareDao
	 *
	 */
	public WorkflowSoftwareDao getWorkflowSoftwareDao();

  public WorkflowSoftware getWorkflowSoftwareByWorkflowSoftwareId (final Integer workflowSoftwareId);

  public WorkflowSoftware getWorkflowSoftwareByWorkflowIdSoftwareId (final Integer workflowId, final Integer softwareId);


}

