
/**
 *
 * TypeSampleResourceCategoryDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleResourceCategory Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.TypeSampleResourceCategory;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class TypeSampleResourceCategoryDaoImpl extends WaspDaoImpl<TypeSampleResourceCategory> implements edu.yu.einstein.wasp.dao.TypeSampleResourceCategoryDao {

	/**
	 * TypeSampleResourceCategoryDaoImpl() Constructor
	 *
	 *
	 */
	public TypeSampleResourceCategoryDaoImpl() {
		super();
		this.entityClass = TypeSampleResourceCategory.class;
	}


	/**
	 * getTypeSampleresourcecategoryByTypeSampleresourcecategoryId(final Integer typeSampleresourcecategoryId)
	 *
	 * @param final Integer typeSampleresourcecategoryId
	 *
	 * @return typeSampleResourceCategory
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public TypeSampleResourceCategory getTypeSampleResourceCategoryByTypeSampleResourceCategoryId (final Integer typeSampleResourceCategoryId) {
    		HashMap m = new HashMap();
		m.put("typeSampleresourcecategoryId", typeSampleResourceCategoryId);

		List<TypeSampleResourceCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			TypeSampleResourceCategory rt = new TypeSampleResourceCategory();
			return rt;
		}
		return results.get(0);
	}

	/**
	 * getTypeSampleResourceCategoryByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return typeSampleResourceCategory
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public TypeSampleResourceCategory getTypeSampleResourceCategoryByIName (final String iName) {
    		HashMap m = new HashMap();
		m.put("iName", iName);

		List<TypeSampleResourceCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			TypeSampleResourceCategory rt = new TypeSampleResourceCategory();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getTypeSampleResourceCategoryByName(final String name)
	 *
	 * @param final String name
	 *
	 * @return typeSampleResourceCategory
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public TypeSampleResourceCategory getTypeSampleResourceCategoryByName (final String name) {
    		HashMap m = new HashMap();
		m.put("name", name);

		List<TypeSampleResourceCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			TypeSampleResourceCategory rt = new TypeSampleResourceCategory();
			return rt;
		}
		return results.get(0);
	}

}

