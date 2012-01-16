
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
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AcctGrantjob;

@SuppressWarnings("unchecked")
@Transactional
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
	@SuppressWarnings("unchecked")
	@Transactional
	public AcctGrantjob getAcctGrantjobByJobId (final int jobId) {
    		HashMap m = new HashMap();
		m.put("jobId", jobId);

		List<AcctGrantjob> results = this.findByMap(m);

		if (results.size() == 0) {
			AcctGrantjob rt = new AcctGrantjob();
			return rt;
		}
		return results.get(0);
	}



}

