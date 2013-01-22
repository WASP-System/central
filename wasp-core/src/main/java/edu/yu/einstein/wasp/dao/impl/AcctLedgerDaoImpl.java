
/**
 *
 * AcctLedgerDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctLedger Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AcctLedger;


@Transactional
@Repository
public class AcctLedgerDaoImpl extends WaspDaoImpl<AcctLedger> implements edu.yu.einstein.wasp.dao.AcctLedgerDao {

	/**
	 * AcctLedgerDaoImpl() Constructor
	 *
	 *
	 */
	public AcctLedgerDaoImpl() {
		super();
		this.entityClass = AcctLedger.class;
	}


	/**
	 * getAcctLedgerByLedgerId(final int ledgerId)
	 *
	 * @param final int ledgerId
	 *
	 * @return acctLedger
	 */

	@Override
	@Transactional
	public AcctLedger getAcctLedgerByLedgerId (final int ledgerId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("ledgerId", ledgerId);

		List<AcctLedger> results = this.findByMap(m);

		if (results.size() == 0) {
			AcctLedger rt = new AcctLedger();
			return rt;
		}
		return results.get(0);
	}



}

