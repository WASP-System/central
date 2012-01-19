
/**
 *
 * StateDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the State Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.State;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class StateDaoImpl extends WaspDaoImpl<State> implements edu.yu.einstein.wasp.dao.StateDao {

	/**
	 * StateDaoImpl() Constructor
	 *
	 *
	 */
	public StateDaoImpl() {
		super();
		this.entityClass = State.class;
	}


	/**
	 * getStateByStateId(final int stateId)
	 *
	 * @param final int stateId
	 *
	 * @return state
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public State getStateByStateId (final int stateId) {
    		HashMap m = new HashMap();
		m.put("stateId", stateId);

		List<State> results = this.findByMap(m);

		if (results.size() == 0) {
			State rt = new State();
			return rt;
		}
		return results.get(0);
	}



}

