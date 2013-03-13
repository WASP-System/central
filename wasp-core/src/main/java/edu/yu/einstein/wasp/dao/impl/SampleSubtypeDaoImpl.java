
/**
 *
 * SampleSubtypeDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSubtype Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SampleSubtype;


@Transactional
@Repository
public class SampleSubtypeDaoImpl extends WaspDaoImpl<SampleSubtype> implements edu.yu.einstein.wasp.dao.SampleSubtypeDao {

	/**
	 * SampleSubtypeDaoImpl() Constructor
	 *
	 *
	 */
	public SampleSubtypeDaoImpl() {
		super();
		this.entityClass = SampleSubtype.class;
	}


	/**
	 * getSampleSubtypeBySampleSubtypeId(final int sampleSubtypeId)
	 *
	 * @param final int sampleSubtypeId
	 *
	 * @return sampleSubtype
	 */

	@Override
	@Transactional
	public SampleSubtype getSampleSubtypeBySampleSubtypeId (final int sampleSubtypeId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", sampleSubtypeId);

		List<SampleSubtype> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleSubtype rt = new SampleSubtype();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSampleSubtypeByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return sampleSubtype
	 */

	@Override
	@Transactional
	public SampleSubtype getSampleSubtypeByIName (final String iName) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("iName", iName);

		List<SampleSubtype> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleSubtype rt = new SampleSubtype();
			return rt;
		}
		return results.get(0);
	}

	  @Override
	  public List<SampleSubtype> getActiveSampleSubtypes(){
		  Map<String, Integer> queryMap = new HashMap<String, Integer>();
		  queryMap.put("isActive", 1);
		  return this.findByMap(queryMap);
	  }

}

