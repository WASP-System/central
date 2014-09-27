
/**
 *
 * AcctGrantjobDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrantjob Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AcctGrantjob;


@Transactional("entityManager")
@Repository
public class AcctGrantjobDaoImpl extends WaspDaoImpl<AcctGrantjob> implements edu.yu.einstein.wasp.dao.AcctGrantjobDao {

	/**
	 * AcctGrantjobDaoImpl() Constructor
	 *
	 *
	 */
	public AcctGrantjobDaoImpl() {
		super();
		this.entityClass = AcctGrantjob.class;
	}


	/**
	 * getAcctGrantjobByJobId(final int jobId)
	 *
	 * @param final int jobId
	 *
	 * @return acctGrantjob
	 */

	@Override
	@Transactional("entityManager")
	public AcctGrantjob getAcctGrantjobById (final int id) {
    	HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", id);

		List<AcctGrantjob> results = this.findByMap(m);

		if (results.size() == 0) {
			AcctGrantjob rt = new AcctGrantjob();
			return rt;
		}
		return results.get(0);
	}

	@Override
	@Transactional("entityManager")
	public List<AcctGrantjob> getAcctGrantjobByJobId (final int jobid) {
    	HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("jobId", jobid);
		return this.findByMap(m);

	}
	
	@Override
	@Transactional("entityManager")
	public List<AcctGrantjob> getAcctGrantjobByAcctGrantId (final int acctGrantId) {
    	HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("grantId", acctGrantId);
		return this.findByMap(m);

	}


	@Override
	public AcctGrantjob getAcctGrantjobByJobIdAcctGrantId(int jobid, int acctGrantId) {
		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("grantId", acctGrantId);
		m.put("jobId", jobid);
		List<AcctGrantjob> results = this.findByMap(m);
		if (results.size() == 0) {
			AcctGrantjob rt = new AcctGrantjob();
			return rt;
		}
		return results.get(0);
	}


}

