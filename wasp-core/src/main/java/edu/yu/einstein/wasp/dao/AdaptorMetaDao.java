
/**
 *
 * AdaptorMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AdaptorMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.AdaptorMeta;


public interface AdaptorMetaDao extends WaspMetaDao<AdaptorMeta> {

  public AdaptorMeta getAdaptorMetaByAdaptorMetaId (final Integer adaptorMetaId);

  public AdaptorMeta getAdaptorMetaByKAdaptorId (final String k, final Integer adaptorId);

 


}

