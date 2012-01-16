
/**
 *
 * StaterunServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the StaterunService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.StaterunDao;
import edu.yu.einstein.wasp.model.Staterun;
import edu.yu.einstein.wasp.service.StaterunService;

@Service
public class StaterunServiceImpl extends WaspServiceImpl<Staterun> implements StaterunService {

	/**
	 * staterunDao;
	 *
	 */
	private StaterunDao staterunDao;

	/**
	 * setStaterunDao(StaterunDao staterunDao)
	 *
	 * @param staterunDao
	 *
	 */
	@Autowired
	public void setStaterunDao(StaterunDao staterunDao) {
		this.staterunDao = staterunDao;
		this.setWaspDao(staterunDao);
	}

	/**
	 * getStaterunDao();
	 *
	 * @return staterunDao
	 *
	 */
	public StaterunDao getStaterunDao() {
		return this.staterunDao;
	}


  public Staterun getStaterunByStaterunId (final int staterunId) {
    return this.getStaterunDao().getStaterunByStaterunId(staterunId);
  }

}

