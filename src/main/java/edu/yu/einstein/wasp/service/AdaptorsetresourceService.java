
/**
 *
 * AdaptorsetresourceService.java 
 * @author echeng (table2type.pl)
 *  
 * the AdaptorsetresourceService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AdaptorsetresourceDao;
import edu.yu.einstein.wasp.model.Adaptorsetresource;

@Service
public interface AdaptorsetresourceService extends WaspService<Adaptorsetresource> {

	/**
	 * setAdaptorsetresourceDao(AdaptorsetresourceDao adaptorsetresourceDao)
	 *
	 * @param adaptorsetresourceDao
	 *
	 */
	public void setAdaptorsetresourceDao(AdaptorsetresourceDao adaptorsetresourceDao);

	/**
	 * getAdaptorsetresourceDao();
	 *
	 * @return adaptorsetresourceDao
	 *
	 */
	public AdaptorsetresourceDao getAdaptorsetresourceDao();

  public Adaptorsetresource getAdaptorsetresourceByAdaptorsetresourceId (final Integer adaptorsetresourceId);

  public Adaptorsetresource getAdaptorsetresourceByAdaptorsetIdResourceId (final Integer adaptorsetId, final Integer resourceId);



}

