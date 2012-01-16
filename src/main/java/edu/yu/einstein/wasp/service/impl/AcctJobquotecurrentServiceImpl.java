
/**
 *
 * AcctJobquotecurrentServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctJobquotecurrentService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AcctJobquotecurrentDao;
import edu.yu.einstein.wasp.model.AcctJobquotecurrent;
import edu.yu.einstein.wasp.service.AcctJobquotecurrentService;

@Service
public class AcctJobquotecurrentServiceImpl extends WaspServiceImpl<AcctJobquotecurrent> implements AcctJobquotecurrentService {

	/**
	 * acctJobquotecurrentDao;
	 *
	 */
	private AcctJobquotecurrentDao acctJobquotecurrentDao;

	/**
	 * setAcctJobquotecurrentDao(AcctJobquotecurrentDao acctJobquotecurrentDao)
	 *
	 * @param acctJobquotecurrentDao
	 *
	 */
	@Autowired
	public void setAcctJobquotecurrentDao(AcctJobquotecurrentDao acctJobquotecurrentDao) {
		this.acctJobquotecurrentDao = acctJobquotecurrentDao;
		this.setWaspDao(acctJobquotecurrentDao);
	}

	/**
	 * getAcctJobquotecurrentDao();
	 *
	 * @return acctJobquotecurrentDao
	 *
	 */
	public AcctJobquotecurrentDao getAcctJobquotecurrentDao() {
		return this.acctJobquotecurrentDao;
	}


  public AcctJobquotecurrent getAcctJobquotecurrentByJobId (final int jobId) {
    return this.getAcctJobquotecurrentDao().getAcctJobquotecurrentByJobId(jobId);
  }

}

