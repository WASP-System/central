
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
import java.util.Map;

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
	public TypeSampleResourceCategory getTypeSampleResourceCategoryByTypeSampleresourcecategoryId (final Integer typeSampleresourcecategoryId) {
    		HashMap m = new HashMap();
		m.put("typeSampleresourcecategoryId", typeSampleresourcecategoryId);

		List<TypeSampleResourceCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			TypeSampleResourceCategory rt = new TypeSampleResourceCategory();
			return rt;
		}
		return results.get(0);
	}



}

