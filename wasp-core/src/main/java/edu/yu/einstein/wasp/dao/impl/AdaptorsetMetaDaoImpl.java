
/**
 *
 * AdaptorsetMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AdaptorsetMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AdaptorsetMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AdaptorsetMetaDaoImpl extends WaspMetaDaoImpl<AdaptorsetMeta> implements edu.yu.einstein.wasp.dao.AdaptorsetMetaDao {

	/**
	 * AdaptorsetMetaDaoImpl() Constructor
	 *
	 *
	 */
	public AdaptorsetMetaDaoImpl() {
		super();
		this.entityClass = AdaptorsetMeta.class;
	}


	/**
	 * getAdaptorsetMetaByAdaptorsetMetaId(final Integer adaptorsetMetaId)
	 *
	 * @param final Integer adaptorsetMetaId
	 *
	 * @return adaptorsetMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public AdaptorsetMeta getAdaptorsetMetaByAdaptorsetMetaId (final Integer adaptorsetMetaId) {
    		HashMap m = new HashMap();
		m.put("adaptorsetMetaId", adaptorsetMetaId);

		List<AdaptorsetMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			AdaptorsetMeta rt = new AdaptorsetMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getAdaptorsetMetaByKAdaptorsetId(final String k, final Integer adaptorsetId)
	 *
	 * @param final String k, final Integer adaptorsetId
	 *
	 * @return adaptorsetMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public AdaptorsetMeta getAdaptorsetMetaByKAdaptorsetId (final String k, final Integer adaptorsetId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("adaptorsetId", adaptorsetId);

		List<AdaptorsetMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			AdaptorsetMeta rt = new AdaptorsetMeta();
			return rt;
		}
		return results.get(0);
	}

}

