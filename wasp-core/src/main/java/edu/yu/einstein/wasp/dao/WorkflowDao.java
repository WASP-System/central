
/**
 *
 * WorkflowDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflow Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.Workflow;


public interface WorkflowDao extends WaspDao<Workflow> {

  public Workflow getWorkflowByWorkflowId (final Integer workflowId);

  public Workflow getWorkflowByIName (final String iName);

  public Workflow getWorkflowByName (final String name);

  public List<Workflow> getActiveWorkflows();
}

