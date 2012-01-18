
/**
 *
 * ResourceCategoryService.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceCategoryService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.model.ResourceCategory;

@Service
public interface ResourceCategoryService extends WaspService<ResourceCategory> {

	/**
	 * setResourceCategoryDao(ResourceCategoryDao resourceCategoryDao)
	 *
	 * @param resourceCategoryDao
	 *
	 */
	public void setResourceCategoryDao(ResourceCategoryDao resourceCategoryDao);

	/**
	 * getResourceCategoryDao();
	 *
	 * @return resourceCategoryDao
	 *
	 */
	public ResourceCategoryDao getResourceCategoryDao();

  public ResourceCategory getResourceCategoryByResourceCategoryId (final Integer resourceCategoryId);

  public ResourceCategory getResourceCategoryByIName (final String iName);

  public ResourceCategory getResourceCategoryByName (final String name);


}

