
/**
 *
 * AdaptorsetMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AdaptorsetMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.AdaptorsetMeta;


public interface AdaptorsetMetaDao extends WaspMetaDao<AdaptorsetMeta> {

  public AdaptorsetMeta getAdaptorsetMetaByAdaptorsetMetaId (final Integer adaptorsetMetaId);

  public AdaptorsetMeta getAdaptorsetMetaByKAdaptorsetId (final String k, final Integer adaptorsetId);

 
}

