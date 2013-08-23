
/**
 *
 * AcctQuoteUserDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteUser Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AcctQuoteUser;


@Transactional("entityManager")
@Repository
public class AcctQuoteUserDaoImpl extends WaspDaoImpl<AcctQuoteUser> implements edu.yu.einstein.wasp.dao.AcctQuoteUserDao {

	/**
	 * AcctQuoteUserDaoImpl() Constructor
	 *
	 *
	 */
	public AcctQuoteUserDaoImpl() {
		super();
		this.entityClass = AcctQuoteUser.class;
	}


	/**
	 * getAcctQuoteUserByQuoteUserId(final int quoteUserId)
	 *
	 * @param final int quoteUserId
	 *
	 * @return acctQuoteUser
	 */

	@Override
	@Transactional("entityManager")
	public AcctQuoteUser getAcctQuoteUserByQuoteUserId (final int quoteUserId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", quoteUserId);

		List<AcctQuoteUser> results = this.findByMap(m);

		if (results.size() == 0) {
			AcctQuoteUser rt = new AcctQuoteUser();
			return rt;
		}
		return results.get(0);
	}



}

