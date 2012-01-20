
/**
 *
 * ResourceCategoryServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceCategoryService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.ResourceCategoryService;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.ResourceCategory;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResourceCategoryServiceImpl extends WaspServiceImpl<ResourceCategory> implements ResourceCategoryService {

	/**
	 * resourceCategoryDao;
	 *
	 */
	private ResourceCategoryDao resourceCategoryDao;

	/**
	 * setResourceCategoryDao(ResourceCategoryDao resourceCategoryDao)
	 *
	 * @param resourceCategoryDao
	 *
	 */
	@Autowired
	public void setResourceCategoryDao(ResourceCategoryDao resourceCategoryDao) {
		this.resourceCategoryDao = resourceCategoryDao;
		this.setWaspDao(resourceCategoryDao);
	}

	/**
	 * getResourceCategoryDao();
	 *
	 * @return resourceCategoryDao
	 *
	 */
	public ResourceCategoryDao getResourceCategoryDao() {
		return this.resourceCategoryDao;
	}


  public ResourceCategory getResourceCategoryByResourceCategoryId (final Integer resourceCategoryId) {
    return this.getResourceCategoryDao().getResourceCategoryByResourceCategoryId(resourceCategoryId);
  }

  public ResourceCategory getResourceCategoryByIName (final String iName) {
    return this.getResourceCategoryDao().getResourceCategoryByIName(iName);
  }

  public ResourceCategory getResourceCategoryByName (final String name) {
    return this.getResourceCategoryDao().getResourceCategoryByName(name);
  }

}

