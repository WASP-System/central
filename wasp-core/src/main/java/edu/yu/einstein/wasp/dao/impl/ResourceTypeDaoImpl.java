
/**
 *
 * ResourceTypeDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceType Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.ResourceType;


@Transactional("entityManager")
@Repository
public class ResourceTypeDaoImpl extends WaspDaoImpl<ResourceType> implements edu.yu.einstein.wasp.dao.ResourceTypeDao {

	/**
	 * ResourceTypeDaoImpl() Constructor
	 *
	 *
	 */
	public ResourceTypeDaoImpl() {
		super();
		this.entityClass = ResourceType.class;
	}


	/**
	 * getResourceTypeByResourceTypeId(final Integer resourceTypeId)
	 *
	 * @param final Integer resourceTypeId
	 *
	 * @return resourceType
	 */

	@Override
	@Transactional("entityManager")
	public ResourceType getResourceTypeByResourceTypeId (final Integer resourceTypeId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", resourceTypeId);

		List<ResourceType> results = this.findByMap(m);

		if (results.size() == 0) {
			ResourceType rt = new ResourceType();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getResourceTypeByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return resourceType
	 */

	@Override
	@Transactional("entityManager")
	public ResourceType getResourceTypeByIName (final String iName) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("iName", iName);

		List<ResourceType> results = this.findByMap(m);

		if (results.size() == 0) {
			ResourceType rt = new ResourceType();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getResourceTypeByName(final String name)
	 *
	 * @param final String name
	 *
	 * @return resourceType
	 */

	@Override
	@Transactional("entityManager")
	public ResourceType getResourceTypeByName (final String name) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("name", name);

		List<ResourceType> results = this.findByMap(m);

		if (results.size() == 0) {
			ResourceType rt = new ResourceType();
			return rt;
		}
		return results.get(0);
	}



}

