
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
	 * getMetaByK(final String k)
	 *
	 * @param final String k
	 *
	 * @return meta
	 */

	@Override
	@Transactional("entityManager")
	public Meta getMetaByK (final String k) {
    		
		HashMap<String, String> m = new HashMap<String, String>();
		m.put("k", k);

		List<Meta> results = this.findByMap(m);

		if (results.size() == 0) {
			Meta rt = new Meta();
			return rt;
		}
		return results.get(0);
	}

	/**
	 * setMeta(final String k, final String v)
	 * creates and returns a new Meta object in memory only
	 * @param final String k, final String v
	 *
	 * @return meta
	 */

	@Override
	@Transactional("entityManager")
	 public Meta setMeta(final String k, final String v){
		Meta meta = new Meta();
		meta.setK(k);
		meta.setV(v);
		return meta;
	}

	/**
	 * saveMeta(Meta)
	 *
	 * @param Meta meta
	 *
	 * @return meta
	 */

	@Override
	@Transactional("entityManager")
	public Meta saveMeta(Meta meta){
		return this.save(meta);
	}
}

