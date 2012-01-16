
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
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Resource;

@SuppressWarnings("unchecked")
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
	 * getResourceByResourceId(final int resourceId)
	 *
	 * @param final int resourceId
	 *
	 * @return resource
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Resource getResourceByResourceId (final int resourceId) {
    		HashMap m = new HashMap();
		m.put("resourceId", resourceId);

		List<Resource> results = (List<Resource>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Resource rt = new Resource();
			return rt;
		}
		return (Resource) results.get(0);
	}



	/**
	 * getResourceByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return resource
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Resource getResourceByIName (final String iName) {
    		HashMap m = new HashMap();
		m.put("iName", iName);

		List<Resource> results = (List<Resource>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Resource rt = new Resource();
			return rt;
		}
		return (Resource) results.get(0);
	}



}

