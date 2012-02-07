
/**
 *
 * ResourceMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.ResourceMetaDao;
import edu.yu.einstein.wasp.model.ResourceMeta;
import edu.yu.einstein.wasp.service.ResourceMetaService;

@Service
public class ResourceMetaServiceImpl extends WaspServiceImpl<ResourceMeta> implements ResourceMetaService {

	/**
	 * resourceMetaDao;
	 *
	 */
	private ResourceMetaDao resourceMetaDao;

	/**
	 * setResourceMetaDao(ResourceMetaDao resourceMetaDao)
	 *
	 * @param resourceMetaDao
	 *
	 */
	@Override
	@Autowired
	public void setResourceMetaDao(ResourceMetaDao resourceMetaDao) {
		this.resourceMetaDao = resourceMetaDao;
		this.setWaspDao(resourceMetaDao);
	}

	/**
	 * getResourceMetaDao();
	 *
	 * @return resourceMetaDao
	 *
	 */
	@Override
	public ResourceMetaDao getResourceMetaDao() {
		return this.resourceMetaDao;
	}


  @Override
public ResourceMeta getResourceMetaByResourceMetaId (final Integer resourceMetaId) {
    return this.getResourceMetaDao().getResourceMetaByResourceMetaId(resourceMetaId);
  }

  @Override
public ResourceMeta getResourceMetaByKResourceId (final String k, final Integer resourceId) {
    return this.getResourceMetaDao().getResourceMetaByKResourceId(k, resourceId);
  }

  @Override
public void updateByResourceId (final String area, final int resourceId, final List<ResourceMeta> metaList) {
    this.getResourceMetaDao().updateByResourceId(area, resourceId, metaList); 
  }

  @Override
public void updateByResourceId (final int resourceId, final List<ResourceMeta> metaList) {
    this.getResourceMetaDao().updateByResourceId(resourceId, metaList); 
  }


}
