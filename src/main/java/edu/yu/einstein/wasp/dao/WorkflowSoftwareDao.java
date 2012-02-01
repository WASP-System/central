
/**
 *
 * WorkflowSoftwareDao.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowSoftware Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.WorkflowSoftware;


public interface WorkflowSoftwareDao extends WaspDao<WorkflowSoftware> {

  public WorkflowSoftware getWorkflowSoftwareByWorkflowSoftwareId (final Integer workflowSoftwareId);

  public WorkflowSoftware getWorkflowSoftwareByWorkflowIdSoftwareId (final Integer workflowId, final Integer softwareId);


}

