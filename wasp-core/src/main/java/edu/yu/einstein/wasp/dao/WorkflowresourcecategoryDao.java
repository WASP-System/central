
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

import edu.yu.einstein.wasp.model.Workflowresourcecategory;


public interface WorkflowresourcecategoryDao extends WaspDao<Workflowresourcecategory> {

  public Workflowresourcecategory getWorkflowresourcecategoryByWorkflowresourcecategoryId (final Integer workflowresourcecategoryId);

  public Workflowresourcecategory getWorkflowresourcecategoryByWorkflowIdResourcecategoryId (final Integer workflowId, final Integer resourcecategoryId);


}

