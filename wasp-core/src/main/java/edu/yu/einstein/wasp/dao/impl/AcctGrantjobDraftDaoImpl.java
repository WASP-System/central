
/**
 *
 * AcctGrantjobDraftDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrantjobDraft Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AcctGrantjobDraft;


@Transactional("entityManager")
@Repository
public class AcctGrantjobDraftDaoImpl extends WaspDaoImpl<AcctGrantjobDraft> implements edu.yu.einstein.wasp.dao.AcctGrantjobDraftDao {

	/**
	 * AcctGrantjobDaoImpl() Constructor
	 *
	 *
	 */
	public AcctGrantjobDraftDaoImpl() {
		super();
		this.entityClass = AcctGrantjobDraft.class;
	}


	/**
	 * getAcctGrantjobById(final int id)
	 *
	 * @param final int id
	 *
	 * @return acctGrantjob
	 */

	@Override
	@Transactional("entityManager")
	public AcctGrantjobDraft getAcctGrantjobDraftById (final int id) {
    	HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", id);

		List<AcctGrantjobDraft> results = this.findByMap(m);

		if (results.size() == 0) {
			AcctGrantjobDraft rt = new AcctGrantjobDraft();
			return rt;
		}
		return results.get(0);
	}
	
	@Override
	@Transactional("entityManager")
	public List<AcctGrantjobDraft> getAcctGrantjobDraftByJobDraftId (final int jobDraftid) {
    	HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("jobDraftId", jobDraftid);
		return this.findByMap(m);

	}
	
	@Override
	@Transactional("entityManager")
	public List<AcctGrantjobDraft> getAcctGrantjobDraftByAcctGrantId (final int acctGrantId) {
    	HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("grantId", acctGrantId);
		return this.findByMap(m);

	}


	@Override
	public AcctGrantjobDraft getAcctGrantjobDraftByJobDraftIdAcctGrantId(int jobDraftid, int acctGrantId) {
		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("grantId", acctGrantId);
		m.put("jobDraftId", jobDraftid);
		List<AcctGrantjobDraft> results = this.findByMap(m);

		if (results.size() == 0) {
			AcctGrantjobDraft rt = new AcctGrantjobDraft();
			return rt;
		}
		return results.get(0);
	}



}

