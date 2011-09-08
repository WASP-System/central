
/**
 *
 * WorkflowsubtypesampleDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowsubtypesample Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface WorkflowsubtypesampleDao extends WaspDao<Workflowsubtypesample> {

  public Workflowsubtypesample getWorkflowsubtypesampleByWorkflowsubtypesampleId (final int workflowsubtypesampleId);

  public Workflowsubtypesample getWorkflowsubtypesampleByWorkflowIdSubtypeSampleId (final int workflowId, final int subtypeSampleId);


}

