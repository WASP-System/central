
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

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.model.State;

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

