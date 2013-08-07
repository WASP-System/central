
/**
 *
 * ResourceCategoryDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceCategory Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.ResourceCategory;


@Transactional("entityManager")
@Repository
public class ResourceCategoryDaoImpl extends WaspDaoImpl<ResourceCategory> implements edu.yu.einstein.wasp.dao.ResourceCategoryDao {

	/**
	 * ResourceCategoryDaoImpl() Constructor
	 *
	 *
	 */
	public ResourceCategoryDaoImpl() {
		super();
		this.entityClass = ResourceCategory.class;
	}


	/**
	 * getResourceCategoryByResourceCategoryId(final Integer resourceCategoryId)
	 *
	 * @param final Integer resourceCategoryId
	 *
	 * @return resourceCategory
	 */

	@Override
	@Transactional("entityManager")
	public ResourceCategory getResourceCategoryByResourceCategoryId (final Integer resourceCategoryId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", resourceCategoryId);

		List<ResourceCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			ResourceCategory rt = new ResourceCategory();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getResourceCategoryByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return resourceCategory
	 */

	@Override
	@Transactional("entityManager")
	public ResourceCategory getResourceCategoryByIName (final String iName) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("iName", iName);

		List<ResourceCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			ResourceCategory rt = new ResourceCategory();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getResourceCategoryByName(final String name)
	 *
	 * @param final String name
	 *
	 * @return resourceCategory
	 */

	@Override
	@Transactional("entityManager")
	public ResourceCategory getResourceCategoryByName (final String name) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("name", name);

		List<ResourceCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			ResourceCategory rt = new ResourceCategory();
			return rt;
		}
		return results.get(0);
	}
	
	@Override
	public List<ResourceCategory> getActiveResourceCategories() {
		Map<String, Integer> queryMap = new HashMap<String, Integer>();
		queryMap.put("isActive", 1);
		return this.findByMap(queryMap);
	}



}

