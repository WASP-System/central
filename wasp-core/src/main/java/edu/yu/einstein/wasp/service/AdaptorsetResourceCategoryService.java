
/**
 *
 * AdaptorsetresourcecategoryService.java 
 * @author echeng (table2type.pl)
 *  
 * the AdaptorsetresourcecategoryService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AdaptorsetResourceCategoryDao;
import edu.yu.einstein.wasp.model.AdaptorsetResourceCategory;

@Service
public interface AdaptorsetResourceCategoryService extends WaspService<AdaptorsetResourceCategory> {

	/**
	 * setAdaptorsetresourcecategoryDao(AdaptorsetresourcecategoryDao adaptorsetresourcecategoryDao)
	 *
	 * @param adaptorsetResourceCategoryDao
	 *
	 */
	public void setAdaptorsetResourceCategoryDao(AdaptorsetResourceCategoryDao adaptorsetResourceCategoryDao);

	/**
	 * getAdaptorsetresourcecategoryDao();
	 *
	 * @return adaptorsetresourcecategoryDao
	 *
	 */
	public AdaptorsetResourceCategoryDao getAdaptorsetResourceCategoryDao();

  public AdaptorsetResourceCategory getAdaptorsetResourceCategoryByAdaptorsetresourcecategoryId (final Integer adaptorsetresourcecategoryId);

  public AdaptorsetResourceCategory getAdaptorsetResourceCategoryByAdaptorsetIdResourcecategoryId (final Integer adaptorsetId, final Integer resourcecategoryId);


}

