
/**
 *
 * WorkflowresourcecategoryMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowresourcecategoryMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.WorkflowresourcecategoryMeta;


public interface WorkflowresourcecategoryMetaDao extends WaspMetaDao<WorkflowresourcecategoryMeta> {

  public WorkflowresourcecategoryMeta getWorkflowresourcecategoryMetaByWorkflowresourcecategoryMetaId (final Integer workflowresourcecategoryMetaId);

  public WorkflowresourcecategoryMeta getWorkflowresourcecategoryMetaByWorkflowresourcecategoryIdK (final Integer workflowresourcecategoryId, final String k);


}

