
/**
 *
 * WorkflowresourceDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowresource Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface WorkflowresourceDao extends WaspDao<Workflowresource> {

  public Workflowresource getWorkflowresourceByWorkflowresourceId (final Integer workflowresourceId);

  public Workflowresource getWorkflowresourceByWorkflowIdResourceId (final Integer workflowId, final Integer resourceId);


}

