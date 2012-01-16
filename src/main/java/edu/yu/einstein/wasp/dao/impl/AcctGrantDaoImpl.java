
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
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AcctGrant;

@SuppressWarnings("unchecked")
@Transactional
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

	@SuppressWarnings("unchecked")
	@Transactional
	public AcctGrant getAcctGrantByGrantId (final int grantId) {
    		HashMap m = new HashMap();
		m.put("grantId", grantId);

		List<AcctGrant> results = (List<AcctGrant>) this.findByMap((Map) m);

		if (results.size() == 0) {
			AcctGrant rt = new AcctGrant();
			return rt;
		}
		return (AcctGrant) results.get(0);
	}



}

