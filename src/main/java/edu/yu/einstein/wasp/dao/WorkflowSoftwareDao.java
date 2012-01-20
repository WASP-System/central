
/**
 *
 * WorkflowSoftwareDao.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowSoftware Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface WorkflowSoftwareDao extends WaspDao<WorkflowSoftware> {

  public WorkflowSoftware getWorkflowSoftwareByWorkflowSoftwareId (final Integer workflowSoftwareId);

  public WorkflowSoftware getWorkflowSoftwareByWorkflowIdSoftwareId (final Integer workflowId, final Integer softwareId);


}

