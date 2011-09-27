
/**
 *
 * AcctJobquotecurrentService.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctJobquotecurrentService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.AcctJobquotecurrentDao;
import edu.yu.einstein.wasp.model.AcctJobquotecurrent;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface AcctJobquotecurrentService extends WaspService<AcctJobquotecurrent> {

	/**
	 * setAcctJobquotecurrentDao(AcctJobquotecurrentDao acctJobquotecurrentDao)
	 *
	 * @param acctJobquotecurrentDao
	 *
	 */
	public void setAcctJobquotecurrentDao(AcctJobquotecurrentDao acctJobquotecurrentDao);

	/**
	 * getAcctJobquotecurrentDao();
	 *
	 * @return acctJobquotecurrentDao
	 *
	 */
	public AcctJobquotecurrentDao getAcctJobquotecurrentDao();

  public AcctJobquotecurrent getAcctJobquotecurrentByJobId (final int jobId);


}

