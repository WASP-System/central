
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

import edu.yu.einstein.wasp.service.AdaptorsetresourcecategoryService;
import edu.yu.einstein.wasp.dao.AdaptorsetresourcecategoryDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Adaptorsetresourcecategory;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdaptorsetresourcecategoryServiceImpl extends WaspServiceImpl<Adaptorsetresourcecategory> implements AdaptorsetresourcecategoryService {

	/**
	 * adaptorsetresourcecategoryDao;
	 *
	 */
	private AdaptorsetresourcecategoryDao adaptorsetresourcecategoryDao;

	/**
	 * setAdaptorsetresourcecategoryDao(AdaptorsetresourcecategoryDao adaptorsetresourcecategoryDao)
	 *
	 * @param adaptorsetresourcecategoryDao
	 *
	 */
	@Autowired
	public void setAdaptorsetresourcecategoryDao(AdaptorsetresourcecategoryDao adaptorsetresourcecategoryDao) {
		this.adaptorsetresourcecategoryDao = adaptorsetresourcecategoryDao;
		this.setWaspDao(adaptorsetresourcecategoryDao);
	}

	/**
	 * getAdaptorsetresourcecategoryDao();
	 *
	 * @return adaptorsetresourcecategoryDao
	 *
	 */
	public AdaptorsetresourcecategoryDao getAdaptorsetresourcecategoryDao() {
		return this.adaptorsetresourcecategoryDao;
	}


  public Adaptorsetresourcecategory getAdaptorsetresourcecategoryByAdaptorsetresourcecategoryId (final Integer adaptorsetresourcecategoryId) {
    return this.getAdaptorsetresourcecategoryDao().getAdaptorsetresourcecategoryByAdaptorsetresourcecategoryId(adaptorsetresourcecategoryId);
  }

  public Adaptorsetresourcecategory getAdaptorsetresourcecategoryByAdaptorsetIdResourcecategoryId (final Integer adaptorsetId, final Integer resourcecategoryId) {
    return this.getAdaptorsetresourcecategoryDao().getAdaptorsetresourcecategoryByAdaptorsetIdResourcecategoryId(adaptorsetId, resourcecategoryId);
  }

}

