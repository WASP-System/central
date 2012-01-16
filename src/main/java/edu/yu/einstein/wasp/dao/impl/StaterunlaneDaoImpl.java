
/**
 *
 * StaterunlaneDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Staterunlane Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Staterunlane;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class StaterunlaneDaoImpl extends WaspDaoImpl<Staterunlane> implements edu.yu.einstein.wasp.dao.StaterunlaneDao {

	/**
	 * StaterunlaneDaoImpl() Constructor
	 *
	 *
	 */
	public StaterunlaneDaoImpl() {
		super();
		this.entityClass = Staterunlane.class;
	}


	/**
	 * getStaterunlaneByStaterunlaneId(final int staterunlaneId)
	 *
	 * @param final int staterunlaneId
	 *
	 * @return staterunlane
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Staterunlane getStaterunlaneByStaterunlaneId (final int staterunlaneId) {
    		HashMap m = new HashMap();
		m.put("staterunlaneId", staterunlaneId);

		List<Staterunlane> results = this.findByMap(m);

		if (results.size() == 0) {
			Staterunlane rt = new Staterunlane();
			return rt;
		}
		return results.get(0);
	}



}

