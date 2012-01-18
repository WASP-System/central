
/**
 *
 * AdaptorsetresourcecategoryDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Adaptorsetresourcecategory Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.AdaptorsetResourceCategory;


public interface AdaptorsetResourceCategoryDao extends WaspDao<AdaptorsetResourceCategory> {

  public AdaptorsetResourceCategory getAdaptorsetResourceCategoryByAdaptorsetresourcecategoryId (final Integer adaptorsetresourcecategoryId);

  public AdaptorsetResourceCategory getAdaptorsetResourceCategoryByAdaptorsetIdResourcecategoryId (final Integer adaptorsetId, final Integer resourcecategoryId);


}

