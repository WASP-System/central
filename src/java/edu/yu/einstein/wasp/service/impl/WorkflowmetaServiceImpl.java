
/**
 *
 * WorkflowmetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowmetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.WorkflowmetaService;
import edu.yu.einstein.wasp.dao.WorkflowmetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Workflowmeta;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkflowmetaServiceImpl extends WaspServiceImpl<Workflowmeta> implements WorkflowmetaService {

  private WorkflowmetaDao workflowmetaDao;
  @Autowired
  public void setWorkflowmetaDao(WorkflowmetaDao workflowmetaDao) {
    this.workflowmetaDao = workflowmetaDao;
    this.setWaspDao(workflowmetaDao);
  }
  public WorkflowmetaDao getWorkflowmetaDao() {
    return this.workflowmetaDao;
  }

  // **

  
  public Workflowmeta getWorkflowmetaByWorkflowmetaId (final int workflowmetaId) {
    return this.getWorkflowmetaDao().getWorkflowmetaByWorkflowmetaId(workflowmetaId);
  }

  public Workflowmeta getWorkflowmetaByKWorkflowId (final String k, final int workflowId) {
    return this.getWorkflowmetaDao().getWorkflowmetaByKWorkflowId(k, workflowId);
  }

  public void updateByWorkflowId (final int workflowId, final List<Workflowmeta> metaList) {
    this.getWorkflowmetaDao().updateByWorkflowId(workflowId, metaList); 
  }

}

