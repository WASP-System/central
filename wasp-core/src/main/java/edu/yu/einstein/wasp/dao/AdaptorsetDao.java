
/**
 *
 * AdaptorsetDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Adaptorset Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.Adaptorset;


public interface AdaptorsetDao extends WaspDao<Adaptorset> {

  public Adaptorset getAdaptorsetByAdaptorsetId (final Integer adaptorsetId);

  public Adaptorset getAdaptorsetByIName (final String iName);

  public Adaptorset getAdaptorsetByName (final String name);


}

