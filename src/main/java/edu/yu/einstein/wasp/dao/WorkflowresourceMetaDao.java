
/**
 *
 * WorkflowresourceMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowresourceMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface WorkflowresourceMetaDao extends WaspDao<WorkflowresourceMeta> {

  public WorkflowresourceMeta getWorkflowresourceMetaByWorkflowresourceoptionId (final Integer workflowresourceoptionId);

  public WorkflowresourceMeta getWorkflowresourceMetaByWorkflowresourceIdK (final Integer workflowresourceId, final Integer k);


}

