
/**
 *
 * StateServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the StateService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.service.StateService;

@Service
public class StateServiceImpl extends WaspServiceImpl<State> implements StateService {

	/**
	 * stateDao;
	 *
	 */
	private StateDao stateDao;

	/**
	 * setStateDao(StateDao stateDao)
	 *
	 * @param stateDao
	 *
	 */
	@Autowired
	public void setStateDao(StateDao stateDao) {
		this.stateDao = stateDao;
		this.setWaspDao(stateDao);
	}

	/**
	 * getStateDao();
	 *
	 * @return stateDao
	 *
	 */
	public StateDao getStateDao() {
		return this.stateDao;
	}


  public State getStateByStateId (final int stateId) {
    return this.getStateDao().getStateByStateId(stateId);
  }

}

