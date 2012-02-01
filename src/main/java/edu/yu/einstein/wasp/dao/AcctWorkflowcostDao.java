
/**
 *
 * AcctWorkflowcostDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctWorkflowcost Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.AcctWorkflowcost;


public interface AcctWorkflowcostDao extends WaspDao<AcctWorkflowcost> {

  public AcctWorkflowcost getAcctWorkflowcostByWorkflowId (final Integer workflowId);


}

