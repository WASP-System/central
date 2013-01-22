
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

import edu.yu.einstein.wasp.model.SoftwareMeta;


public interface SoftwareMetaDao extends WaspMetaDao<SoftwareMeta> {

  public SoftwareMeta getSoftwareMetaBySoftwareMetaId (final Integer softwareMetaId);

  public SoftwareMeta getSoftwareMetaByKSoftwareId (final String k, final Integer softwareId);

 


}

