
/**
 *
 * StaterunlaneService.java 
 * @author echeng (table2type.pl)
 *  
 * the StaterunlaneService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.StaterunlaneDao;
import edu.yu.einstein.wasp.model.Staterunlane;

@Service
public interface StaterunlaneService extends WaspService<Staterunlane> {

	/**
	 * setStaterunlaneDao(StaterunlaneDao staterunlaneDao)
	 *
	 * @param staterunlaneDao
	 *
	 */
	public void setStaterunlaneDao(StaterunlaneDao staterunlaneDao);

	/**
	 * getStaterunlaneDao();
	 *
	 * @return staterunlaneDao
	 *
	 */
	public StaterunlaneDao getStaterunlaneDao();

  public Staterunlane getStaterunlaneByStaterunlaneId (final int staterunlaneId);


}

