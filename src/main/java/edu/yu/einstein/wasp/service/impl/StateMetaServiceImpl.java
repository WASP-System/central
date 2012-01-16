
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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.StateMetaDao;
import edu.yu.einstein.wasp.model.StateMeta;
import edu.yu.einstein.wasp.service.StateMetaService;

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
	@Override
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
	@Override
	public StateMetaDao getStateMetaDao() {
		return this.stateMetaDao;
	}


  @Override
public StateMeta getStateMetaByStateMetaId (final int stateMetaId) {
    return this.getStateMetaDao().getStateMetaByStateMetaId(stateMetaId);
  }

  @Override
public StateMeta getStateMetaByKStateId (final String k, final int stateId) {
    return this.getStateMetaDao().getStateMetaByKStateId(k, stateId);
  }

  @Override
public void updateByStateId (final String area, final int stateId, final List<StateMeta> metaList) {
    this.getStateMetaDao().updateByStateId(area, stateId, metaList); 
  }

  @Override
public void updateByStateId (final int stateId, final List<StateMeta> metaList) {
    this.getStateMetaDao().updateByStateId(stateId, metaList); 
  }


}

