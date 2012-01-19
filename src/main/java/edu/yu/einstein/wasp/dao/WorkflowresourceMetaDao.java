
/**
 *
 * WorkflowresourceMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowresourceMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.WorkflowresourceMeta;


public interface WorkflowresourceMetaDao extends WaspDao<WorkflowresourceMeta> {

  public WorkflowresourceMeta getWorkflowresourceMetaByWorkflowresourceoptionId (final Integer workflowresourceoptionId);

  public WorkflowresourceMeta getWorkflowresourceMetaByWorkflowresourceIdK (final Integer workflowresourceId, final Integer k);


}

