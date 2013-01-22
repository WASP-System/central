
/**
 *
 * AdaptorMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AdaptorMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AdaptorMeta;


@Transactional
@Repository
public class AdaptorMetaDaoImpl extends WaspDaoImpl<AdaptorMeta> implements edu.yu.einstein.wasp.dao.AdaptorMetaDao {

	/**
	 * AdaptorMetaDaoImpl() Constructor
	 *
	 *
	 */
	public AdaptorMetaDaoImpl() {
		super();
		this.entityClass = AdaptorMeta.class;
	}


	/**
	 * getAdaptorMetaByAdaptorMetaId(final Integer adaptorMetaId)
	 *
	 * @param final Integer adaptorMetaId
	 *
	 * @return adaptorMeta
	 */

	@Override
	@Transactional
	public AdaptorMeta getAdaptorMetaByAdaptorMetaId (final Integer adaptorMetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("adaptorMetaId", adaptorMetaId);

		List<AdaptorMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			AdaptorMeta rt = new AdaptorMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getAdaptorMetaByKAdaptorId(final String k, final Integer adaptorId)
	 *
	 * @param final String k, final Integer adaptorId
	 *
	 * @return adaptorMeta
	 */

	@Override
	@Transactional
	public AdaptorMeta getAdaptorMetaByKAdaptorId (final String k, final Integer adaptorId) {
    		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("k", k);
		m.put("adaptorId", adaptorId);

		List<AdaptorMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			AdaptorMeta rt = new AdaptorMeta();
			return rt;
		}
		return results.get(0);
	}


	/**
	 * updateByAdaptorId (final int adaptorId, final List<AdaptorMeta> metaList)
	 *
	 * @param adaptorId
	 * @param metaList
	 *
	 */
	@Override
	@Transactional
	public void updateByAdaptorId (final int adaptorId, final List<AdaptorMeta> metaList) {
		for (AdaptorMeta m:metaList) {
			AdaptorMeta currentMeta = getAdaptorMetaByKAdaptorId(m.getK(), adaptorId);
			if (currentMeta.getAdaptorMetaId() == null){
				// metadata value not in database yet
				m.setAdaptorId(adaptorId);
				entityManager.persist(m);
			} else if (!currentMeta.getV().equals(m.getV())){
				// meta exists already but value has changed
				currentMeta.setV(m.getV());
				entityManager.merge(currentMeta);
			} else{
				// no change to meta so do nothing
			}
		}
	}



}

