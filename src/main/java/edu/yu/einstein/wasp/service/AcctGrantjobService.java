
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

import edu.yu.einstein.wasp.dao.AcctGrantjobDao;
import edu.yu.einstein.wasp.model.AcctGrantjob;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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

