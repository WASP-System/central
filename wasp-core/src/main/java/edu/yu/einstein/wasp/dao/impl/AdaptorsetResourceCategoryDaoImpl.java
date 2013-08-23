
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


@Transactional("entityManager")
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
	@Transactional("entityManager")
	public AdaptorsetResourceCategory getAdaptorsetResourceCategoryByAdaptorsetresourcecategoryId (final Integer adaptorsetresourcecategoryId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", adaptorsetresourcecategoryId);

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
	@Transactional("entityManager")
	public AdaptorsetResourceCategory getAdaptorsetResourceCategoryByAdaptorsetIdResourcecategoryId (final Integer adaptorsetId, final Integer resourcecategoryId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("adaptorsetId", adaptorsetId);
		m.put("resourceCategoryId", resourcecategoryId);

		List<AdaptorsetResourceCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			AdaptorsetResourceCategory rt = new AdaptorsetResourceCategory();
			return rt;
		}
		return results.get(0);
	}



}

