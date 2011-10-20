
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

import edu.yu.einstein.wasp.service.ResourceMetaService;
import edu.yu.einstein.wasp.dao.ResourceMetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.ResourceMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResourceMetaServiceImpl extends WaspMetaServiceImpl<ResourceMeta> implements ResourceMetaService {

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
	public ResourceMetaDao getResourceMetaDao() {
		return this.resourceMetaDao;
	}


  public ResourceMeta getResourceMetaByResourceMetaId (final int resourceMetaId) {
    return this.getResourceMetaDao().getResourceMetaByResourceMetaId(resourceMetaId);
  }

  public ResourceMeta getResourceMetaByKResourceId (final String k, final int resourceId) {
    return this.getResourceMetaDao().getResourceMetaByKResourceId(k, resourceId);
  }

  public void updateByResourceId (final String area, final int resourceId, final List<ResourceMeta> metaList) {
    this.getResourceMetaDao().updateByResourceId(area, resourceId, metaList); 
  }

  public void updateByResourceId (final int resourceId, final List<ResourceMeta> metaList) {
    this.getResourceMetaDao().updateByResourceId(resourceId, metaList); 
  }


}

