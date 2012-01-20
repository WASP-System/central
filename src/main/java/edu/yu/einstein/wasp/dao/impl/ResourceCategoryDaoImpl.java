
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.ResourceCategory;

@SuppressWarnings("unchecked")
@Transactional
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

	@SuppressWarnings("unchecked")
	@Transactional
	public ResourceCategory getResourceCategoryByResourceCategoryId (final Integer resourceCategoryId) {
    		HashMap m = new HashMap();
		m.put("resourceCategoryId", resourceCategoryId);

		List<ResourceCategory> results = (List<ResourceCategory>) this.findByMap((Map) m);

		if (results.size() == 0) {
			ResourceCategory rt = new ResourceCategory();
			return rt;
		}
		return (ResourceCategory) results.get(0);
	}



	/**
	 * getResourceCategoryByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return resourceCategory
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public ResourceCategory getResourceCategoryByIName (final String iName) {
    		HashMap m = new HashMap();
		m.put("iName", iName);

		List<ResourceCategory> results = (List<ResourceCategory>) this.findByMap((Map) m);

		if (results.size() == 0) {
			ResourceCategory rt = new ResourceCategory();
			return rt;
		}
		return (ResourceCategory) results.get(0);
	}



	/**
	 * getResourceCategoryByName(final String name)
	 *
	 * @param final String name
	 *
	 * @return resourceCategory
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public ResourceCategory getResourceCategoryByName (final String name) {
    		HashMap m = new HashMap();
		m.put("name", name);

		List<ResourceCategory> results = (List<ResourceCategory>) this.findByMap((Map) m);

		if (results.size() == 0) {
			ResourceCategory rt = new ResourceCategory();
			return rt;
		}
		return (ResourceCategory) results.get(0);
	}



}

