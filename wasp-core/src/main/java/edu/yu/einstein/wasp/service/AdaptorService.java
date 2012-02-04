
/**
 *
 * AdaptorService.java 
 * @author echeng (table2type.pl)
 *  
 * the AdaptorService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.model.Adaptor;

@Service
public interface AdaptorService extends WaspService<Adaptor> {

	/**
	 * setAdaptorDao(AdaptorDao adaptorDao)
	 *
	 * @param adaptorDao
	 *
	 */
	public void setAdaptorDao(AdaptorDao adaptorDao);

	/**
	 * getAdaptorDao();
	 *
	 * @return adaptorDao
	 *
	 */
	public AdaptorDao getAdaptorDao();

  public Adaptor getAdaptorByAdaptorId (final Integer adaptorId);

  public Adaptor getAdaptorByIName (final String iName);

  public Adaptor getAdaptorByName (final String name);


}

