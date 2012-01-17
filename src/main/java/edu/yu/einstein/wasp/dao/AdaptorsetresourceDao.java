
/**
 *
 * AdaptorsetresourceDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Adaptorsetresource Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.Adaptorsetresource;


public interface AdaptorsetresourceDao extends WaspDao<Adaptorsetresource> {

  public Adaptorsetresource getAdaptorsetresourceByAdaptorsetresourceId (final Integer adaptorsetresourceId);

  public Adaptorsetresource getAdaptorsetresourceByAdaptorsetIdResourceId (final Integer adaptorsetId, final Integer resourceId);



}

