
/**
 *
 * AcctGrantService.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrantService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.AcctGrantDao;
import edu.yu.einstein.wasp.model.AcctGrant;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface AcctGrantService extends WaspService<AcctGrant> {

  public void setAcctGrantDao(AcctGrantDao acctGrantDao);
  public AcctGrantDao getAcctGrantDao();

  public AcctGrant getAcctGrantByGrantId (final int grantId);

}

