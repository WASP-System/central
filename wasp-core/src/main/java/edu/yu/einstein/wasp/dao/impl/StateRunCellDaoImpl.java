
/**
 *
 * StateruncellDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the StateRunCell Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.StateRunCell;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class StateRunCellDaoImpl extends WaspDaoImpl<StateRunCell> implements edu.yu.einstein.wasp.dao.StateRunCellDao {

	/**
	 * StateruncellDaoImpl() Constructor
	 *
	 *
	 */
	public StateRunCellDaoImpl() {
		super();
		this.entityClass = StateRunCell.class;
	}


	/**
	 * getStateruncellByStateruncellId(final int stateruncellId)
	 *
	 * @param final int stateruncellId
	 *
	 * @return stateRunCell
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public StateRunCell getStateRunCellByStateruncellId (final int stateruncellId) {
    		HashMap m = new HashMap();
		m.put("stateruncellId", stateruncellId);

		List<StateRunCell> results = this.findByMap(m);

		if (results.size() == 0) {
			StateRunCell rt = new StateRunCell();
			return rt;
		}
		return results.get(0);
	}



}

