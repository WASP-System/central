
/**
 *
 * SoftwareMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SoftwareMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SoftwareMetaDao;
import edu.yu.einstein.wasp.model.SoftwareMeta;
import edu.yu.einstein.wasp.service.SoftwareMetaService;

@Service
public class SoftwareMetaServiceImpl extends WaspServiceImpl<SoftwareMeta> implements SoftwareMetaService {

	/**
	 * softwareMetaDao;
	 *
	 */
	private SoftwareMetaDao softwareMetaDao;

	/**
	 * setSoftwareMetaDao(SoftwareMetaDao softwareMetaDao)
	 *
	 * @param softwareMetaDao
	 *
	 */
	@Override
	@Autowired
	public void setSoftwareMetaDao(SoftwareMetaDao softwareMetaDao) {
		this.softwareMetaDao = softwareMetaDao;
		this.setWaspDao(softwareMetaDao);
	}

	/**
	 * getSoftwareMetaDao();
	 *
	 * @return softwareMetaDao
	 *
	 */
	@Override
	public SoftwareMetaDao getSoftwareMetaDao() {
		return this.softwareMetaDao;
	}


  @Override
public SoftwareMeta getSoftwareMetaBySoftwareMetaId (final Integer softwareMetaId) {
    return this.getSoftwareMetaDao().getSoftwareMetaBySoftwareMetaId(softwareMetaId);
  }

  @Override
public SoftwareMeta getSoftwareMetaByKSoftwareId (final String k, final Integer softwareId) {
    return this.getSoftwareMetaDao().getSoftwareMetaByKSoftwareId(k, softwareId);
  }

  @Override
public void updateBySoftwareId (final String area, final int softwareId, final List<SoftwareMeta> metaList) {
    this.getSoftwareMetaDao().updateBySoftwareId(area, softwareId, metaList); 
  }

  @Override
public void updateBySoftwareId (final int softwareId, final List<SoftwareMeta> metaList) {
    this.getSoftwareMetaDao().updateBySoftwareId(softwareId, metaList); 
  }


}

