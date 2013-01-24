
/**
 *
 * AcctQuoteMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AcctQuoteMeta;


@Transactional
@Repository
public class AcctQuoteMetaDaoImpl extends WaspMetaDaoImpl<AcctQuoteMeta> implements edu.yu.einstein.wasp.dao.AcctQuoteMetaDao {

	/**
	 * AcctQuoteMetaDaoImpl() Constructor
	 *
	 *
	 */
	public AcctQuoteMetaDaoImpl() {
		super();
		this.entityClass = AcctQuoteMeta.class;
	}


	/**
	 * getAcctQuoteMetaByQuotemetaId(final Integer quotemetaId)
	 *
	 * @param final Integer quotemetaId
	 *
	 * @return acctQuoteMeta
	 */

	@Override
	@Transactional
	public AcctQuoteMeta getAcctQuoteMetaByQuotemetaId (final Integer quotemetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("quotemetaId", quotemetaId);

		List<AcctQuoteMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			AcctQuoteMeta rt = new AcctQuoteMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getAcctQuoteMetaByKQuoteId(final String k, final Integer quoteId)
	 *
	 * @param final String k, final Integer quoteId
	 *
	 * @return acctQuoteMeta
	 */

	@Override
	@Transactional
	public AcctQuoteMeta getAcctQuoteMetaByKQuoteId (final String k, final Integer quoteId) {
    		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("k", k);
		m.put("quoteId", quoteId);

		List<AcctQuoteMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			AcctQuoteMeta rt = new AcctQuoteMeta();
			return rt;
		}
		return results.get(0);
	}




}

