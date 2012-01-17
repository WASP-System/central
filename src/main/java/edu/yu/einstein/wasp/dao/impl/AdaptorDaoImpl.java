
/**
 *
 * AdaptorDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Adaptor Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Adaptor;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AdaptorDaoImpl extends WaspDaoImpl<Adaptor> implements edu.yu.einstein.wasp.dao.AdaptorDao {

	/**
	 * AdaptorDaoImpl() Constructor
	 *
	 *
	 */
	public AdaptorDaoImpl() {
		super();
		this.entityClass = Adaptor.class;
	}


	/**
	 * getAdaptorByAdaptorId(final Integer adaptorId)
	 *
	 * @param final Integer adaptorId
	 *
	 * @return adaptor
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Adaptor getAdaptorByAdaptorId (final Integer adaptorId) {
    		HashMap m = new HashMap();
		m.put("adaptorId", adaptorId);

		List<Adaptor> results = this.findByMap(m);

		if (results.size() == 0) {
			Adaptor rt = new Adaptor();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getAdaptorByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return adaptor
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Adaptor getAdaptorByIName (final String iName) {
    		HashMap m = new HashMap();
		m.put("iName", iName);

		List<Adaptor> results = this.findByMap(m);

		if (results.size() == 0) {
			Adaptor rt = new Adaptor();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getAdaptorByName(final String name)
	 *
	 * @param final String name
	 *
	 * @return adaptor
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Adaptor getAdaptorByName (final String name) {
    		HashMap m = new HashMap();
		m.put("name", name);

		List<Adaptor> results = this.findByMap(m);

		if (results.size() == 0) {
			Adaptor rt = new Adaptor();
			return rt;
		}
		return results.get(0);
	}



}

