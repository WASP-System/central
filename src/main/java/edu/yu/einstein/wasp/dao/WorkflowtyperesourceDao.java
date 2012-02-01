
/**
 *
 * WorkflowtyperesourceDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowtyperesource Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.Workflowtyperesource;


public interface WorkflowtyperesourceDao extends WaspDao<Workflowtyperesource> {

  public Workflowtyperesource getWorkflowtyperesourceByWorkflowtyperesourceId (final Integer workflowtyperesourceId);

  public Workflowtyperesource getWorkflowtyperesourceByWorkflowIdTypeResourceId (final Integer workflowId, final Integer typeResourceId);


}

