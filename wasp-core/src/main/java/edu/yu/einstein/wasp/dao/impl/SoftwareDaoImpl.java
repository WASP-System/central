
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

@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
	@Transactional
	public Software getSoftwareBySoftwareId (final Integer softwareId) {
    		HashMap m = new HashMap();
		m.put("softwareId", softwareId);

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
	@SuppressWarnings("unchecked")
	@Transactional
	public Software getSoftwareByIName (final String iName) {
    		HashMap m = new HashMap();
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
	@SuppressWarnings("unchecked")
	@Transactional
	public Software getSoftwareByName (final String name) {
    		HashMap m = new HashMap();
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
		  Map queryMap = new HashMap();
		  queryMap.put("isActive", 1);
		  return this.findByMap(queryMap);
	  }


}

