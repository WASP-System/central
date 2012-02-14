
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.service.ResourceCategoryService;

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
	@Override
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
	@Override
	public ResourceCategoryDao getResourceCategoryDao() {
		return this.resourceCategoryDao;
	}


  @Override
public ResourceCategory getResourceCategoryByResourceCategoryId (final Integer resourceCategoryId) {
    return this.getResourceCategoryDao().getResourceCategoryByResourceCategoryId(resourceCategoryId);
  }

  @Override
public ResourceCategory getResourceCategoryByIName (final String iName) {
    return this.getResourceCategoryDao().getResourceCategoryByIName(iName);
  }

  @Override
public ResourceCategory getResourceCategoryByName (final String name) {
    return this.getResourceCategoryDao().getResourceCategoryByName(name);
  }
  
  @Override
  public List<ResourceCategory> getActiveResourceCategories(){
	  Map queryMap = new HashMap();
	  queryMap.put("isActive", 1);
	  return this.findByMap(queryMap);
  }

}

