
/**
 *
 * AcctWorkflowcostService.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctWorkflowcostService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.AcctWorkflowcostDao;
import edu.yu.einstein.wasp.model.AcctWorkflowcost;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface AcctWorkflowcostService extends WaspService<AcctWorkflowcost> {

	/**
	 * setAcctWorkflowcostDao(AcctWorkflowcostDao acctWorkflowcostDao)
	 *
	 * @param acctWorkflowcostDao
	 *
	 */
	public void setAcctWorkflowcostDao(AcctWorkflowcostDao acctWorkflowcostDao);

	/**
	 * getAcctWorkflowcostDao();
	 *
	 * @return acctWorkflowcostDao
	 *
	 */
	public AcctWorkflowcostDao getAcctWorkflowcostDao();

  public AcctWorkflowcost getAcctWorkflowcostByWorkflowId (final int workflowId);


}

