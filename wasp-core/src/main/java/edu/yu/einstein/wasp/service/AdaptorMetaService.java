
/**
 *
 * AdaptorMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the AdaptorMetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AdaptorMetaDao;
import edu.yu.einstein.wasp.model.AdaptorMeta;

@Service
public interface AdaptorMetaService extends WaspService<AdaptorMeta> {

	/**
	 * setAdaptorMetaDao(AdaptorMetaDao adaptorMetaDao)
	 *
	 * @param adaptorMetaDao
	 *
	 */
	public void setAdaptorMetaDao(AdaptorMetaDao adaptorMetaDao);

	/**
	 * getAdaptorMetaDao();
	 *
	 * @return adaptorMetaDao
	 *
	 */
	public AdaptorMetaDao getAdaptorMetaDao();

  public AdaptorMeta getAdaptorMetaByAdaptorMetaId (final Integer adaptorMetaId);

  public AdaptorMeta getAdaptorMetaByKAdaptorId (final String k, final Integer adaptorId);


  public void updateByAdaptorId (final String area, final int adaptorId, final List<AdaptorMeta> metaList);

  public void updateByAdaptorId (final int adaptorId, final List<AdaptorMeta> metaList);


}

