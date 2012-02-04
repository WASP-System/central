
/**
 *
 * SoftwareMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the SoftwareMetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SoftwareMetaDao;
import edu.yu.einstein.wasp.model.SoftwareMeta;

@Service
public interface SoftwareMetaService extends WaspService<SoftwareMeta> {

	/**
	 * setSoftwareMetaDao(SoftwareMetaDao softwareMetaDao)
	 *
	 * @param softwareMetaDao
	 *
	 */
	public void setSoftwareMetaDao(SoftwareMetaDao softwareMetaDao);

	/**
	 * getSoftwareMetaDao();
	 *
	 * @return softwareMetaDao
	 *
	 */
	public SoftwareMetaDao getSoftwareMetaDao();

  public SoftwareMeta getSoftwareMetaBySoftwareMetaId (final Integer softwareMetaId);

  public SoftwareMeta getSoftwareMetaByKSoftwareId (final String k, final Integer softwareId);


  public void updateBySoftwareId (final String area, final int softwareId, final List<SoftwareMeta> metaList);

  public void updateBySoftwareId (final int softwareId, final List<SoftwareMeta> metaList);


}

