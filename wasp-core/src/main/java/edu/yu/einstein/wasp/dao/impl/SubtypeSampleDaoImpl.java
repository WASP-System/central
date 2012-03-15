
/**
 *
 * SubtypeSampleDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SubtypeSample Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SubtypeSample;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SubtypeSampleDaoImpl extends WaspDaoImpl<SubtypeSample> implements edu.yu.einstein.wasp.dao.SubtypeSampleDao {

	/**
	 * SubtypeSampleDaoImpl() Constructor
	 *
	 *
	 */
	public SubtypeSampleDaoImpl() {
		super();
		this.entityClass = SubtypeSample.class;
	}


	/**
	 * getSubtypeSampleBySubtypeSampleId(final int subtypeSampleId)
	 *
	 * @param final int subtypeSampleId
	 *
	 * @return subtypeSample
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public SubtypeSample getSubtypeSampleBySubtypeSampleId (final int subtypeSampleId) {
    		HashMap m = new HashMap();
		m.put("subtypeSampleId", subtypeSampleId);

		List<SubtypeSample> results = this.findByMap(m);

		if (results.size() == 0) {
			SubtypeSample rt = new SubtypeSample();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSubtypeSampleByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return subtypeSample
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public SubtypeSample getSubtypeSampleByIName (final String iName) {
    		HashMap m = new HashMap();
		m.put("iName", iName);

		List<SubtypeSample> results = this.findByMap(m);

		if (results.size() == 0) {
			SubtypeSample rt = new SubtypeSample();
			return rt;
		}
		return results.get(0);
	}

	  @Override
	  public List<SubtypeSample> getActiveSubtypeSamples(){
		  Map queryMap = new HashMap();
		  queryMap.put("isActive", 1);
		  return this.findByMap(queryMap);
	  }

}

