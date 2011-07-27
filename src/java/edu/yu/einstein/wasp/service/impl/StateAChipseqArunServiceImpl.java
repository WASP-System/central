
/**
 *
 * StateAChipseqArunService.java 
 * @author echeng (table2type.pl)
 *  
 * the StateAChipseqArunService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.StateAChipseqArunService;
import edu.yu.einstein.wasp.dao.StateAChipseqArunDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.StateAChipseqArun;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StateAChipseqArunServiceImpl extends WaspServiceImpl<StateAChipseqArun> implements StateAChipseqArunService {

  private StateAChipseqArunDao stateAChipseqArunDao;
  @Autowired
  public void setStateAChipseqArunDao(StateAChipseqArunDao stateAChipseqArunDao) {
    this.stateAChipseqArunDao = stateAChipseqArunDao;
    this.setWaspDao(stateAChipseqArunDao);
  }
  public StateAChipseqArunDao getStateAChipseqArunDao() {
    return this.stateAChipseqArunDao;
  }

  // **

  
  public StateAChipseqArun getStateAChipseqArunByStateArunId (final int stateArunId) {
    return this.getStateAChipseqArunDao().getStateAChipseqArunByStateArunId(stateArunId);
  }
}

