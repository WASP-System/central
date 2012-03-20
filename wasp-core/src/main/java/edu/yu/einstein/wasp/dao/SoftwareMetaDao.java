
/**
 *
 * SoftwareMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SoftwareMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.SoftwareMeta;


public interface SoftwareMetaDao extends WaspDao<SoftwareMeta> {

  public SoftwareMeta getSoftwareMetaBySoftwareMetaId (final Integer softwareMetaId);

  public SoftwareMeta getSoftwareMetaByKSoftwareId (final String k, final Integer softwareId);

  public void updateBySoftwareId (final int softwareId, final List<SoftwareMeta> metaList);




}

