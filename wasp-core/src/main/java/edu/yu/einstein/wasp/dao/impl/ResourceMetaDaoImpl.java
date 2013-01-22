
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

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.ResourceMeta;


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
	 * getResourceMetaByResourceMetaId(final Integer resourceMetaId)
	 *
	 * @param final Integer resourceMetaId
	 *
	 * @return resourceMeta
	 */

	@Override
	@Transactional
	public ResourceMeta getResourceMetaByResourceMetaId (final Integer resourceMetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("resourceMetaId", resourceMetaId);

		List<ResourceMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			ResourceMeta rt = new ResourceMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getResourceMetaByKResourceId(final String k, final Integer resourceId)
	 *
	 * @param final String k, final Integer resourceId
	 *
	 * @return resourceMeta
	 */

	@Override
	@Transactional
	public ResourceMeta getResourceMetaByKResourceId (final String k, final Integer resourceId) {
    		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("k", k);
		m.put("resourceId", resourceId);

		List<ResourceMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			ResourceMeta rt = new ResourceMeta();
			return rt;
		}
		return results.get(0);
	}


	/**
	 * updateByResourceId (final int resourceId, final List<ResourceMeta> metaList)
	 *
	 * @param resourceId
	 * @param metaList
	 *
	 */
	@Override
	@Transactional
	public void updateByResourceId (final int resourceId, final List<ResourceMeta> metaList) {
		for (ResourceMeta m:metaList) {
			ResourceMeta currentMeta = getResourceMetaByKResourceId(m.getK(), resourceId);
			if (currentMeta.getResourceMetaId() == null){
				// metadata value not in database yet
				m.setResourceId(resourceId);
				entityManager.persist(m);
			} else if (!currentMeta.getV().equals(m.getV())){
				// meta exists already but value has changed
				currentMeta.setV(m.getV());
				entityManager.merge(currentMeta);
			} else{
				// no change to meta so do nothing
			}
		}
	}



}

