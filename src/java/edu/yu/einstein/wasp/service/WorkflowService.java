
/**
 *
 * WorkflowService.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.model.Workflow;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface WorkflowService extends WaspService<Workflow> {

  public void setWorkflowDao(WorkflowDao workflowDao);
  public WorkflowDao getWorkflowDao();

  public Workflow getWorkflowByWorkflowId (final int workflowId);

  public Workflow getWorkflowByIName (final String iName);

  public Workflow getWorkflowByName (final String name);

}

