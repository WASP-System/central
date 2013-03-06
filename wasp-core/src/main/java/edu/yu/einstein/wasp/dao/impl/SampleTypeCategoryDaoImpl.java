
/**
 *
 * SampleTypeCategoryDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleTypeCategory Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SampleTypeCategory;


@Transactional
@Repository
public class SampleTypeCategoryDaoImpl extends WaspDaoImpl<SampleTypeCategory> implements edu.yu.einstein.wasp.dao.SampleTypeCategoryDao {

	/**
	 * SampleTypeCategoryDaoImpl() Constructor
	 *
	 *
	 */
	public SampleTypeCategoryDaoImpl() {
		super();
		this.entityClass = SampleTypeCategory.class;
	}


	/**
	 * getSampleTypecategoryBySampleTypecategoryId(final Integer sampleTypecategoryId)
	 *
	 * @param final Integer sampleTypecategoryId
	 *
	 * @return sampleTypeCategory
	 */

	@Override
	@Transactional
	public SampleTypeCategory getSampleTypeCategoryBySampleTypecategoryId (final Integer sampleTypecategoryId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", sampleTypecategoryId);

		List<SampleTypeCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleTypeCategory rt = new SampleTypeCategory();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSampleTypecategoryByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return sampleTypeCategory
	 */

	@Override
	@Transactional
	public SampleTypeCategory getSampleTypeCategoryByIName (final String iName) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("iName", iName);

		List<SampleTypeCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleTypeCategory rt = new SampleTypeCategory();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSampleTypecategoryByName(final String name)
	 *
	 * @param final String name
	 *
	 * @return sampleTypeCategory
	 */

	@Override
	@Transactional
	public SampleTypeCategory getSampleTypeCategoryByName (final String name) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("name", name);

		List<SampleTypeCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleTypeCategory rt = new SampleTypeCategory();
			return rt;
		}
		return results.get(0);
	}



}

