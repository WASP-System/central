
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


@Transactional("entityManager")
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
	@Transactional("entityManager")
	public Lab getLabByLabId (final int labId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", labId);

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
	@Transactional("entityManager")
	public Lab getLabByName (final String name) {
    		HashMap<String, String> m = new HashMap<String, String>();
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
	@Transactional("entityManager")
	public Lab getLabByPrimaryUserId (final int primaryUserId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("primaryUserId", primaryUserId);

		List<Lab> results = this.findByMap(m);

		if (results.size() == 0) {
			Lab rt = new Lab();
			return rt;
		}
		return results.get(0);
	}

	  
	  @Override
	  public List<Lab> getActiveLabs(){
		  Map<String, Integer> queryMap = new HashMap<String, Integer>();
		  queryMap.put("isActive", 1);
		  return this.findByMap(queryMap);
	  }

}

