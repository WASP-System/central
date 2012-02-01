
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.ResourceLaneDao;
import edu.yu.einstein.wasp.model.ResourceLane;
import edu.yu.einstein.wasp.service.ResourceLaneService;

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
	@Override
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
	@Override
	public ResourceLaneDao getResourceLaneDao() {
		return this.resourceLaneDao;
	}


  @Override
public ResourceLane getResourceLaneByResourceLaneId (final Integer resourceLaneId) {
    return this.getResourceLaneDao().getResourceLaneByResourceLaneId(resourceLaneId);
  }

  @Override
public ResourceLane getResourceLaneByINameResourceId (final String iName, final Integer resourceId) {
    return this.getResourceLaneDao().getResourceLaneByINameResourceId(iName, resourceId);
  }

  @Override
public ResourceLane getResourceLaneByNameResourceId (final String name, final Integer resourceId) {
    return this.getResourceLaneDao().getResourceLaneByNameResourceId(name, resourceId);
  }

}

