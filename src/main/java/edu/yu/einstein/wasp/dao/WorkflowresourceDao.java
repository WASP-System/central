
/**
 *
 * WorkflowresourceDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowresource Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.Workflowresource;


public interface WorkflowresourceDao extends WaspDao<Workflowresource> {

  public Workflowresource getWorkflowresourceByWorkflowresourceId (final int workflowresourceId);

  public Workflowresource getWorkflowresourceByWorkflowIdResourceId (final int workflowId, final int resourceId);


}

