
/**
 *
 * StaterunlaneService.java 
 * @author echeng (table2type.pl)
 *  
 * the StaterunlaneService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.StaterunlaneService;
import edu.yu.einstein.wasp.dao.StaterunlaneDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Staterunlane;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StaterunlaneServiceImpl extends WaspServiceImpl<Staterunlane> implements StaterunlaneService {

  private StaterunlaneDao staterunlaneDao;
  @Autowired
  public void setStaterunlaneDao(StaterunlaneDao staterunlaneDao) {
    this.staterunlaneDao = staterunlaneDao;
    this.setWaspDao(staterunlaneDao);
  }
  public StaterunlaneDao getStaterunlaneDao() {
    return this.staterunlaneDao;
  }

  // **

  
  public Staterunlane getStaterunlaneByStaterunlaneId (final int staterunlaneId) {
    return this.getStaterunlaneDao().getStaterunlaneByStaterunlaneId(staterunlaneId);
  }
}

