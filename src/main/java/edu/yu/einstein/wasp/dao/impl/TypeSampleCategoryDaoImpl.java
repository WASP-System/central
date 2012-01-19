
/**
 *
 * TypeSampleCategoryDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleCategory Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.TypeSampleCategory;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class TypeSampleCategoryDaoImpl extends WaspDaoImpl<TypeSampleCategory> implements edu.yu.einstein.wasp.dao.TypeSampleCategoryDao {

	/**
	 * TypeSampleCategoryDaoImpl() Constructor
	 *
	 *
	 */
	public TypeSampleCategoryDaoImpl() {
		super();
		this.entityClass = TypeSampleCategory.class;
	}


	/**
	 * getTypeSamplecategoryByTypeSamplecategoryId(final Integer typeSamplecategoryId)
	 *
	 * @param final Integer typeSamplecategoryId
	 *
	 * @return typeSampleCategory
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public TypeSampleCategory getTypeSampleCategoryByTypeSamplecategoryId (final Integer typeSamplecategoryId) {
    		HashMap m = new HashMap();
		m.put("typeSamplecategoryId", typeSamplecategoryId);

		List<TypeSampleCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			TypeSampleCategory rt = new TypeSampleCategory();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getTypeSamplecategoryByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return typeSampleCategory
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public TypeSampleCategory getTypeSampleCategoryByIName (final String iName) {
    		HashMap m = new HashMap();
		m.put("iName", iName);

		List<TypeSampleCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			TypeSampleCategory rt = new TypeSampleCategory();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getTypeSamplecategoryByName(final String name)
	 *
	 * @param final String name
	 *
	 * @return typeSampleCategory
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public TypeSampleCategory getTypeSampleCategoryByName (final String name) {
    		HashMap m = new HashMap();
		m.put("name", name);

		List<TypeSampleCategory> results = this.findByMap(m);

		if (results.size() == 0) {
			TypeSampleCategory rt = new TypeSampleCategory();
			return rt;
		}
		return results.get(0);
	}



}

