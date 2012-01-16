
/**
 *
 * AcctGrantService.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrantService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AcctGrantDao;
import edu.yu.einstein.wasp.model.AcctGrant;

@Service
public interface AcctGrantService extends WaspService<AcctGrant> {

	/**
	 * setAcctGrantDao(AcctGrantDao acctGrantDao)
	 *
	 * @param acctGrantDao
	 *
	 */
	public void setAcctGrantDao(AcctGrantDao acctGrantDao);

	/**
	 * getAcctGrantDao();
	 *
	 * @return acctGrantDao
	 *
	 */
	public AcctGrantDao getAcctGrantDao();

  public AcctGrant getAcctGrantByGrantId (final int grantId);


}

