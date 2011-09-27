
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

import edu.yu.einstein.wasp.service.StaterunService;
import edu.yu.einstein.wasp.dao.StaterunDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Staterun;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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

