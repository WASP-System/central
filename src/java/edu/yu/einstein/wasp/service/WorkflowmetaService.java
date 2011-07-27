
/**
 *
 * WorkflowmetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowmetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.WorkflowmetaDao;
import edu.yu.einstein.wasp.model.Workflowmeta;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface WorkflowmetaService extends WaspService<Workflowmeta> {

  public void setWorkflowmetaDao(WorkflowmetaDao workflowmetaDao);
  public WorkflowmetaDao getWorkflowmetaDao();

  public Workflowmeta getWorkflowmetaByWorkflowmetaId (final int workflowmetaId);

  public Workflowmeta getWorkflowmetaByKWorkflowId (final String k, final int workflowId);

  public void updateByWorkflowId (final int workflowId, final List<Workflowmeta> metaList);

}

