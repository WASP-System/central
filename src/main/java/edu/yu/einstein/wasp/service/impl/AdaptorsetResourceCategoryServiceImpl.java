
/**
 *
 * AdaptorsetresourcecategoryServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AdaptorsetresourcecategoryService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AdaptorsetResourceCategoryDao;
import edu.yu.einstein.wasp.model.AdaptorsetResourceCategory;
import edu.yu.einstein.wasp.service.AdaptorsetResourceCategoryService;

@Service
public class AdaptorsetResourceCategoryServiceImpl extends WaspServiceImpl<AdaptorsetResourceCategory> implements AdaptorsetResourceCategoryService {

	/**
	 * adaptorsetresourcecategoryDao;
	 *
	 */
	private AdaptorsetResourceCategoryDao adaptorsetResourceCategoryDao;

	/**
	 * setAdaptorsetresourcecategoryDao(AdaptorsetresourcecategoryDao adaptorsetresourcecategoryDao)
	 *
	 * @param adaptorsetResourceCategoryDao
	 *
	 */
	@Override
	@Autowired
	public void setAdaptorsetResourceCategoryDao(AdaptorsetResourceCategoryDao adaptorsetResourceCategoryDao) {
		this.adaptorsetResourceCategoryDao = adaptorsetResourceCategoryDao;
		this.setWaspDao(adaptorsetResourceCategoryDao);
	}

	/**
	 * getAdaptorsetresourcecategoryDao();
	 *
	 * @return adaptorsetresourcecategoryDao
	 *
	 */
	@Override
	public AdaptorsetResourceCategoryDao getAdaptorsetResourceCategoryDao() {
		return this.adaptorsetResourceCategoryDao;
	}


  @Override
public AdaptorsetResourceCategory getAdaptorsetResourceCategoryByAdaptorsetresourcecategoryId (final Integer adaptorsetresourcecategoryId) {
    return this.getAdaptorsetResourceCategoryDao().getAdaptorsetResourceCategoryByAdaptorsetresourcecategoryId(adaptorsetresourcecategoryId);
  }

  @Override
public AdaptorsetResourceCategory getAdaptorsetResourceCategoryByAdaptorsetIdResourcecategoryId (final Integer adaptorsetId, final Integer resourcecategoryId) {
    return this.getAdaptorsetResourceCategoryDao().getAdaptorsetResourceCategoryByAdaptorsetIdResourcecategoryId(adaptorsetId, resourcecategoryId);
  }


}

