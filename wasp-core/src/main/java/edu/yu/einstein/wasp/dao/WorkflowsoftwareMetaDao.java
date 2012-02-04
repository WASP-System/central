
/**
 *
 * WorkflowsoftwareMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowsoftwareMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.WorkflowsoftwareMeta;


public interface WorkflowsoftwareMetaDao extends WaspDao<WorkflowsoftwareMeta> {

  public WorkflowsoftwareMeta getWorkflowsoftwareMetaByWorkflowsoftwareMetaId (final Integer workflowsoftwareMetaId);

  public WorkflowsoftwareMeta getWorkflowsoftwareMetaByWorkflowsoftwareIdK (final Integer workflowsoftwareId, final String k);


}

