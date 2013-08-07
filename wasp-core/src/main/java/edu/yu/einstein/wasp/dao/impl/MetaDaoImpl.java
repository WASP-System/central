
/**
 *
 * MetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Meta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Meta;


@Transactional("entityManager")
@Repository
public class MetaDaoImpl extends WaspDaoImpl<Meta> implements edu.yu.einstein.wasp.dao.MetaDao {

	/**
	 * MetaDaoImpl() Constructor
	 *
	 *
	 */
	public MetaDaoImpl() {
		super();
		this.entityClass = Meta.class;
	}


	/**
	 * getMetaByMetaId(final int metaId)
	 *
	 * @param final int metaId
	 *
	 * @return meta
	 */

	@Override
	@Transactional("entityManager")
	public Meta getMetaByMetaId (final int metaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", metaId);

		List<Meta> results = this.findByMap(m);

		if (results.size() == 0) {
			Meta rt = new Meta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getMetaByPropertyK(final String property, final String k)
	 *
	 * @param final String property, final String k
	 *
	 * @return meta
	 */

	@Override
	@Transactional("entityManager")
	public Meta getMetaByPropertyK (final String property, final String k) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("property", property);
		m.put("k", k);

		List<Meta> results = this.findByMap(m);

		if (results.size() == 0) {
			Meta rt = new Meta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getMetaByPropertyV(final String property, final String v)
	 *
	 * @param final String property, final String v
	 *
	 * @return meta
	 */

	@Override
	@Transactional("entityManager")
	public Meta getMetaByPropertyV (final String property, final String v) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("property", property);
		m.put("v", v);

		List<Meta> results = this.findByMap(m);

		if (results.size() == 0) {
			Meta rt = new Meta();
			return rt;
		}
		return results.get(0);
	}



}

