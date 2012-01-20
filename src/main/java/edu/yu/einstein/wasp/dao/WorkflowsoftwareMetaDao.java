
/**
 *
 * WorkflowsoftwareMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowsoftwareMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface WorkflowsoftwareMetaDao extends WaspDao<WorkflowsoftwareMeta> {

  public WorkflowsoftwareMeta getWorkflowsoftwareMetaByWorkflowsoftwareMetaId (final Integer workflowsoftwareMetaId);

  public WorkflowsoftwareMeta getWorkflowsoftwareMetaByWorkflowsoftwareIdK (final Integer workflowsoftwareId, final String k);


}

