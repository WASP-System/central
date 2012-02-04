
/**
 *
 * StateMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the StateMetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.StateMetaDao;
import edu.yu.einstein.wasp.model.StateMeta;

@Service
public interface StateMetaService extends WaspMetaService<StateMeta> {

	/**
	 * setStateMetaDao(StateMetaDao stateMetaDao)
	 *
	 * @param stateMetaDao
	 *
	 */
	public void setStateMetaDao(StateMetaDao stateMetaDao);

	/**
	 * getStateMetaDao();
	 *
	 * @return stateMetaDao
	 *
	 */
	public StateMetaDao getStateMetaDao();

  public StateMeta getStateMetaByStateMetaId (final int stateMetaId);

  public StateMeta getStateMetaByKStateId (final String k, final int stateId);


  public void updateByStateId (final String area, final int stateId, final List<StateMeta> metaList);

  public void updateByStateId (final int stateId, final List<StateMeta> metaList);


}

