
/**
 *
 * StaterunService.java 
 * @author echeng (table2type.pl)
 *  
 * the StaterunService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.StaterunDao;
import edu.yu.einstein.wasp.model.Staterun;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface StaterunService extends WaspService<Staterun> {

	/**
	 * setStaterunDao(StaterunDao staterunDao)
	 *
	 * @param staterunDao
	 *
	 */
	public void setStaterunDao(StaterunDao staterunDao);

	/**
	 * getStaterunDao();
	 *
	 * @return staterunDao
	 *
	 */
	public StaterunDao getStaterunDao();

  public Staterun getStaterunByStaterunId (final int staterunId);


}

