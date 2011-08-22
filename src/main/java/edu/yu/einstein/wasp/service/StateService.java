
/**
 *
 * StateService.java 
 * @author echeng (table2type.pl)
 *  
 * the StateService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.model.State;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface StateService extends WaspService<State> {

  public void setStateDao(StateDao stateDao);
  public StateDao getStateDao();

  public State getStateByStateId (final int stateId);

}

