
/**
 *
 * AcctJobquotecurrentDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctJobquotecurrent Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AcctJobquotecurrent;


@Transactional
@Repository
public class AcctJobquotecurrentDaoImpl extends WaspDaoImpl<AcctJobquotecurrent> implements edu.yu.einstein.wasp.dao.AcctJobquotecurrentDao {

	/**
	 * AcctJobquotecurrentDaoImpl() Constructor
	 *
	 *
	 */
	public AcctJobquotecurrentDaoImpl() {
		super();
		this.entityClass = AcctJobquotecurrent.class;
	}


	/**
	 * getAcctJobquotecurrentByJobId(final int jobId)
	 *
	 * @param final int jobId
	 *
	 * @return acctJobquotecurrent
	 */

	@Override
	@Transactional
	public AcctJobquotecurrent getAcctJobquotecurrentByJobId (final int jobId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", jobId);

		List<AcctJobquotecurrent> results = this.findByMap(m);

		if (results.size() == 0) {
			AcctJobquotecurrent rt = new AcctJobquotecurrent();
			return rt;
		}
		return results.get(0);
	}



}

