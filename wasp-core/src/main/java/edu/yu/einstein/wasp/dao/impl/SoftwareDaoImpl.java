
/**
 *
 * SoftwareDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Software Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Software;


@Transactional
@Repository
public class SoftwareDaoImpl extends WaspDaoImpl<Software> implements edu.yu.einstein.wasp.dao.SoftwareDao {

	/**
	 * SoftwareDaoImpl() Constructor
	 *
	 *
	 */
	public SoftwareDaoImpl() {
		super();
		this.entityClass = Software.class;
	}


	/**
	 * getSoftwareBySoftwareId(final Integer softwareId)
	 *
	 * @param final Integer softwareId
	 *
	 * @return software
	 */

	@Override
	@Transactional
	public Software getSoftwareBySoftwareId (final Integer softwareId) {
    	HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", softwareId);

		List<Software> results = this.findByMap(m);

		if (results.size() == 0) {
			Software rt = new Software();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSoftwareByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return software
	 */

	@Override
	@Transactional
	public Software getSoftwareByIName (final String iName) {
    	HashMap<String, String> m = new HashMap<String, String>();
		m.put("iName", iName);

		List<Software> results = this.findByMap(m);

		if (results.size() == 0) {
			Software rt = new Software();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSoftwareByName(final String name)
	 *
	 * @param final String name
	 *
	 * @return software
	 */

	@Override
	@Transactional
	public Software getSoftwareByName (final String name) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("name", name);

		List<Software> results = this.findByMap(m);

		if (results.size() == 0) {
			Software rt = new Software();
			return rt;
		}
		return results.get(0);
	}
	
	  
	  @Override
	  public List<Software> getActiveSoftware(){
		  Map<String, Integer> queryMap = new HashMap<String, Integer>();
		  queryMap.put("isActive", 1);
		  return this.findByMap(queryMap);
	  }


}

