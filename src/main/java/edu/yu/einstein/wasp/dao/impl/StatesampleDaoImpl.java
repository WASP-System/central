
/**
 *
 * StatesampleDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Statesample Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Statesample;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class StatesampleDaoImpl extends WaspDaoImpl<Statesample> implements edu.yu.einstein.wasp.dao.StatesampleDao {

	/**
	 * StatesampleDaoImpl() Constructor
	 *
	 *
	 */
	public StatesampleDaoImpl() {
		super();
		this.entityClass = Statesample.class;
	}


	/**
	 * getStatesampleByStatesampleId(final int statesampleId)
	 *
	 * @param final int statesampleId
	 *
	 * @return statesample
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Statesample getStatesampleByStatesampleId (final int statesampleId) {
    		HashMap m = new HashMap();
		m.put("statesampleId", statesampleId);

		List<Statesample> results = (List<Statesample>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Statesample rt = new Statesample();
			return rt;
		}
		return (Statesample) results.get(0);
	}



}

