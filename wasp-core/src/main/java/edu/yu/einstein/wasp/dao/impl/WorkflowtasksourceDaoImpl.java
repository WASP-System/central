
/**
 *
 * WorkflowtasksourceDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowtasksource Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Workflowtasksource;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class WorkflowtasksourceDaoImpl extends WaspDaoImpl<Workflowtasksource> implements edu.yu.einstein.wasp.dao.WorkflowtasksourceDao {

	/**
	 * WorkflowtasksourceDaoImpl() Constructor
	 *
	 *
	 */
	public WorkflowtasksourceDaoImpl() {
		super();
		this.entityClass = Workflowtasksource.class;
	}


	/**
	 * getWorkflowtasksourceByWorkflowtasksourceId(final Integer workflowtasksourceId)
	 *
	 * @param final Integer workflowtasksourceId
	 *
	 * @return workflowtasksource
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Workflowtasksource getWorkflowtasksourceByWorkflowtasksourceId (final Integer workflowtasksourceId) {
    		HashMap m = new HashMap();
		m.put("workflowtasksourceId", workflowtasksourceId);

		List<Workflowtasksource> results = this.findByMap(m);

		if (results.size() == 0) {
			Workflowtasksource rt = new Workflowtasksource();
			return rt;
		}
		return results.get(0);
	}



}
