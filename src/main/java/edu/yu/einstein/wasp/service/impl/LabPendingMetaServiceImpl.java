
/**
 *
 * LabPendingMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the LabPendingMetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.LabPendingMetaService;
import edu.yu.einstein.wasp.dao.LabPendingMetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.LabPendingMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LabPendingMetaServiceImpl extends WaspServiceImpl<LabPendingMeta> implements LabPendingMetaService {

  private LabPendingMetaDao labPendingMetaDao;
  @Autowired
  public void setLabPendingMetaDao(LabPendingMetaDao labPendingMetaDao) {
    this.labPendingMetaDao = labPendingMetaDao;
    this.setWaspDao(labPendingMetaDao);
  }
  public LabPendingMetaDao getLabPendingMetaDao() {
    return this.labPendingMetaDao;
  }

  // **

  
  public LabPendingMeta getLabPendingMetaByLabPendingMetaId (final int labPendingMetaId) {
    return this.getLabPendingMetaDao().getLabPendingMetaByLabPendingMetaId(labPendingMetaId);
  }

  public LabPendingMeta getLabPendingMetaByKLabpendingId (final String k, final int labpendingId) {
    return this.getLabPendingMetaDao().getLabPendingMetaByKLabpendingId(k, labpendingId);
  }

  public void updateByLabpendingId (final int labpendingId, final List<LabPendingMeta> metaList) {
    this.getLabPendingMetaDao().updateByLabpendingId(labpendingId, metaList); 
  }

}

