
/**
 *
 * AdaptorsetMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the AdaptorsetMetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AdaptorsetMetaDao;
import edu.yu.einstein.wasp.model.AdaptorsetMeta;

@Service
public interface AdaptorsetMetaService extends WaspService<AdaptorsetMeta> {

	/**
	 * setAdaptorsetMetaDao(AdaptorsetMetaDao adaptorsetMetaDao)
	 *
	 * @param adaptorsetMetaDao
	 *
	 */
	public void setAdaptorsetMetaDao(AdaptorsetMetaDao adaptorsetMetaDao);

	/**
	 * getAdaptorsetMetaDao();
	 *
	 * @return adaptorsetMetaDao
	 *
	 */
	public AdaptorsetMetaDao getAdaptorsetMetaDao();

  public AdaptorsetMeta getAdaptorsetMetaByAdaptorsetMetaId (final Integer adaptorsetMetaId);

  public AdaptorsetMeta getAdaptorsetMetaByKAdaptorsetId (final String k, final Integer adaptorsetId);


  public void updateByAdaptorsetId (final String area, final int adaptorsetId, final List<AdaptorsetMeta> metaList);

  public void updateByAdaptorsetId (final int adaptorsetId, final List<AdaptorsetMeta> metaList);


}

