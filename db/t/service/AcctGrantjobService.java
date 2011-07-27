
/**
 *
 * AcctGrantjobService.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrantjobService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.AcctGrantjobDao;
import edu.yu.einstein.wasp.model.AcctGrantjob;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface AcctGrantjobService extends WaspService<AcctGrantjob> {

  public void setAcctGrantjobDao(AcctGrantjobDao acctGrantjobDao);
  public AcctGrantjobDao getAcctGrantjobDao();

  public AcctGrantjob getAcctGrantjobByJobId (final int jobId);

}

