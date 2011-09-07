
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
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

	@SuppressWarnings("unchecked")
	@Transactional
	public Staterunlane getStaterunlaneByStaterunlaneId (final int staterunlaneId) {
    		HashMap m = new HashMap();
		m.put("staterunlaneId", staterunlaneId);

		List<Staterunlane> results = (List<Staterunlane>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Staterunlane rt = new Staterunlane();
			return rt;
		}
		return (Staterunlane) results.get(0);
	}



}

