
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AcctJobquotecurrent;

@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	@Transactional
	public AcctJobquotecurrent getAcctJobquotecurrentByJobId (final int jobId) {
    		HashMap m = new HashMap();
		m.put("jobId", jobId);

		List<AcctJobquotecurrent> results = (List<AcctJobquotecurrent>) this.findByMap((Map) m);

		if (results.size() == 0) {
			AcctJobquotecurrent rt = new AcctJobquotecurrent();
			return rt;
		}
		return (AcctJobquotecurrent) results.get(0);
	}



}

