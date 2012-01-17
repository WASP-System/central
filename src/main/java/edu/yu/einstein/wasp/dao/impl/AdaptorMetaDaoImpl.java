
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

@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
	@Transactional
	public AdaptorMeta getAdaptorMetaByAdaptorMetaId (final Integer adaptorMetaId) {
    		HashMap m = new HashMap();
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
	@SuppressWarnings("unchecked")
	@Transactional
	public AdaptorMeta getAdaptorMetaByKAdaptorId (final String k, final Integer adaptorId) {
    		HashMap m = new HashMap();
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
	 * updateByAdaptorId (final string area, final int adaptorId, final List<AdaptorMeta> metaList)
	 *
	 * @param adaptorId
	 * @param metaList
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByAdaptorId (final String area, final int adaptorId, final List<AdaptorMeta> metaList) {
		entityManager.createNativeQuery("delete from adaptormeta where adaptorId=:adaptorId and k like :area").setParameter("adaptorId", adaptorId).setParameter("area", area + ".%").executeUpdate();

		for (AdaptorMeta m:metaList) {
			m.setAdaptorId(adaptorId);
			entityManager.persist(m);
		}
	}


	/**
	 * updateByAdaptorId (final int adaptorId, final List<AdaptorMeta> metaList)
	 *
	 * @param adaptorId
	 * @param metaList
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByAdaptorId (final int adaptorId, final List<AdaptorMeta> metaList) {
		entityManager.createNativeQuery("delete from adaptormeta where adaptorId=:adaptorId").setParameter("adaptorId", adaptorId).executeUpdate();

		for (AdaptorMeta m:metaList) {
			m.setAdaptorId(adaptorId);
			entityManager.persist(m);
		}
	}



}

