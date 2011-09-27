
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

import edu.yu.einstein.wasp.service.StatesampleService;
import edu.yu.einstein.wasp.dao.StatesampleDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Statesample;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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

