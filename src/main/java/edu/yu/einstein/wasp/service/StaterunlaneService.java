
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

import edu.yu.einstein.wasp.dao.StaterunlaneDao;
import edu.yu.einstein.wasp.model.Staterunlane;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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

