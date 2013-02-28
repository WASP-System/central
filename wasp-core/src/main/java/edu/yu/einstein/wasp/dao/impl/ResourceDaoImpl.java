
/**
 *
 * ResourceDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Resource Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Resource;


@Transactional
@Repository
public class ResourceDaoImpl extends WaspDaoImpl<Resource> implements edu.yu.einstein.wasp.dao.ResourceDao {

	/**
	 * ResourceDaoImpl() Constructor
	 *
	 *
	 */
	public ResourceDaoImpl() {
		super();
		this.entityClass = Resource.class;
	}


	/**
	 * getResourceByResourceId(final Integer resourceId)
	 *
	 * @param final Integer resourceId
	 *
	 * @return resource
	 */

	@Override
	@Transactional
	public Resource getResourceByResourceId (final Integer resourceId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", resourceId);

		List<Resource> results = this.findByMap(m);

		if (results.size() == 0) {
			Resource rt = new Resource();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getResourceByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return resource
	 */

	@Override
	@Transactional
	public Resource getResourceByIName (final String iName) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("iName", iName);

		List<Resource> results = this.findByMap(m);

		if (results.size() == 0) {
			Resource rt = new Resource();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getResourceByName(final String name)
	 *
	 * @param final String name
	 *
	 * @return resource
	 */

	@Override
	@Transactional
	public Resource getResourceByName (final String name) {
    	HashMap<String, String> m = new HashMap<String, String>();
		m.put("name", name);

		List<Resource> results = this.findByMap(m);

		if (results.size() == 0) {
			Resource rt = new Resource();
			return rt;
		}
		return results.get(0);
	}



}

