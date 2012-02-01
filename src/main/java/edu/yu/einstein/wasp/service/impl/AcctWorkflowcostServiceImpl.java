
/**
 *
 * AcctWorkflowcostServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctWorkflowcostService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AcctWorkflowcostDao;
import edu.yu.einstein.wasp.model.AcctWorkflowcost;
import edu.yu.einstein.wasp.service.AcctWorkflowcostService;

@Service
public class AcctWorkflowcostServiceImpl extends WaspServiceImpl<AcctWorkflowcost> implements AcctWorkflowcostService {

	/**
	 * acctWorkflowcostDao;
	 *
	 */
	private AcctWorkflowcostDao acctWorkflowcostDao;

	/**
	 * setAcctWorkflowcostDao(AcctWorkflowcostDao acctWorkflowcostDao)
	 *
	 * @param acctWorkflowcostDao
	 *
	 */
	@Override
	@Autowired
	public void setAcctWorkflowcostDao(AcctWorkflowcostDao acctWorkflowcostDao) {
		this.acctWorkflowcostDao = acctWorkflowcostDao;
		this.setWaspDao(acctWorkflowcostDao);
	}

	/**
	 * getAcctWorkflowcostDao();
	 *
	 * @return acctWorkflowcostDao
	 *
	 */
	@Override
	public AcctWorkflowcostDao getAcctWorkflowcostDao() {
		return this.acctWorkflowcostDao;
	}


  @Override
public AcctWorkflowcost getAcctWorkflowcostByWorkflowId (final Integer workflowId) {
    return this.getAcctWorkflowcostDao().getAcctWorkflowcostByWorkflowId(workflowId);
  }

}

