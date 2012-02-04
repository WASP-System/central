
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

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AdaptorsetResourceCategory;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AdaptorsetResourceCategoryDaoImpl extends WaspDaoImpl<AdaptorsetResourceCategory> implements edu.yu.einstein.wasp.dao.AdaptorsetResourceCategoryDao {

	/**
	 * AdaptorsetresourcecategoryDaoImpl() Constructor
	 *
	 *
	 */
	public AdaptorsetResourceCategoryDaoImpl() {
		super();
		this.entityClass = AdaptorsetResourceCategory.class;
	}


	/**
	 * getAdaptorsetresourcecategoryByAdaptorsetresourcecategoryId(final Integer adaptorsetresourcecategoryId)
	 *
	 * @param final Integer adaptorsetresourcecategoryId
	 *
	 * @return adaptorsetresourcecategory
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public AdaptorsetResourceCategory getAdaptorsetResourceCategoryByAdaptorsetresourcecategoryId (final Integer adaptorsetresourcecategoryId) {
    		HashMap m = new HashMap();
		m.put("adaptorsetresourcecategoryId", adaptorsetresourcecategoryId);

		List<AdaptorsetResourceCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			AdaptorsetResourceCategory rt = new AdaptorsetResourceCategory();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getAdaptorsetresourcecategoryByAdaptorsetIdResourcecategoryId(final Integer adaptorsetId, final Integer resourcecategoryId)
	 *
	 * @param final Integer adaptorsetId, final Integer resourcecategoryId
	 *
	 * @return adaptorsetresourcecategory
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public AdaptorsetResourceCategory getAdaptorsetResourceCategoryByAdaptorsetIdResourcecategoryId (final Integer adaptorsetId, final Integer resourcecategoryId) {
    		HashMap m = new HashMap();
		m.put("adaptorsetId", adaptorsetId);
		m.put("resourcecategoryId", resourcecategoryId);

		List<AdaptorsetResourceCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			AdaptorsetResourceCategory rt = new AdaptorsetResourceCategory();
			return rt;
		}
		return results.get(0);
	}



}

