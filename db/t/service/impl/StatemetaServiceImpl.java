
/**
 *
 * StatemetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the StatemetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.StatemetaService;
import edu.yu.einstein.wasp.dao.StatemetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Statemeta;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StatemetaServiceImpl extends WaspServiceImpl<Statemeta> implements StatemetaService {

  private StatemetaDao statemetaDao;
  @Autowired
  public void setStatemetaDao(StatemetaDao statemetaDao) {
    this.statemetaDao = statemetaDao;
    this.setWaspDao(statemetaDao);
  }
  public StatemetaDao getStatemetaDao() {
    return this.statemetaDao;
  }

  // **

  
  public Statemeta getStatemetaByStatemetaId (final int statemetaId) {
    return this.getStatemetaDao().getStatemetaByStatemetaId(statemetaId);
  }

  public Statemeta getStatemetaByKStateId (final String k, final int stateId) {
    return this.getStatemetaDao().getStatemetaByKStateId(k, stateId);
  }

  public void updateByStateId (final int stateId, final List<Statemeta> metaList) {
    this.getStatemetaDao().updateByStateId(stateId, metaList); 
  }

}

