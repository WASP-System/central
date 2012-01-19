
/**
 *
 * TypeSampleCategoryResourceCategoryDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleCategoryResourceCategory Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.TypeSampleCategoryResourceCategory;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class TypeSampleCategoryResourceCategoryDaoImpl extends WaspDaoImpl<TypeSampleCategoryResourceCategory> implements edu.yu.einstein.wasp.dao.TypeSampleCategoryResourceCategoryDao {

	/**
	 * TypeSampleCategoryResourceCategoryDaoImpl() Constructor
	 *
	 *
	 */
	public TypeSampleCategoryResourceCategoryDaoImpl() {
		super();
		this.entityClass = TypeSampleCategoryResourceCategory.class;
	}


	/**
	 * getTypeSamplecategoryresourcecategoryByTypeSamplecategoryresourcecategoryId(final Integer typeSamplecategoryresourcecategoryId)
	 *
	 * @param final Integer typeSamplecategoryresourcecategoryId
	 *
	 * @return typeSampleCategoryResourceCategory
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public TypeSampleCategoryResourceCategory getTypeSampleCategoryResourceCategoryByTypeSamplecategoryresourcecategoryId (final Integer typeSamplecategoryresourcecategoryId) {
    		HashMap m = new HashMap();
		m.put("typeSamplecategoryresourcecategoryId", typeSamplecategoryresourcecategoryId);

		List<TypeSampleCategoryResourceCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			TypeSampleCategoryResourceCategory rt = new TypeSampleCategoryResourceCategory();
			return rt;
		}
		return results.get(0);
	}



}

