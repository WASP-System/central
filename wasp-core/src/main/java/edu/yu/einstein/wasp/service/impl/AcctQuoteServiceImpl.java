/**
 *
 * AcctQuoteServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AcctQuoteDao;
import edu.yu.einstein.wasp.model.AcctQuote;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.service.AcctQuoteService;

@Service
public class AcctQuoteServiceImpl extends WaspServiceImpl<AcctQuote> implements AcctQuoteService {

	/**
	 * acctQuoteDao;
	 * 
	 */
	private AcctQuoteDao	acctQuoteDao;

	/**
	 * setAcctQuoteDao(AcctQuoteDao acctQuoteDao)
	 * 
	 * @param acctQuoteDao
	 * 
	 */
	@Override
	@Autowired
	public void setAcctQuoteDao(AcctQuoteDao acctQuoteDao) {
		this.acctQuoteDao = acctQuoteDao;
		this.setWaspDao(acctQuoteDao);
	}

	/**
	 * getAcctQuoteDao();
	 * 
	 * @return acctQuoteDao
	 * 
	 */
	@Override
	public AcctQuoteDao getAcctQuoteDao() {
		return this.acctQuoteDao;
	}

	@Override
	public AcctQuote getAcctQuoteByQuoteId(final int quoteId) {
		return this.getAcctQuoteDao().getAcctQuoteByQuoteId(quoteId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AcctQuote> getJobQuotesByJobId(Integer jobId) {
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("jobId", jobId.toString());
		return this.findByMap(queryMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AcctQuote> getJobQuotesByUserId(Integer userId) {
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("UserId", userId.toString());
		return this.findByMap(queryMap);
	}
}
