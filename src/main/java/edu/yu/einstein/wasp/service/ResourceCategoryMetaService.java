
/**
 *
 * ResourceCategoryMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceCategoryMetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.ResourceCategoryMetaDao;
import edu.yu.einstein.wasp.model.ResourceCategoryMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface ResourceCategoryMetaService extends WaspService<ResourceCategoryMeta> {

	/**
	 * setResourceCategoryMetaDao(ResourceCategoryMetaDao resourceCategoryMetaDao)
	 *
	 * @param resourceCategoryMetaDao
	 *
	 */
	public void setResourceCategoryMetaDao(ResourceCategoryMetaDao resourceCategoryMetaDao);

	/**
	 * getResourceCategoryMetaDao();
	 *
	 * @return resourceCategoryMetaDao
	 *
	 */
	public ResourceCategoryMetaDao getResourceCategoryMetaDao();

  public ResourceCategoryMeta getResourceCategoryMetaByResourceCategoryMetaId (final Integer resourceCategoryMetaId);

  public ResourceCategoryMeta getResourceCategoryMetaByKResourcecategoryId (final String k, final Integer resourcecategoryId);


  public void updateByResourcecategoryId (final String area, final int resourcecategoryId, final List<ResourceCategoryMeta> metaList);

  public void updateByResourcecategoryId (final int resourcecategoryId, final List<ResourceCategoryMeta> metaList);


}

