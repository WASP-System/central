
/**
 *
 * AdaptorsetresourceDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Adaptorsetresource Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Adaptorsetresource;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AdaptorsetresourceDaoImpl extends WaspDaoImpl<Adaptorsetresource> implements edu.yu.einstein.wasp.dao.AdaptorsetresourceDao {

	/**
	 * AdaptorsetresourceDaoImpl() Constructor
	 *
	 *
	 */
	public AdaptorsetresourceDaoImpl() {
		super();
		this.entityClass = Adaptorsetresource.class;
	}


	/**
	 * getAdaptorsetresourceByAdaptorsetresourceId(final Integer adaptorsetresourceId)
	 *
	 * @param final Integer adaptorsetresourceId
	 *
	 * @return adaptorsetresource
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Adaptorsetresource getAdaptorsetresourceByAdaptorsetresourceId (final Integer adaptorsetresourceId) {
    		HashMap m = new HashMap();
		m.put("adaptorsetresourceId", adaptorsetresourceId);

		List<Adaptorsetresource> results = this.findByMap(m);

		if (results.size() == 0) {
			Adaptorsetresource rt = new Adaptorsetresource();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getAdaptorsetresourceByAdaptorsetIdResourceId(final Integer adaptorsetId, final Integer resourceId)
	 *
	 * @param final Integer adaptorsetId, final Integer resourceId
	 *
	 * @return adaptorsetresource
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Adaptorsetresource getAdaptorsetresourceByAdaptorsetIdResourceId (final Integer adaptorsetId, final Integer resourceId) {
    		HashMap m = new HashMap();
		m.put("adaptorsetId", adaptorsetId);
		m.put("resourceId", resourceId);

		List<Adaptorsetresource> results = this.findByMap(m);

		if (results.size() == 0) {
			Adaptorsetresource rt = new Adaptorsetresource();
			return rt;
		}
		return results.get(0);
	}

}

