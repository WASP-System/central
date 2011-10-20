
/**
 *
 * StateMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the StateMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.StateMetaService;
import edu.yu.einstein.wasp.dao.StateMetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.StateMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StateMetaServiceImpl extends WaspMetaServiceImpl<StateMeta> implements StateMetaService {

	/**
	 * stateMetaDao;
	 *
	 */
	private StateMetaDao stateMetaDao;

	/**
	 * setStateMetaDao(StateMetaDao stateMetaDao)
	 *
	 * @param stateMetaDao
	 *
	 */
	@Autowired
	public void setStateMetaDao(StateMetaDao stateMetaDao) {
		this.stateMetaDao = stateMetaDao;
		this.setWaspDao(stateMetaDao);
	}

	/**
	 * getStateMetaDao();
	 *
	 * @return stateMetaDao
	 *
	 */
	public StateMetaDao getStateMetaDao() {
		return this.stateMetaDao;
	}


  public StateMeta getStateMetaByStateMetaId (final int stateMetaId) {
    return this.getStateMetaDao().getStateMetaByStateMetaId(stateMetaId);
  }

  public StateMeta getStateMetaByKStateId (final String k, final int stateId) {
    return this.getStateMetaDao().getStateMetaByKStateId(k, stateId);
  }

  public void updateByStateId (final String area, final int stateId, final List<StateMeta> metaList) {
    this.getStateMetaDao().updateByStateId(area, stateId, metaList); 
  }

  public void updateByStateId (final int stateId, final List<StateMeta> metaList) {
    this.getStateMetaDao().updateByStateId(stateId, metaList); 
  }


}

