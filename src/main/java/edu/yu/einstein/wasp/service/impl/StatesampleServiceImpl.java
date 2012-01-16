
/**
 *
 * StatesampleServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the StatesampleService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.StatesampleDao;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.service.StatesampleService;

@Service
public class StatesampleServiceImpl extends WaspServiceImpl<Statesample> implements StatesampleService {

	/**
	 * statesampleDao;
	 *
	 */
	private StatesampleDao statesampleDao;

	/**
	 * setStatesampleDao(StatesampleDao statesampleDao)
	 *
	 * @param statesampleDao
	 *
	 */
	@Autowired
	public void setStatesampleDao(StatesampleDao statesampleDao) {
		this.statesampleDao = statesampleDao;
		this.setWaspDao(statesampleDao);
	}

	/**
	 * getStatesampleDao();
	 *
	 * @return statesampleDao
	 *
	 */
	public StatesampleDao getStatesampleDao() {
		return this.statesampleDao;
	}


  public Statesample getStatesampleByStatesampleId (final int statesampleId) {
    return this.getStatesampleDao().getStatesampleByStatesampleId(statesampleId);
  }

}

