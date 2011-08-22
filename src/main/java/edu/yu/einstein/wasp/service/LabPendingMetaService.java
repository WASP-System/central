
/**
 *
 * LabPendingMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the LabPendingMetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.LabPendingMetaDao;
import edu.yu.einstein.wasp.model.LabPendingMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface LabPendingMetaService extends WaspService<LabPendingMeta> {

  public void setLabPendingMetaDao(LabPendingMetaDao labPendingMetaDao);
  public LabPendingMetaDao getLabPendingMetaDao();

  public LabPendingMeta getLabPendingMetaByLabPendingMetaId (final int labPendingMetaId);

  public LabPendingMeta getLabPendingMetaByKLabpendingId (final String k, final int labpendingId);

  public void updateByLabpendingId (final int labpendingId, final List<LabPendingMeta> metaList);

}

