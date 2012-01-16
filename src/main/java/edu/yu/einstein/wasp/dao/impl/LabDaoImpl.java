
/**
 *
 * LabDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Lab Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Lab;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class LabDaoImpl extends WaspDaoImpl<Lab> implements edu.yu.einstein.wasp.dao.LabDao {

	/**
	 * LabDaoImpl() Constructor
	 *
	 *
	 */
	public LabDaoImpl() {
		super();
		this.entityClass = Lab.class;
	}


	/**
	 * getLabByLabId(final int labId)
	 *
	 * @param final int labId
	 *
	 * @return lab
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Lab getLabByLabId (final int labId) {
    		HashMap m = new HashMap();
		m.put("labId", labId);

		List<Lab> results = this.findByMap(m);

		if (results.size() == 0) {
			Lab rt = new Lab();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getLabByName(final String name)
	 *
	 * @param final String name
	 *
	 * @return lab
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Lab getLabByName (final String name) {
    		HashMap m = new HashMap();
		m.put("name", name);

		List<Lab> results = this.findByMap(m);

		if (results.size() == 0) {
			Lab rt = new Lab();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getLabByPrimaryUserId(final int primaryUserId)
	 *
	 * @param final int primaryUserId
	 *
	 * @return lab
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Lab getLabByPrimaryUserId (final int primaryUserId) {
    		HashMap m = new HashMap();
		m.put("primaryUserId", primaryUserId);

		List<Lab> results = this.findByMap(m);

		if (results.size() == 0) {
			Lab rt = new Lab();
			return rt;
		}
		return results.get(0);
	}



}

