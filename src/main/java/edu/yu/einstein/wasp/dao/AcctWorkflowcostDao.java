
/**
 *
 * AcctWorkflowcostDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctWorkflowcostDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface AcctWorkflowcostDao extends WaspDao<AcctWorkflowcost> {

  public AcctWorkflowcost getAcctWorkflowcostByWorkflowId (final int workflowId);

}

