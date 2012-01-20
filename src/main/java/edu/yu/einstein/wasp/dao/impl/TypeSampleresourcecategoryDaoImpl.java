
/**
 *
 * TypeSampleresourcecategoryDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleresourcecategory Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.TypeSampleresourcecategory;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class TypeSampleresourcecategoryDaoImpl extends WaspDaoImpl<TypeSampleresourcecategory> implements edu.yu.einstein.wasp.dao.TypeSampleresourcecategoryDao {

	/**
	 * TypeSampleresourcecategoryDaoImpl() Constructor
	 *
	 *
	 */
	public TypeSampleresourcecategoryDaoImpl() {
		super();
		this.entityClass = TypeSampleresourcecategory.class;
	}


	/**
	 * getTypeSampleresourcecategoryByTypeSampleresourcecategoryId(final Integer typeSampleresourcecategoryId)
	 *
	 * @param final Integer typeSampleresourcecategoryId
	 *
	 * @return typeSampleresourcecategory
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public TypeSampleresourcecategory getTypeSampleresourcecategoryByTypeSampleresourcecategoryId (final Integer typeSampleresourcecategoryId) {
    		HashMap m = new HashMap();
		m.put("typeSampleresourcecategoryId", typeSampleresourcecategoryId);

		List<TypeSampleresourcecategory> results = (List<TypeSampleresourcecategory>) this.findByMap((Map) m);

		if (results.size() == 0) {
			TypeSampleresourcecategory rt = new TypeSampleresourcecategory();
			return rt;
		}
		return (TypeSampleresourcecategory) results.get(0);
	}



	/**
	 * getTypeSampleresourcecategoryByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return typeSampleresourcecategory
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public TypeSampleresourcecategory getTypeSampleresourcecategoryByIName (final String iName) {
    		HashMap m = new HashMap();
		m.put("iName", iName);

		List<TypeSampleresourcecategory> results = (List<TypeSampleresourcecategory>) this.findByMap((Map) m);

		if (results.size() == 0) {
			TypeSampleresourcecategory rt = new TypeSampleresourcecategory();
			return rt;
		}
		return (TypeSampleresourcecategory) results.get(0);
	}



	/**
	 * getTypeSampleresourcecategoryByName(final String name)
	 *
	 * @param final String name
	 *
	 * @return typeSampleresourcecategory
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public TypeSampleresourcecategory getTypeSampleresourcecategoryByName (final String name) {
    		HashMap m = new HashMap();
		m.put("name", name);

		List<TypeSampleresourcecategory> results = (List<TypeSampleresourcecategory>) this.findByMap((Map) m);

		if (results.size() == 0) {
			TypeSampleresourcecategory rt = new TypeSampleresourcecategory();
			return rt;
		}
		return (TypeSampleresourcecategory) results.get(0);
	}



}

