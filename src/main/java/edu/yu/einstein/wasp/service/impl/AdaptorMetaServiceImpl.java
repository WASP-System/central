
/**
 *
 * AdaptorMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AdaptorMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AdaptorMetaDao;
import edu.yu.einstein.wasp.model.AdaptorMeta;
import edu.yu.einstein.wasp.service.AdaptorMetaService;

@Service
public class AdaptorMetaServiceImpl extends WaspServiceImpl<AdaptorMeta> implements AdaptorMetaService {

	/**
	 * adaptorMetaDao;
	 *
	 */
	private AdaptorMetaDao adaptorMetaDao;

	/**
	 * setAdaptorMetaDao(AdaptorMetaDao adaptorMetaDao)
	 *
	 * @param adaptorMetaDao
	 *
	 */
	@Override
	@Autowired
	public void setAdaptorMetaDao(AdaptorMetaDao adaptorMetaDao) {
		this.adaptorMetaDao = adaptorMetaDao;
		this.setWaspDao(adaptorMetaDao);
	}

	/**
	 * getAdaptorMetaDao();
	 *
	 * @return adaptorMetaDao
	 *
	 */
	@Override
	public AdaptorMetaDao getAdaptorMetaDao() {
		return this.adaptorMetaDao;
	}


  @Override
public AdaptorMeta getAdaptorMetaByAdaptorMetaId (final Integer adaptorMetaId) {
    return this.getAdaptorMetaDao().getAdaptorMetaByAdaptorMetaId(adaptorMetaId);
  }

  @Override
public AdaptorMeta getAdaptorMetaByKAdaptorId (final String k, final Integer adaptorId) {
    return this.getAdaptorMetaDao().getAdaptorMetaByKAdaptorId(k, adaptorId);
  }

  @Override
public void updateByAdaptorId (final String area, final int adaptorId, final List<AdaptorMeta> metaList) {
    this.getAdaptorMetaDao().updateByAdaptorId(area, adaptorId, metaList); 
  }

  @Override
public void updateByAdaptorId (final int adaptorId, final List<AdaptorMeta> metaList) {
    this.getAdaptorMetaDao().updateByAdaptorId(adaptorId, metaList); 
  }


}

