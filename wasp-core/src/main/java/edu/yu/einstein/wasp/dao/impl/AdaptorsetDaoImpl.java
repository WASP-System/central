
/**
 *
 * AdaptorsetDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Adaptorset Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Adaptorset;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AdaptorsetDaoImpl extends WaspDaoImpl<Adaptorset> implements edu.yu.einstein.wasp.dao.AdaptorsetDao {

	/**
	 * AdaptorsetDaoImpl() Constructor
	 *
	 *
	 */
	public AdaptorsetDaoImpl() {
		super();
		this.entityClass = Adaptorset.class;
	}


	/**
	 * getAdaptorsetByAdaptorsetId(final Integer adaptorsetId)
	 *
	 * @param final Integer adaptorsetId
	 *
	 * @return adaptorset
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Adaptorset getAdaptorsetByAdaptorsetId (final Integer adaptorsetId) {
    		HashMap m = new HashMap();
		m.put("adaptorsetId", adaptorsetId);

		List<Adaptorset> results = this.findByMap(m);

		if (results.size() == 0) {
			Adaptorset rt = new Adaptorset();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getAdaptorsetByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return adaptorset
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Adaptorset getAdaptorsetByIName (final String iName) {
    		HashMap m = new HashMap();
		m.put("iName", iName);

		List<Adaptorset> results = this.findByMap(m);

		if (results.size() == 0) {
			Adaptorset rt = new Adaptorset();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getAdaptorsetByName(final String name)
	 *
	 * @param final String name
	 *
	 * @return adaptorset
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Adaptorset getAdaptorsetByName (final String name) {
    		HashMap m = new HashMap();
		m.put("name", name);

		List<Adaptorset> results = this.findByMap(m);

		if (results.size() == 0) {
			Adaptorset rt = new Adaptorset();
			return rt;
		}
		return results.get(0);
	}



}

