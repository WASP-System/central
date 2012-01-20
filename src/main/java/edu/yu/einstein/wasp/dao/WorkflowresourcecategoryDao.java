
/**
 *
 * WorkflowresourcecategoryDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowresourcecategory Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface WorkflowresourcecategoryDao extends WaspDao<Workflowresourcecategory> {

  public Workflowresourcecategory getWorkflowresourcecategoryByWorkflowresourcecategoryId (final Integer workflowresourcecategoryId);

  public Workflowresourcecategory getWorkflowresourcecategoryByWorkflowIdResourcecategoryId (final Integer workflowId, final Integer resourcecategoryId);


}

