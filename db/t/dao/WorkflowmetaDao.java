
/**
 *
 * WorkflowmetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowmetaDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface WorkflowmetaDao extends WaspDao<Workflowmeta> {

  public Workflowmeta getWorkflowmetaByWorkflowmetaId (final int workflowmetaId);

  public Workflowmeta getWorkflowmetaByKWorkflowId (final String k, final int workflowId);

  public void updateByWorkflowId (final int workflowId, final List<Workflowmeta> metaList);

}

