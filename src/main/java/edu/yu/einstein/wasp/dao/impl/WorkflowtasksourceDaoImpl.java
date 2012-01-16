
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
	 * getWorkflowtasksourceByWorkflowtasksourceId(final int workflowtasksourceId)
	 *
	 * @param final int workflowtasksourceId
	 *
	 * @return workflowtasksource
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Workflowtasksource getWorkflowtasksourceByWorkflowtasksourceId (final int workflowtasksourceId) {
    		HashMap m = new HashMap();
		m.put("workflowtasksourceId", workflowtasksourceId);

		List<Workflowtasksource> results = (List<Workflowtasksource>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Workflowtasksource rt = new Workflowtasksource();
			return rt;
		}
		return (Workflowtasksource) results.get(0);
	}



}

