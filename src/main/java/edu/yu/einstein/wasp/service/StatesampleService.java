
/**
 *
 * StatesampleService.java 
 * @author echeng (table2type.pl)
 *  
 * the StatesampleService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.StatesampleDao;
import edu.yu.einstein.wasp.model.Statesample;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface StatesampleService extends WaspService<Statesample> {

	/**
	 * setStatesampleDao(StatesampleDao statesampleDao)
	 *
	 * @param statesampleDao
	 *
	 */
	public void setStatesampleDao(StatesampleDao statesampleDao);

	/**
	 * getStatesampleDao();
	 *
	 * @return statesampleDao
	 *
	 */
	public StatesampleDao getStatesampleDao();

  public Statesample getStatesampleByStatesampleId (final int statesampleId);


}

