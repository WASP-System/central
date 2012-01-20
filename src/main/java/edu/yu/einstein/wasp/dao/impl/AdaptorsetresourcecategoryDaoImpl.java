
/**
 *
 * AdaptorsetresourcecategoryDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Adaptorsetresourcecategory Dao Impl
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
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Adaptorsetresourcecategory;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AdaptorsetresourcecategoryDaoImpl extends WaspDaoImpl<Adaptorsetresourcecategory> implements edu.yu.einstein.wasp.dao.AdaptorsetresourcecategoryDao {

	/**
	 * AdaptorsetresourcecategoryDaoImpl() Constructor
	 *
	 *
	 */
	public AdaptorsetresourcecategoryDaoImpl() {
		super();
		this.entityClass = Adaptorsetresourcecategory.class;
	}


	/**
	 * getAdaptorsetresourcecategoryByAdaptorsetresourcecategoryId(final Integer adaptorsetresourcecategoryId)
	 *
	 * @param final Integer adaptorsetresourcecategoryId
	 *
	 * @return adaptorsetresourcecategory
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Adaptorsetresourcecategory getAdaptorsetresourcecategoryByAdaptorsetresourcecategoryId (final Integer adaptorsetresourcecategoryId) {
    		HashMap m = new HashMap();
		m.put("adaptorsetresourcecategoryId", adaptorsetresourcecategoryId);

		List<Adaptorsetresourcecategory> results = (List<Adaptorsetresourcecategory>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Adaptorsetresourcecategory rt = new Adaptorsetresourcecategory();
			return rt;
		}
		return (Adaptorsetresourcecategory) results.get(0);
	}



	/**
	 * getAdaptorsetresourcecategoryByAdaptorsetIdResourcecategoryId(final Integer adaptorsetId, final Integer resourcecategoryId)
	 *
	 * @param final Integer adaptorsetId, final Integer resourcecategoryId
	 *
	 * @return adaptorsetresourcecategory
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Adaptorsetresourcecategory getAdaptorsetresourcecategoryByAdaptorsetIdResourcecategoryId (final Integer adaptorsetId, final Integer resourcecategoryId) {
    		HashMap m = new HashMap();
		m.put("adaptorsetId", adaptorsetId);
		m.put("resourcecategoryId", resourcecategoryId);

		List<Adaptorsetresourcecategory> results = (List<Adaptorsetresourcecategory>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Adaptorsetresourcecategory rt = new Adaptorsetresourcecategory();
			return rt;
		}
		return (Adaptorsetresourcecategory) results.get(0);
	}



}

