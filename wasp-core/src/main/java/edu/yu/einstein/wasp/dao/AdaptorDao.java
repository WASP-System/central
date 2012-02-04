
/**
 *
 * AdaptorDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Adaptor Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.Adaptor;


public interface AdaptorDao extends WaspDao<Adaptor> {

  public Adaptor getAdaptorByAdaptorId (final Integer adaptorId);

  public Adaptor getAdaptorByIName (final String iName);

  public Adaptor getAdaptorByName (final String name);


}

