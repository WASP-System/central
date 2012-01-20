
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

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface WorkflowresourcecategoryMetaDao extends WaspDao<WorkflowresourcecategoryMeta> {

  public WorkflowresourcecategoryMeta getWorkflowresourcecategoryMetaByWorkflowresourcecategoryMetaId (final Integer workflowresourcecategoryMetaId);

  public WorkflowresourcecategoryMeta getWorkflowresourcecategoryMetaByWorkflowresourcecategoryIdK (final Integer workflowresourcecategoryId, final String k);


}

