
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

import edu.yu.einstein.wasp.service.SoftwareMetaService;
import edu.yu.einstein.wasp.dao.SoftwareMetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.SoftwareMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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
	public SoftwareMetaDao getSoftwareMetaDao() {
		return this.softwareMetaDao;
	}


  public SoftwareMeta getSoftwareMetaBySoftwareMetaId (final Integer softwareMetaId) {
    return this.getSoftwareMetaDao().getSoftwareMetaBySoftwareMetaId(softwareMetaId);
  }

  public SoftwareMeta getSoftwareMetaByKSoftwareId (final String k, final Integer softwareId) {
    return this.getSoftwareMetaDao().getSoftwareMetaByKSoftwareId(k, softwareId);
  }

  public void updateBySoftwareId (final String area, final int softwareId, final List<SoftwareMeta> metaList) {
    this.getSoftwareMetaDao().updateBySoftwareId(area, softwareId, metaList); 
  }

  public void updateBySoftwareId (final int softwareId, final List<SoftwareMeta> metaList) {
    this.getSoftwareMetaDao().updateBySoftwareId(softwareId, metaList); 
  }


}

