
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
	 * updateByStateId (final string area, final int stateId, final List<StateMeta> metaList)
	 *
	 * @param stateId
	 * @param metaList
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByStateId (final String area, final int stateId, final List<StateMeta> metaList) {
		entityManager.createNativeQuery("delete from statemeta where stateId=:stateId and k like :area").setParameter("stateId", stateId).setParameter("area", area + ".%").executeUpdate();

		for (StateMeta m:metaList) {
			m.setStateId(stateId);
			entityManager.persist(m);
		}
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
		entityManager.createNativeQuery("delete from statemeta where stateId=:stateId").setParameter("stateId", stateId).executeUpdate();

		for (StateMeta m:metaList) {
			m.setStateId(stateId);
			entityManager.persist(m);
		}
 	}



}
