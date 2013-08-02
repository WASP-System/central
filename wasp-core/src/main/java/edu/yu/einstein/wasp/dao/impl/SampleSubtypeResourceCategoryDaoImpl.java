
/**
 *
 * SampleSubtypeResourceCategoryDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSubtypeResourceCategory Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SampleSubtypeResourceCategory;


@Transactional("entityManager")
@Repository
public class SampleSubtypeResourceCategoryDaoImpl extends WaspDaoImpl<SampleSubtypeResourceCategory> implements edu.yu.einstein.wasp.dao.SampleSubtypeResourceCategoryDao {

	/**
	 * SampleSubtypeResourceCategoryDaoImpl() Constructor
	 *
	 *
	 */
	public SampleSubtypeResourceCategoryDaoImpl() {
		super();
		this.entityClass = SampleSubtypeResourceCategory.class;
	}


	/**
	 * getSampleSubtypeResourceCategoryBySampleSubtypeResourceCategoryId(final Integer sampleTyperesourcecategoryId)
	 *
	 * @param final Integer sampleSubtypeResourceCategoryId
	 *
	 * @return sampleResourceTypeCategory
	 */

	@Override
	@Transactional("entityManager")
	public SampleSubtypeResourceCategory getSampleSubtypeResourceCategoryBySampleSubtypeResourceCategoryId (final Integer sampleSubtypeResourceCategoryId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", sampleSubtypeResourceCategoryId);

		List<SampleSubtypeResourceCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleSubtypeResourceCategory rt = new SampleSubtypeResourceCategory();
			return rt;
		}
		return results.get(0);
	}

	@Override
	@Transactional("entityManager")
	public SampleSubtypeResourceCategory getSampleSubtypeResourceCategoryBySampleSubtypeIdResourceCategoryId(final Integer sampleSubtypeId, final Integer resourceCategoryId){
		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("sampleSubtypeId", sampleSubtypeId);
		m.put("resourcecategoryId", resourceCategoryId);

		List<SampleSubtypeResourceCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleSubtypeResourceCategory rt = new SampleSubtypeResourceCategory();
			return rt;
		}
		return results.get(0);
	}
}
