
/**
 *
 * AcctGrantjobServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrantjobService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AcctGrantjobDao;
import edu.yu.einstein.wasp.model.AcctGrantjob;
import edu.yu.einstein.wasp.service.AcctGrantjobService;

@Service
public class AcctGrantjobServiceImpl extends WaspServiceImpl<AcctGrantjob> implements AcctGrantjobService {

	/**
	 * acctGrantjobDao;
	 *
	 */
	private AcctGrantjobDao acctGrantjobDao;

	/**
	 * setAcctGrantjobDao(AcctGrantjobDao acctGrantjobDao)
	 *
	 * @param acctGrantjobDao
	 *
	 */
	@Autowired
	public void setAcctGrantjobDao(AcctGrantjobDao acctGrantjobDao) {
		this.acctGrantjobDao = acctGrantjobDao;
		this.setWaspDao(acctGrantjobDao);
	}

	/**
	 * getAcctGrantjobDao();
	 *
	 * @return acctGrantjobDao
	 *
	 */
	public AcctGrantjobDao getAcctGrantjobDao() {
		return this.acctGrantjobDao;
	}


  public AcctGrantjob getAcctGrantjobByJobId (final int jobId) {
    return this.getAcctGrantjobDao().getAcctGrantjobByJobId(jobId);
  }

}

