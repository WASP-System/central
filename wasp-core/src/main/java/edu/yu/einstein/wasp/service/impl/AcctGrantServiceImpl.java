
/**
 *
 * AcctGrantServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrantService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AcctGrantDao;
import edu.yu.einstein.wasp.model.AcctGrant;
import edu.yu.einstein.wasp.service.AcctGrantService;

@Service
public class AcctGrantServiceImpl extends WaspServiceImpl<AcctGrant> implements AcctGrantService {

	/**
	 * acctGrantDao;
	 *
	 */
	private AcctGrantDao acctGrantDao;

	/**
	 * setAcctGrantDao(AcctGrantDao acctGrantDao)
	 *
	 * @param acctGrantDao
	 *
	 */
	@Override
	@Autowired
	public void setAcctGrantDao(AcctGrantDao acctGrantDao) {
		this.acctGrantDao = acctGrantDao;
		this.setWaspDao(acctGrantDao);
	}

	/**
	 * getAcctGrantDao();
	 *
	 * @return acctGrantDao
	 *
	 */
	@Override
	public AcctGrantDao getAcctGrantDao() {
		return this.acctGrantDao;
	}


  @Override
public AcctGrant getAcctGrantByGrantId (final int grantId) {
    return this.getAcctGrantDao().getAcctGrantByGrantId(grantId);
  }

}

