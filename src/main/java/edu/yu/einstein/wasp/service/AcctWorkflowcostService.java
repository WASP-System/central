
/**
 *
 * AcctWorkflowcostService.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctWorkflowcostService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.AcctWorkflowcostDao;
import edu.yu.einstein.wasp.model.AcctWorkflowcost;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface AcctWorkflowcostService extends WaspService<AcctWorkflowcost> {

  public void setAcctWorkflowcostDao(AcctWorkflowcostDao acctWorkflowcostDao);
  public AcctWorkflowcostDao getAcctWorkflowcostDao();

  public AcctWorkflowcost getAcctWorkflowcostByWorkflowId (final int workflowId);

}

