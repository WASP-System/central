
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

import edu.yu.einstein.wasp.service.AcctGrantService;
import edu.yu.einstein.wasp.dao.AcctGrantDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.AcctGrant;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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
	public AcctGrantDao getAcctGrantDao() {
		return this.acctGrantDao;
	}


  public AcctGrant getAcctGrantByGrantId (final int grantId) {
    return this.getAcctGrantDao().getAcctGrantByGrantId(grantId);
  }

}

