
/**
 *
 * WorkflowresourcetypeDao.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowResourceType Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.WorkflowResourceType;


public interface WorkflowResourceTypeDao extends WaspDao<WorkflowResourceType> {

  public WorkflowResourceType getWorkflowResourceTypeByWorkflowresourcetypeId (final Integer workflowresourcetypeId);

  public WorkflowResourceType getWorkflowResourceTypeByWorkflowIdResourceTypeId (final Integer workflowId, final Integer resourceTypeId);


}

