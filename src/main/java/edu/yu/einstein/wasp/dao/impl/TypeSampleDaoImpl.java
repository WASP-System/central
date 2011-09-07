
/**
 *
 * TypeSampleDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSample Dao Impl
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
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.TypeSample;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class TypeSampleDaoImpl extends WaspDaoImpl<TypeSample> implements edu.yu.einstein.wasp.dao.TypeSampleDao {

	/**
	 * TypeSampleDaoImpl() Constructor
	 *
	 *
	 */
	public TypeSampleDaoImpl() {
		super();
		this.entityClass = TypeSample.class;
	}


	/**
	 * getTypeSampleByTypeSampleId(final int typeSampleId)
	 *
	 * @param final int typeSampleId
	 *
	 * @return typeSample
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public TypeSample getTypeSampleByTypeSampleId (final int typeSampleId) {
    		HashMap m = new HashMap();
		m.put("typeSampleId", typeSampleId);

		List<TypeSample> results = (List<TypeSample>) this.findByMap((Map) m);

		if (results.size() == 0) {
			TypeSample rt = new TypeSample();
			return rt;
		}
		return (TypeSample) results.get(0);
	}



	/**
	 * getTypeSampleByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return typeSample
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public TypeSample getTypeSampleByIName (final String iName) {
    		HashMap m = new HashMap();
		m.put("iName", iName);

		List<TypeSample> results = (List<TypeSample>) this.findByMap((Map) m);

		if (results.size() == 0) {
			TypeSample rt = new TypeSample();
			return rt;
		}
		return (TypeSample) results.get(0);
	}



	/**
	 * getTypeSampleByName(final String name)
	 *
	 * @param final String name
	 *
	 * @return typeSample
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public TypeSample getTypeSampleByName (final String name) {
    		HashMap m = new HashMap();
		m.put("name", name);

		List<TypeSample> results = (List<TypeSample>) this.findByMap((Map) m);

		if (results.size() == 0) {
			TypeSample rt = new TypeSample();
			return rt;
		}
		return (TypeSample) results.get(0);
	}



}

