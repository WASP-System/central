
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AcctLedger;

@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	@Transactional
	public AcctLedger getAcctLedgerByLedgerId (final int ledgerId) {
    		HashMap m = new HashMap();
		m.put("ledgerId", ledgerId);

		List<AcctLedger> results = (List<AcctLedger>) this.findByMap((Map) m);

		if (results.size() == 0) {
			AcctLedger rt = new AcctLedger();
			return rt;
		}
		return (AcctLedger) results.get(0);
	}



}

