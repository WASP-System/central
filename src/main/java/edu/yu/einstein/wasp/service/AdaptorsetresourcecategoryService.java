
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

import edu.yu.einstein.wasp.dao.AdaptorsetresourcecategoryDao;
import edu.yu.einstein.wasp.model.Adaptorsetresourcecategory;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface AdaptorsetresourcecategoryService extends WaspService<Adaptorsetresourcecategory> {

	/**
	 * setAdaptorsetresourcecategoryDao(AdaptorsetresourcecategoryDao adaptorsetresourcecategoryDao)
	 *
	 * @param adaptorsetresourcecategoryDao
	 *
	 */
	public void setAdaptorsetresourcecategoryDao(AdaptorsetresourcecategoryDao adaptorsetresourcecategoryDao);

	/**
	 * getAdaptorsetresourcecategoryDao();
	 *
	 * @return adaptorsetresourcecategoryDao
	 *
	 */
	public AdaptorsetresourcecategoryDao getAdaptorsetresourcecategoryDao();

  public Adaptorsetresourcecategory getAdaptorsetresourcecategoryByAdaptorsetresourcecategoryId (final Integer adaptorsetresourcecategoryId);

  public Adaptorsetresourcecategory getAdaptorsetresourcecategoryByAdaptorsetIdResourcecategoryId (final Integer adaptorsetId, final Integer resourcecategoryId);


}

