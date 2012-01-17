
/**
 *
 * AdaptorsetMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AdaptorsetMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AdaptorsetMetaDao;
import edu.yu.einstein.wasp.model.AdaptorsetMeta;
import edu.yu.einstein.wasp.service.AdaptorsetMetaService;

@Service
public class AdaptorsetMetaServiceImpl extends WaspServiceImpl<AdaptorsetMeta> implements AdaptorsetMetaService {

	/**
	 * adaptorsetMetaDao;
	 *
	 */
	private AdaptorsetMetaDao adaptorsetMetaDao;

	/**
	 * setAdaptorsetMetaDao(AdaptorsetMetaDao adaptorsetMetaDao)
	 *
	 * @param adaptorsetMetaDao
	 *
	 */
	@Override
	@Autowired
	public void setAdaptorsetMetaDao(AdaptorsetMetaDao adaptorsetMetaDao) {
		this.adaptorsetMetaDao = adaptorsetMetaDao;
		this.setWaspDao(adaptorsetMetaDao);
	}

	/**
	 * getAdaptorsetMetaDao();
	 *
	 * @return adaptorsetMetaDao
	 *
	 */
	@Override
	public AdaptorsetMetaDao getAdaptorsetMetaDao() {
		return this.adaptorsetMetaDao;
	}


  @Override
  public AdaptorsetMeta getAdaptorsetMetaByAdaptorsetMetaId (final Integer adaptorsetMetaId) {
    return this.getAdaptorsetMetaDao().getAdaptorsetMetaByAdaptorsetMetaId(adaptorsetMetaId);
  }

  @Override
  public AdaptorsetMeta getAdaptorsetMetaByKAdaptorsetId (final String k, final Integer adaptorsetId) {
    return this.getAdaptorsetMetaDao().getAdaptorsetMetaByKAdaptorsetId(k, adaptorsetId);
  }

  @Override
  public void updateByAdaptorsetId (final String area, final int adaptorsetId, final List<AdaptorsetMeta> metaList) {
    this.getAdaptorsetMetaDao().updateByAdaptorsetId(area, adaptorsetId, metaList); 
  }

  @Override
  public void updateByAdaptorsetId (final int adaptorsetId, final List<AdaptorsetMeta> metaList) {
    this.getAdaptorsetMetaDao().updateByAdaptorsetId(adaptorsetId, metaList); 
  }


}

