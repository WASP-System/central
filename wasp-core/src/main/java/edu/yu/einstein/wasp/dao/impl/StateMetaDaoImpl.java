
/**
 *
 * StateMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the StateMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.StateMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class StateMetaDaoImpl extends WaspDaoImpl<StateMeta> implements edu.yu.einstein.wasp.dao.StateMetaDao {

	/**
	 * StateMetaDaoImpl() Constructor
	 *
	 *
	 */
	public StateMetaDaoImpl() {
		super();
		this.entityClass = StateMeta.class;
	}


	/**
	 * getStateMetaByStateMetaId(final int stateMetaId)
	 *
	 * @param final int stateMetaId
	 *
	 * @return stateMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public StateMeta getStateMetaByStateMetaId (final int stateMetaId) {
    		HashMap m = new HashMap();
		m.put("stateMetaId", stateMetaId);

		List<StateMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			StateMeta rt = new StateMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getStateMetaByKStateId(final String k, final int stateId)
	 *
	 * @param final String k, final int stateId
	 *
	 * @return stateMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public StateMeta getStateMetaByKStateId (final String k, final int stateId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("stateId", stateId);

		List<StateMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			StateMeta rt = new StateMeta();
			return rt;
		}
		return results.get(0);
	}




	/**
	 * updateByStateId (final int stateId, final List<StateMeta> metaList)
	 *
	 * @param stateId
	 * @param metaList
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByStateId (final int stateId, final List<StateMeta> metaList) {
		for (StateMeta m:metaList) {
			StateMeta currentMeta = getStateMetaByKStateId(m.getK(), stateId);
			if (currentMeta.getStateMetaId() == null){
				// metadata value not in database yet
				m.setStateId(stateId);
				entityManager.persist(m);
			} else if (!currentMeta.getV().equals(m.getV())){
				// meta exists already but value has changed
				currentMeta.setV(m.getV());
				entityManager.merge(currentMeta);
			} else{
				// no change to meta so do nothing
			}
		}
 	}



}

