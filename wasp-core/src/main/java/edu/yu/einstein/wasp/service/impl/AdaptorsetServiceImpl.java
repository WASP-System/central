
/**
 *
 * AdaptorsetServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AdaptorsetService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AdaptorsetDao;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.service.AdaptorsetService;

@Service
public class AdaptorsetServiceImpl extends WaspServiceImpl<Adaptorset> implements AdaptorsetService {

	/**
	 * adaptorsetDao;
	 *
	 */
	private AdaptorsetDao adaptorsetDao;

	/**
	 * setAdaptorsetDao(AdaptorsetDao adaptorsetDao)
	 *
	 * @param adaptorsetDao
	 *
	 */
	@Override
	@Autowired
	public void setAdaptorsetDao(AdaptorsetDao adaptorsetDao) {
		this.adaptorsetDao = adaptorsetDao;
		this.setWaspDao(adaptorsetDao);
	}

	/**
	 * getAdaptorsetDao();
	 *
	 * @return adaptorsetDao
	 *
	 */
	@Override
	public AdaptorsetDao getAdaptorsetDao() {
		return this.adaptorsetDao;
	}


  @Override
  public Adaptorset getAdaptorsetByAdaptorsetId (final Integer adaptorsetId) {
    return this.getAdaptorsetDao().getAdaptorsetByAdaptorsetId(adaptorsetId);
  }

  @Override
  public Adaptorset getAdaptorsetByIName (final String iName) {
    return this.getAdaptorsetDao().getAdaptorsetByIName(iName);
  }

  @Override
  public Adaptorset getAdaptorsetByName (final String name) {
    return this.getAdaptorsetDao().getAdaptorsetByName(name);
  }

}

