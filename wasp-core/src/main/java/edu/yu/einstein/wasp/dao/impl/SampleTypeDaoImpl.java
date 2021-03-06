
/**
 *
 * SampleTypeDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleType Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SampleType;


@Transactional("entityManager")
@Repository
public class SampleTypeDaoImpl extends WaspDaoImpl<SampleType> implements edu.yu.einstein.wasp.dao.SampleTypeDao {

	/**
	 * SampleTypeDaoImpl() Constructor
	 *
	 *
	 */
	public SampleTypeDaoImpl() {
		super();
		this.entityClass = SampleType.class;
	}


	/**
	 * getSampleTypeBySampleTypeId(final int sampleTypeId)
	 *
	 * @param final int sampleTypeId
	 *
	 * @return sampleType
	 */

	@Override
	@Transactional("entityManager")
	public SampleType getSampleTypeBySampleTypeId (final int sampleTypeId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", sampleTypeId);

		List<SampleType> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleType rt = new SampleType();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSampleTypeByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return sampleType
	 */

	@Override
	@Transactional("entityManager")
	public SampleType getSampleTypeByIName (final String iName) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("iName", iName);

		List<SampleType> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleType rt = new SampleType();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSampleTypeByName(final String name)
	 *
	 * @param final String name
	 *
	 * @return sampleType
	 */

	@Override
	@Transactional("entityManager")
	public SampleType getSampleTypeByName (final String name) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("name", name);

		List<SampleType> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleType rt = new SampleType();
			return rt;
		}
		return results.get(0);
	}



}

