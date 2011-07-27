
/**
 *
 * WorkflowDao.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface WorkflowDao extends WaspDao<Workflow> {

  public Workflow getWorkflowByWorkflowId (final int workflowId);

  public Workflow getWorkflowByIName (final String iName);

  public Workflow getWorkflowByName (final String name);

}

