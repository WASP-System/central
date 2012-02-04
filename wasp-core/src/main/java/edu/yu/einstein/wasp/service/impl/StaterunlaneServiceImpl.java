
/**
 *
 * StaterunlaneServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the StaterunlaneService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.StaterunlaneDao;
import edu.yu.einstein.wasp.model.Staterunlane;
import edu.yu.einstein.wasp.service.StaterunlaneService;

@Service
public class StaterunlaneServiceImpl extends WaspServiceImpl<Staterunlane> implements StaterunlaneService {

	/**
	 * staterunlaneDao;
	 *
	 */
	private StaterunlaneDao staterunlaneDao;

	/**
	 * setStaterunlaneDao(StaterunlaneDao staterunlaneDao)
	 *
	 * @param staterunlaneDao
	 *
	 */
	@Override
	@Autowired
	public void setStaterunlaneDao(StaterunlaneDao staterunlaneDao) {
		this.staterunlaneDao = staterunlaneDao;
		this.setWaspDao(staterunlaneDao);
	}

	/**
	 * getStaterunlaneDao();
	 *
	 * @return staterunlaneDao
	 *
	 */
	@Override
	public StaterunlaneDao getStaterunlaneDao() {
		return this.staterunlaneDao;
	}


  @Override
public Staterunlane getStaterunlaneByStaterunlaneId (final int staterunlaneId) {
    return this.getStaterunlaneDao().getStaterunlaneByStaterunlaneId(staterunlaneId);
  }

}

