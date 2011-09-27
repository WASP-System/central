
/**
 *
 * LabPendingMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the LabPendingMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface LabPendingMetaDao extends WaspDao<LabPendingMeta> {

  public LabPendingMeta getLabPendingMetaByLabPendingMetaId (final int labPendingMetaId);

  public LabPendingMeta getLabPendingMetaByKLabpendingId (final String k, final int labpendingId);

  public void updateByLabpendingId (final int labpendingId, final List<LabPendingMeta> metaList);


}

