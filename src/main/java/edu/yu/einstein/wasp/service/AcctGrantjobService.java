
/**
 *
 * AcctGrantjobService.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrantjobService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AcctGrantjobDao;
import edu.yu.einstein.wasp.model.AcctGrantjob;

@Service
public interface AcctGrantjobService extends WaspService<AcctGrantjob> {

	/**
	 * setAcctGrantjobDao(AcctGrantjobDao acctGrantjobDao)
	 *
	 * @param acctGrantjobDao
	 *
	 */
	public void setAcctGrantjobDao(AcctGrantjobDao acctGrantjobDao);

	/**
	 * getAcctGrantjobDao();
	 *
	 * @return acctGrantjobDao
	 *
	 */
	public AcctGrantjobDao getAcctGrantjobDao();

  public AcctGrantjob getAcctGrantjobByJobId (final int jobId);


}

