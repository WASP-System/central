
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

import edu.yu.einstein.wasp.service.AcctWorkflowcostService;
import edu.yu.einstein.wasp.dao.AcctWorkflowcostDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.AcctWorkflowcost;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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
	public AcctWorkflowcostDao getAcctWorkflowcostDao() {
		return this.acctWorkflowcostDao;
	}


  public AcctWorkflowcost getAcctWorkflowcostByWorkflowId (final int workflowId) {
    return this.getAcctWorkflowcostDao().getAcctWorkflowcostByWorkflowId(workflowId);
  }

}

