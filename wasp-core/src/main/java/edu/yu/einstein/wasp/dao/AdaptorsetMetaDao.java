
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

import java.util.List;

import edu.yu.einstein.wasp.model.AdaptorsetMeta;


public interface AdaptorsetMetaDao extends WaspDao<AdaptorsetMeta> {

  public AdaptorsetMeta getAdaptorsetMetaByAdaptorsetMetaId (final Integer adaptorsetMetaId);

  public AdaptorsetMeta getAdaptorsetMetaByKAdaptorsetId (final String k, final Integer adaptorsetId);

  public void updateByAdaptorsetId (final int adaptorsetId, final List<AdaptorsetMeta> metaList);




}

