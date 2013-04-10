
/**
 *
 * LabPendingDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the LabPending Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.LabPending;


@Transactional
@Repository
public class LabPendingDaoImpl extends WaspDaoImpl<LabPending> implements edu.yu.einstein.wasp.dao.LabPendingDao {

	/**
	 * LabPendingDaoImpl() Constructor
	 *
	 *
	 */
	public LabPendingDaoImpl() {
		super();
		this.entityClass = LabPending.class;
	}


	/**
	 * getLabPendingByLabPendingId(final int labPendingId)
	 *
	 * @param final int labPendingId
	 *
	 * @return labPending
	 */

	@Override
	@Transactional
	public LabPending getLabPendingByLabPendingId (final int labPendingId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", labPendingId);

		List<LabPending> results = this.findByMap(m);

		if (results.size() == 0) {
			LabPending rt = new LabPending();
			return rt;
		}
		return results.get(0);
	}



}

