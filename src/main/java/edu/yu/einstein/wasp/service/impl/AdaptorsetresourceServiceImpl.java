
/**
 *
 * AdaptorsetresourceServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AdaptorsetresourceService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AdaptorsetresourceDao;
import edu.yu.einstein.wasp.model.Adaptorsetresource;
import edu.yu.einstein.wasp.service.AdaptorsetresourceService;

@Service
public class AdaptorsetresourceServiceImpl extends WaspServiceImpl<Adaptorsetresource> implements AdaptorsetresourceService {

	/**
	 * adaptorsetresourceDao;
	 *
	 */
	private AdaptorsetresourceDao adaptorsetresourceDao;

	/**
	 * setAdaptorsetresourceDao(AdaptorsetresourceDao adaptorsetresourceDao)
	 *
	 * @param adaptorsetresourceDao
	 *
	 */
	@Override
	@Autowired
	public void setAdaptorsetresourceDao(AdaptorsetresourceDao adaptorsetresourceDao) {
		this.adaptorsetresourceDao = adaptorsetresourceDao;
		this.setWaspDao(adaptorsetresourceDao);
	}

	/**
	 * getAdaptorsetresourceDao();
	 *
	 * @return adaptorsetresourceDao
	 *
	 */
	@Override
	public AdaptorsetresourceDao getAdaptorsetresourceDao() {
		return this.adaptorsetresourceDao;
	}


  @Override
  public Adaptorsetresource getAdaptorsetresourceByAdaptorsetresourceId (final Integer adaptorsetresourceId) {
    return this.getAdaptorsetresourceDao().getAdaptorsetresourceByAdaptorsetresourceId(adaptorsetresourceId);
  }

  @Override
  public Adaptorsetresource getAdaptorsetresourceByAdaptorsetIdResourceId (final Integer adaptorsetId, final Integer resourceId) {
    return this.getAdaptorsetresourceDao().getAdaptorsetresourceByAdaptorsetIdResourceId(adaptorsetId, resourceId);
  }

}

