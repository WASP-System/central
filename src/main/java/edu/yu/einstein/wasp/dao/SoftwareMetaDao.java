
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

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface SoftwareMetaDao extends WaspDao<SoftwareMeta> {

  public SoftwareMeta getSoftwareMetaBySoftwareMetaId (final Integer softwareMetaId);

  public SoftwareMeta getSoftwareMetaByKSoftwareId (final String k, final Integer softwareId);


  public void updateBySoftwareId (final String area, final int softwareId, final List<SoftwareMeta> metaList);

  public void updateBySoftwareId (final int softwareId, final List<SoftwareMeta> metaList);




}

