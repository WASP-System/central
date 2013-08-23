
/**
 *
 * ResourceCellDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceCell Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.ResourceCell;


@Transactional("entityManager")
@Repository
public class ResourceCellDaoImpl extends WaspDaoImpl<ResourceCell> implements edu.yu.einstein.wasp.dao.ResourceCellDao {

	/**
	 * ResourceCellDaoImpl() Constructor
	 *
	 *
	 */
	public ResourceCellDaoImpl() {
		super();
		this.entityClass = ResourceCell.class;
	}


	/**
	 * getResourceCellByResourceCellId(final Integer resourceCellId)
	 *
	 * @param final Integer resourceCellId
	 *
	 * @return resourceCell
	 */

	@Override
	@Transactional("entityManager")
	public ResourceCell getResourceCellByResourceCellId (final Integer resourceCellId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", resourceCellId);

		List<ResourceCell> results = this.findByMap(m);

		if (results.size() == 0) {
			ResourceCell rt = new ResourceCell();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getResourceCellByINameResourceId(final String iName, final Integer resourceId)
	 *
	 * @param final String iName, final Integer resourceId
	 *
	 * @return resourceCell
	 */

	@Override
	@Transactional("entityManager")
	public ResourceCell getResourceCellByINameResourceId (final String iName, final Integer resourceId) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("iName", iName);
		m.put("resourceId", resourceId.toString());

		List<ResourceCell> results = this.findByMap(m);

		if (results.size() == 0) {
			ResourceCell rt = new ResourceCell();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getResourceCellByNameResourceId(final String name, final Integer resourceId)
	 *
	 * @param final String name, final Integer resourceId
	 *
	 * @return resourceCell
	 */

	@Override
	@Transactional("entityManager")
	public ResourceCell getResourceCellByNameResourceId (final String name, final Integer resourceId) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("name", name);
		m.put("resourceId", resourceId.toString());

		List<ResourceCell> results = this.findByMap(m);

		if (results.size() == 0) {
			ResourceCell rt = new ResourceCell();
			return rt;
		}
		return results.get(0);
	}



}

