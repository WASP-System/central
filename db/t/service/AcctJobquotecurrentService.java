
/**
 *
 * AcctJobquotecurrentService.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctJobquotecurrentService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.AcctJobquotecurrentDao;
import edu.yu.einstein.wasp.model.AcctJobquotecurrent;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface AcctJobquotecurrentService extends WaspService<AcctJobquotecurrent> {

  public void setAcctJobquotecurrentDao(AcctJobquotecurrentDao acctJobquotecurrentDao);
  public AcctJobquotecurrentDao getAcctJobquotecurrentDao();

  public AcctJobquotecurrent getAcctJobquotecurrentByJobId (final int jobId);

}

