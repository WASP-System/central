
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

import edu.yu.einstein.wasp.service.ResourceCategoryMetaService;
import edu.yu.einstein.wasp.dao.ResourceCategoryMetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.ResourceCategoryMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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
	public ResourceCategoryMetaDao getResourceCategoryMetaDao() {
		return this.resourceCategoryMetaDao;
	}


  public ResourceCategoryMeta getResourceCategoryMetaByResourceCategoryMetaId (final Integer resourceCategoryMetaId) {
    return this.getResourceCategoryMetaDao().getResourceCategoryMetaByResourceCategoryMetaId(resourceCategoryMetaId);
  }

  public ResourceCategoryMeta getResourceCategoryMetaByKResourcecategoryId (final String k, final Integer resourcecategoryId) {
    return this.getResourceCategoryMetaDao().getResourceCategoryMetaByKResourcecategoryId(k, resourcecategoryId);
  }

  public void updateByResourcecategoryId (final String area, final int resourcecategoryId, final List<ResourceCategoryMeta> metaList) {
    this.getResourceCategoryMetaDao().updateByResourcecategoryId(area, resourcecategoryId, metaList); 
  }

  public void updateByResourcecategoryId (final int resourcecategoryId, final List<ResourceCategoryMeta> metaList) {
    this.getResourceCategoryMetaDao().updateByResourcecategoryId(resourcecategoryId, metaList); 
  }


}

