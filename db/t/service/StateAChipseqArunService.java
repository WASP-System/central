
/**
 *
 * StateAChipseqArunService.java 
 * @author echeng (table2type.pl)
 *  
 * the StateAChipseqArunService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.StateAChipseqArunDao;
import edu.yu.einstein.wasp.model.StateAChipseqArun;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface StateAChipseqArunService extends WaspService<StateAChipseqArun> {

  public void setStateAChipseqArunDao(StateAChipseqArunDao stateAChipseqArunDao);
  public StateAChipseqArunDao getStateAChipseqArunDao();

  public StateAChipseqArun getStateAChipseqArunByStateArunId (final int stateArunId);

}

