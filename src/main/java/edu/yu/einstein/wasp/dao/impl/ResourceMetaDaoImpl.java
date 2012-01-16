
/**
 *
 * ResourceMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.ResourceMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class ResourceMetaDaoImpl extends WaspDaoImpl<ResourceMeta> implements edu.yu.einstein.wasp.dao.ResourceMetaDao {

	/**
	 * ResourceMetaDaoImpl() Constructor
	 *
	 *
	 */
	public ResourceMetaDaoImpl() {
		super();
		this.entityClass = ResourceMeta.class;
	}


	/**
	 * getResourceMetaByResourceMetaId(final int resourceMetaId)
	 *
	 * @param final int resourceMetaId
	 *
	 * @return resourceMeta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public ResourceMeta getResourceMetaByResourceMetaId (final int resourceMetaId) {
    		HashMap m = new HashMap();
		m.put("resourceMetaId", resourceMetaId);

		List<ResourceMeta> results = (List<ResourceMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			ResourceMeta rt = new ResourceMeta();
			return rt;
		}
		return (ResourceMeta) results.get(0);
	}



	/**
	 * getResourceMetaByKResourceId(final String k, final int resourceId)
	 *
	 * @param final String k, final int resourceId
	 *
	 * @return resourceMeta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public ResourceMeta getResourceMetaByKResourceId (final String k, final int resourceId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("resourceId", resourceId);

		List<ResourceMeta> results = (List<ResourceMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			ResourceMeta rt = new ResourceMeta();
			return rt;
		}
		return (ResourceMeta) results.get(0);
	}



	/**
	 * updateByResourceId (final string area, final int resourceId, final List<ResourceMeta> metaList)
	 *
	 * @param resourceId
	 * @param metaList
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByResourceId (final String area, final int resourceId, final List<ResourceMeta> metaList) {
		entityManager.createNativeQuery("delete from resourcemeta where resourceId=:resourceId and k like :area").setParameter("resourceId", resourceId).setParameter("area", area + ".%").executeUpdate();

		for (ResourceMeta m:metaList) {
			m.setResourceId(resourceId);
			entityManager.persist(m);
		}
 	}


	/**
	 * updateByResourceId (final int resourceId, final List<ResourceMeta> metaList)
	 *
	 * @param resourceId
	 * @param metaList
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByResourceId (final int resourceId, final List<ResourceMeta> metaList) {
		entityManager.createNativeQuery("delete from resourcemeta where resourceId=:resourceId").setParameter("resourceId", resourceId).executeUpdate();

		for (ResourceMeta m:metaList) {
			m.setResourceId(resourceId);
			entityManager.persist(m);
		}
 	}



}

