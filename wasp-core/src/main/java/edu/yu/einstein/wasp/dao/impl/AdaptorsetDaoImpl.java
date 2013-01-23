
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
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Adaptorset;


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
	@Transactional
	public Adaptorset getAdaptorsetByAdaptorsetId (final Integer adaptorsetId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
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
	@Transactional
	public Adaptorset getAdaptorsetByIName (final String iName) {
    		HashMap<String, String> m = new HashMap<String, String>();
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
	@Transactional
	public Adaptorset getAdaptorsetByName (final String name) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("name", name);

		List<Adaptorset> results = this.findByMap(m);

		if (results.size() == 0) {
			Adaptorset rt = new Adaptorset();
			return rt;
		}
		return results.get(0);
	}

	  @Override
	  public List<Adaptorset> getActiveAdaptorsets(){
		  Map<String, Integer> queryMap = new HashMap<String, Integer>();
		  queryMap.put("isActive", 1);
		  return this.findByMap(queryMap);
	  }

}

