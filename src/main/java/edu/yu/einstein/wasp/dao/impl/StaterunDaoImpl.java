
/**
 *
 * StaterunDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Staterun Dao Impl
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

import edu.yu.einstein.wasp.model.Staterun;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class StaterunDaoImpl extends WaspDaoImpl<Staterun> implements edu.yu.einstein.wasp.dao.StaterunDao {

	/**
	 * StaterunDaoImpl() Constructor
	 *
	 *
	 */
	public StaterunDaoImpl() {
		super();
		this.entityClass = Staterun.class;
	}


	/**
	 * getStaterunByStaterunId(final int staterunId)
	 *
	 * @param final int staterunId
	 *
	 * @return staterun
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Staterun getStaterunByStaterunId (final int staterunId) {
    		HashMap m = new HashMap();
		m.put("staterunId", staterunId);

		List<Staterun> results = (List<Staterun>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Staterun rt = new Staterun();
			return rt;
		}
		return (Staterun) results.get(0);
	}



}

