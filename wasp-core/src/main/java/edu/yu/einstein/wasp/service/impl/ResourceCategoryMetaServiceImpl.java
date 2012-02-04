
/**
 *
 * ResourceCategoryMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceCategoryMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.ResourceCategoryMetaDao;
import edu.yu.einstein.wasp.model.ResourceCategoryMeta;
import edu.yu.einstein.wasp.service.ResourceCategoryMetaService;

@Service
public class ResourceCategoryMetaServiceImpl extends WaspServiceImpl<ResourceCategoryMeta> implements ResourceCategoryMetaService {

	/**
	 * resourceCategoryMetaDao;
	 *
	 */
	private ResourceCategoryMetaDao resourceCategoryMetaDao;

	/**
	 * setResourceCategoryMetaDao(ResourceCategoryMetaDao resourceCategoryMetaDao)
	 *
	 * @param resourceCategoryMetaDao
	 *
	 */
	@Override
	@Autowired
	public void setResourceCategoryMetaDao(ResourceCategoryMetaDao resourceCategoryMetaDao) {
		this.resourceCategoryMetaDao = resourceCategoryMetaDao;
		this.setWaspDao(resourceCategoryMetaDao);
	}

	/**
	 * getResourceCategoryMetaDao();
	 *
	 * @return resourceCategoryMetaDao
	 *
	 */
	@Override
	public ResourceCategoryMetaDao getResourceCategoryMetaDao() {
		return this.resourceCategoryMetaDao;
	}


  @Override
public ResourceCategoryMeta getResourceCategoryMetaByResourceCategoryMetaId (final Integer resourceCategoryMetaId) {
    return this.getResourceCategoryMetaDao().getResourceCategoryMetaByResourceCategoryMetaId(resourceCategoryMetaId);
  }

  @Override
public ResourceCategoryMeta getResourceCategoryMetaByKResourcecategoryId (final String k, final Integer resourcecategoryId) {
    return this.getResourceCategoryMetaDao().getResourceCategoryMetaByKResourcecategoryId(k, resourcecategoryId);
  }

  @Override
public void updateByResourcecategoryId (final String area, final int resourcecategoryId, final List<ResourceCategoryMeta> metaList) {
    this.getResourceCategoryMetaDao().updateByResourcecategoryId(area, resourcecategoryId, metaList); 
  }

  @Override
public void updateByResourcecategoryId (final int resourcecategoryId, final List<ResourceCategoryMeta> metaList) {
    this.getResourceCategoryMetaDao().updateByResourcecategoryId(resourcecategoryId, metaList); 
  }


}

