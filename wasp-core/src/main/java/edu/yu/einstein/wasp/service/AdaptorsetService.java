
/**
 *
 * AdaptorsetService.java 
 * @author echeng (table2type.pl)
 *  
 * the AdaptorsetService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AdaptorsetDao;
import edu.yu.einstein.wasp.model.Adaptorset;

@Service
public interface AdaptorsetService extends WaspService<Adaptorset> {

	/**
	 * setAdaptorsetDao(AdaptorsetDao adaptorsetDao)
	 *
	 * @param adaptorsetDao
	 *
	 */
	public void setAdaptorsetDao(AdaptorsetDao adaptorsetDao);

	/**
	 * getAdaptorsetDao();
	 *
	 * @return adaptorsetDao
	 *
	 */
	public AdaptorsetDao getAdaptorsetDao();

  public Adaptorset getAdaptorsetByAdaptorsetId (final Integer adaptorsetId);

  public Adaptorset getAdaptorsetByIName (final String iName);

  public Adaptorset getAdaptorsetByName (final String name);


}

