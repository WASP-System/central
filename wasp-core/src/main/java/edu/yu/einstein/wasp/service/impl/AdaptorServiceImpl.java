
/**
 *
 * AdaptorServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AdaptorService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.service.AdaptorService;

@Service
public class AdaptorServiceImpl extends WaspServiceImpl<Adaptor> implements AdaptorService {

	/**
	 * adaptorDao;
	 *
	 */
	private AdaptorDao adaptorDao;

	/**
	 * setAdaptorDao(AdaptorDao adaptorDao)
	 *
	 * @param adaptorDao
	 *
	 */
	@Override
	@Autowired
	public void setAdaptorDao(AdaptorDao adaptorDao) {
		this.adaptorDao = adaptorDao;
		this.setWaspDao(adaptorDao);
	}

	/**
	 * getAdaptorDao();
	 *
	 * @return adaptorDao
	 *
	 */
	@Override
	public AdaptorDao getAdaptorDao() {
		return this.adaptorDao;
	}


  @Override
  public Adaptor getAdaptorByAdaptorId (final Integer adaptorId) {
    return this.getAdaptorDao().getAdaptorByAdaptorId(adaptorId);
  }

  @Override
  public Adaptor getAdaptorByIName (final String iName) {
    return this.getAdaptorDao().getAdaptorByIName(iName);
  }

  @Override
  public Adaptor getAdaptorByName (final String name) {
    return this.getAdaptorDao().getAdaptorByName(name);
  }

}

