
/**
 *
 * WorkflowtaskDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowtask Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Workflowtask;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class WorkflowtaskDaoImpl extends WaspDaoImpl<Workflowtask> implements edu.yu.einstein.wasp.dao.WorkflowtaskDao {

	/**
	 * WorkflowtaskDaoImpl() Constructor
	 *
	 *
	 */
	public WorkflowtaskDaoImpl() {
		super();
		this.entityClass = Workflowtask.class;
	}


	/**
	 * getWorkflowtaskByWorkflowtaskId(final Integer workflowtaskId)
	 *
	 * @param final Integer workflowtaskId
	 *
	 * @return workflowtask
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Workflowtask getWorkflowtaskByWorkflowtaskId (final Integer workflowtaskId) {
    		HashMap m = new HashMap();
		m.put("workflowtaskId", workflowtaskId);

		List<Workflowtask> results = this.findByMap(m);

		if (results.size() == 0) {
			Workflowtask rt = new Workflowtask();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getWorkflowtaskByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return workflowtask
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Workflowtask getWorkflowtaskByIName (final String iName) {
    		HashMap m = new HashMap();
		m.put("iName", iName);

		List<Workflowtask> results = this.findByMap(m);

		if (results.size() == 0) {
			Workflowtask rt = new Workflowtask();
			return rt;
		}
		return results.get(0);
	}



}
