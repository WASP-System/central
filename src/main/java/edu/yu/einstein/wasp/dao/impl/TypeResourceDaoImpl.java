
/**
 *
 * TypeResourceDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeResource Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.TypeResource;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class TypeResourceDaoImpl extends WaspDaoImpl<TypeResource> implements edu.yu.einstein.wasp.dao.TypeResourceDao {

	/**
	 * TypeResourceDaoImpl() Constructor
	 *
	 *
	 */
	public TypeResourceDaoImpl() {
		super();
		this.entityClass = TypeResource.class;
	}


	/**
	 * getTypeResourceByTypeResourceId(final int typeResourceId)
	 *
	 * @param final int typeResourceId
	 *
	 * @return typeResource
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public TypeResource getTypeResourceByTypeResourceId (final int typeResourceId) {
    		HashMap m = new HashMap();
		m.put("typeResourceId", typeResourceId);

		List<TypeResource> results = this.findByMap(m);

		if (results.size() == 0) {
			TypeResource rt = new TypeResource();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getTypeResourceByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return typeResource
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public TypeResource getTypeResourceByIName (final String iName) {
    		HashMap m = new HashMap();
		m.put("iName", iName);

		List<TypeResource> results = this.findByMap(m);

		if (results.size() == 0) {
			TypeResource rt = new TypeResource();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getTypeResourceByName(final String name)
	 *
	 * @param final String name
	 *
	 * @return typeResource
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public TypeResource getTypeResourceByName (final String name) {
    		HashMap m = new HashMap();
		m.put("name", name);

		List<TypeResource> results = this.findByMap(m);

		if (results.size() == 0) {
			TypeResource rt = new TypeResource();
			return rt;
		}
		return results.get(0);
	}



}

