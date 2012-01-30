
/**
 *
 * StateService.java 
 * @author echeng (table2type.pl)
 *  
 * the StateService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.model.State;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface StateService extends WaspService<State> {

	/**
	 * setStateDao(StateDao stateDao)
	 *
	 * @param stateDao
	 *
	 */
	public void setStateDao(StateDao stateDao);

	/**
	 * getStateDao();
	 *
	 * @return stateDao
	 *
	 */
	public StateDao getStateDao();

  public State getStateByStateId (final int stateId);


}

