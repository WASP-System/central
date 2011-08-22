
/**
 *
 * StatejobService.java 
 * @author echeng (table2type.pl)
 *  
 * the StatejobService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.StatejobService;
import edu.yu.einstein.wasp.dao.StatejobDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Statejob;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StatejobServiceImpl extends WaspServiceImpl<Statejob> implements StatejobService {

  private StatejobDao statejobDao;
  @Autowired
  public void setStatejobDao(StatejobDao statejobDao) {
    this.statejobDao = statejobDao;
    this.setWaspDao(statejobDao);
  }
  public StatejobDao getStatejobDao() {
    return this.statejobDao;
  }

  // **

  
  public Statejob getStatejobByStatejobId (final int statejobId) {
    return this.getStatejobDao().getStatejobByStatejobId(statejobId);
  }
}

