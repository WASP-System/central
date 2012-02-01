
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

import edu.yu.einstein.wasp.model.Workflowsubtypesample;


public interface WorkflowsubtypesampleDao extends WaspDao<Workflowsubtypesample> {

  public Workflowsubtypesample getWorkflowsubtypesampleByWorkflowsubtypesampleId (final Integer workflowsubtypesampleId);

  public Workflowsubtypesample getWorkflowsubtypesampleByWorkflowIdSubtypeSampleId (final Integer workflowId, final Integer subtypeSampleId);


}

