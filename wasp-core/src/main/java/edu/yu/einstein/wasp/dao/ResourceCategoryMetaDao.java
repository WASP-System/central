
/**
 *
 * ResourceCategoryMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceCategoryMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.ResourceCategoryMeta;


public interface ResourceCategoryMetaDao extends WaspMetaDao<ResourceCategoryMeta> {

  public ResourceCategoryMeta getResourceCategoryMetaByResourceCategoryMetaId (final Integer resourceCategoryMetaId);

  public ResourceCategoryMeta getResourceCategoryMetaByKResourcecategoryId (final String k, final Integer resourcecategoryId);





}

