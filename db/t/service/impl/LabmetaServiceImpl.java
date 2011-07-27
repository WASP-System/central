
/**
 *
 * LabmetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the LabmetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.LabmetaService;
import edu.yu.einstein.wasp.dao.LabmetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Labmeta;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LabmetaServiceImpl extends WaspServiceImpl<Labmeta> implements LabmetaService {

  private LabmetaDao labmetaDao;
  @Autowired
  public void setLabmetaDao(LabmetaDao labmetaDao) {
    this.labmetaDao = labmetaDao;
    this.setWaspDao(labmetaDao);
  }
  public LabmetaDao getLabmetaDao() {
    return this.labmetaDao;
  }

  // **

  
  public Labmeta getLabmetaByLabmetaId (final int labmetaId) {
    return this.getLabmetaDao().getLabmetaByLabmetaId(labmetaId);
  }

  public Labmeta getLabmetaByKLabId (final String k, final int labId) {
    return this.getLabmetaDao().getLabmetaByKLabId(k, labId);
  }

  public void updateByLabId (final int labId, final List<Labmeta> metaList) {
    this.getLabmetaDao().updateByLabId(labId, metaList); 
  }

}

