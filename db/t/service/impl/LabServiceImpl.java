
/**
 *
 * LabService.java 
 * @author echeng (table2type.pl)
 *  
 * the LabService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Lab;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LabServiceImpl extends WaspServiceImpl<Lab> implements LabService {

  private LabDao labDao;
  @Autowired
  public void setLabDao(LabDao labDao) {
    this.labDao = labDao;
    this.setWaspDao(labDao);
  }
  public LabDao getLabDao() {
    return this.labDao;
  }

  // **

  
  public Lab getLabByLabId (final int labId) {
    return this.getLabDao().getLabByLabId(labId);
  }

  public Lab getLabByName (final String name) {
    return this.getLabDao().getLabByName(name);
  }
}

