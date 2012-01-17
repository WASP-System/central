
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

import java.util.List;

import edu.yu.einstein.wasp.model.AdaptorMeta;


public interface AdaptorMetaDao extends WaspDao<AdaptorMeta> {

  public AdaptorMeta getAdaptorMetaByAdaptorMetaId (final Integer adaptorMetaId);

  public AdaptorMeta getAdaptorMetaByKAdaptorId (final String k, final Integer adaptorId);


  public void updateByAdaptorId (final String area, final int adaptorId, final List<AdaptorMeta> metaList);

  public void updateByAdaptorId (final int adaptorId, final List<AdaptorMeta> metaList);




}

