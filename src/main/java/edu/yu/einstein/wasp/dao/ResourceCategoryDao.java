
/**
 *
 * ResourceCategoryDao.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceCategory Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.ResourceCategory;


public interface ResourceCategoryDao extends WaspDao<ResourceCategory> {

  public ResourceCategory getResourceCategoryByResourceCategoryId (final Integer resourceCategoryId);

  public ResourceCategory getResourceCategoryByIName (final String iName);

  public ResourceCategory getResourceCategoryByName (final String name);


}

