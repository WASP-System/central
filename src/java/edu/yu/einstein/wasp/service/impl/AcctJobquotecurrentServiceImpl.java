
/**
 *
 * AcctJobquotecurrentService.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctJobquotecurrentService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.AcctJobquotecurrentService;
import edu.yu.einstein.wasp.dao.AcctJobquotecurrentDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.AcctJobquotecurrent;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AcctJobquotecurrentServiceImpl extends WaspServiceImpl<AcctJobquotecurrent> implements AcctJobquotecurrentService {

  private AcctJobquotecurrentDao acctJobquotecurrentDao;
  @Autowired
  public void setAcctJobquotecurrentDao(AcctJobquotecurrentDao acctJobquotecurrentDao) {
    this.acctJobquotecurrentDao = acctJobquotecurrentDao;
    this.setWaspDao(acctJobquotecurrentDao);
  }
  public AcctJobquotecurrentDao getAcctJobquotecurrentDao() {
    return this.acctJobquotecurrentDao;
  }

  // **

  
  public AcctJobquotecurrent getAcctJobquotecurrentByJobId (final int jobId) {
    return this.getAcctJobquotecurrentDao().getAcctJobquotecurrentByJobId(jobId);
  }
}

