
/**
 *
 * ResourceLaneServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceLaneService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.ResourceLaneService;
import edu.yu.einstein.wasp.dao.ResourceLaneDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.ResourceLane;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResourceLaneServiceImpl extends WaspServiceImpl<ResourceLane> implements ResourceLaneService {

	/**
	 * resourceLaneDao;
	 *
	 */
	private ResourceLaneDao resourceLaneDao;

	/**
	 * setResourceLaneDao(ResourceLaneDao resourceLaneDao)
	 *
	 * @param resourceLaneDao
	 *
	 */
	@Autowired
	public void setResourceLaneDao(ResourceLaneDao resourceLaneDao) {
		this.resourceLaneDao = resourceLaneDao;
		this.setWaspDao(resourceLaneDao);
	}

	/**
	 * getResourceLaneDao();
	 *
	 * @return resourceLaneDao
	 *
	 */
	public ResourceLaneDao getResourceLaneDao() {
		return this.resourceLaneDao;
	}


  public ResourceLane getResourceLaneByResourceLaneId (final Integer resourceLaneId) {
    return this.getResourceLaneDao().getResourceLaneByResourceLaneId(resourceLaneId);
  }

  public ResourceLane getResourceLaneByINameResourceId (final String iName, final Integer resourceId) {
    return this.getResourceLaneDao().getResourceLaneByINameResourceId(iName, resourceId);
  }

  public ResourceLane getResourceLaneByNameResourceId (final String name, final Integer resourceId) {
    return this.getResourceLaneDao().getResourceLaneByNameResourceId(name, resourceId);
  }

}

