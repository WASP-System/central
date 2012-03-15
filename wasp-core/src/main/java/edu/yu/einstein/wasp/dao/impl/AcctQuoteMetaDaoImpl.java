
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
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AcctQuoteMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AcctQuoteMetaDaoImpl extends WaspDaoImpl<AcctQuoteMeta> implements edu.yu.einstein.wasp.dao.AcctQuoteMetaDao {

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
	@SuppressWarnings("unchecked")
	@Transactional
	public AcctQuoteMeta getAcctQuoteMetaByQuotemetaId (final Integer quotemetaId) {
    		HashMap m = new HashMap();
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
	@SuppressWarnings("unchecked")
	@Transactional
	public AcctQuoteMeta getAcctQuoteMetaByKQuoteId (final String k, final Integer quoteId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("quoteId", quoteId);

		List<AcctQuoteMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			AcctQuoteMeta rt = new AcctQuoteMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * updateByQuoteId (final string area, final int quoteId, final List<AcctQuoteMeta> metaList)
	 *
	 * @param quoteId
	 * @param metaList
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByQuoteId (final String area, final int quoteId, final List<AcctQuoteMeta> metaList) {
		entityManager.createNativeQuery("delete from acct_quotemeta where quoteId=:quoteId and k like :area").setParameter("quoteId", quoteId).setParameter("area", area + ".%").executeUpdate();

		for (AcctQuoteMeta m:metaList) {
			m.setQuoteId(quoteId);
			entityManager.persist(m);
		}
	}


	/**
	 * updateByQuoteId (final int quoteId, final List<AcctQuoteMeta> metaList)
	 *
	 * @param quoteId
	 * @param metaList
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByQuoteId (final int quoteId, final List<AcctQuoteMeta> metaList) {
		entityManager.createNativeQuery("delete from acct_quotemeta where quoteId=:quoteId").setParameter("quoteId", quoteId).executeUpdate();

		for (AcctQuoteMeta m:metaList) {
			m.setQuoteId(quoteId);
			entityManager.persist(m);
		}
	}



}

