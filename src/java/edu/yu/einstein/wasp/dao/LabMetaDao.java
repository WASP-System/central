
/**
 *
 * LabMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the LabMetaDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface LabMetaDao extends WaspDao<LabMeta> {

  public LabMeta getLabMetaByLabMetaId (final int labMetaId);

  public LabMeta getLabMetaByKLabId (final String k, final int labId);

  public void updateByLabId (final int labId, final List<LabMeta> metaList);

}

