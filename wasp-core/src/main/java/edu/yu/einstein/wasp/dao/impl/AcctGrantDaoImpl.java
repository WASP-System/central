
/**
 *
 * AcctGrantDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrant Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AcctGrant;

@Transactional("entityManager")
@Repository
public class AcctGrantDaoImpl extends WaspDaoImpl<AcctGrant> implements edu.yu.einstein.wasp.dao.AcctGrantDao {

	/**
	 * AcctGrantDaoImpl() Constructor
	 *
	 *
	 */
	public AcctGrantDaoImpl() {
		super();
		this.entityClass = AcctGrant.class;
	}


	/**
	 * getAcctGrantByGrantId(final int grantId)
	 *
	 * @param final int grantId
	 *
	 * @return acctGrant
	 */

	@Override
	@Transactional("entityManager")
	public AcctGrant getAcctGrantByGrantId (final int grantId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", grantId);

		List<AcctGrant> results = this.findByMap(m);

		if (results.size() == 0) {
			AcctGrant rt = new AcctGrant();
			return rt;
		}
		return results.get(0);
	}



}

