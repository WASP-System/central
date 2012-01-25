
/**
 *
 * SubtypeSampleResourceCategoryDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SubtypeSampleResourceCategory Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SubtypeSampleResourceCategory;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SubtypeSampleResourceCategoryDaoImpl extends WaspDaoImpl<SubtypeSampleResourceCategory> implements edu.yu.einstein.wasp.dao.SubtypeSampleResourceCategoryDao {

	/**
	 * SubtypeSampleResourceCategoryDaoImpl() Constructor
	 *
	 *
	 */
	public SubtypeSampleResourceCategoryDaoImpl() {
		super();
		this.entityClass = SubtypeSampleResourceCategory.class;
	}


	/**
	 * getSubtypeSampleresourcecategoryBySubtypeSampleresourcecategoryId(final Integer typeSampleresourcecategoryId)
	 *
	 * @param final Integer subtypeSampleresourcecategoryId
	 *
	 * @return typeSampleResourceCategory
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public SubtypeSampleResourceCategory getSubtypeSampleResourceCategoryBySubtypeSampleResourceCategoryId (final Integer subtypeSampleresourcecategoryId) {
    		HashMap m = new HashMap();
		m.put("subtypeSampleresourcecategoryId", subtypeSampleresourcecategoryId);

		List<SubtypeSampleResourceCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			SubtypeSampleResourceCategory rt = new SubtypeSampleResourceCategory();
			return rt;
		}
		return results.get(0);
	}

	/**
	 * getSubtypeSampleResourceCategoryByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return typeSampleResourceCategory
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public SubtypeSampleResourceCategory getSubtypeSampleResourceCategoryByIName (final String iName) {
    		HashMap m = new HashMap();
		m.put("iName", iName);

		List<SubtypeSampleResourceCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			SubtypeSampleResourceCategory rt = new SubtypeSampleResourceCategory();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSubtypeSampleResourceCategoryByName(final String name)
	 *
	 * @param final String name
	 *
	 * @return typeSampleResourceCategory
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public SubtypeSampleResourceCategory getSubtypeSampleResourceCategoryByName (final String name) {
    		HashMap m = new HashMap();
		m.put("name", name);

		List<SubtypeSampleResourceCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			SubtypeSampleResourceCategory rt = new SubtypeSampleResourceCategory();
			return rt;
		}
		return results.get(0);
	}

}

