
/**
 *
 * ResourceServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.service.ResourceService;

@Service
public class ResourceServiceImpl extends WaspServiceImpl<Resource> implements ResourceService {

	/**
	 * resourceDao;
	 *
	 */
	private ResourceDao resourceDao;

	/**
	 * setResourceDao(ResourceDao resourceDao)
	 *
	 * @param resourceDao
	 *
	 */
	@Override
	@Autowired
	public void setResourceDao(ResourceDao resourceDao) {
		this.resourceDao = resourceDao;
		this.setWaspDao(resourceDao);
	}

	/**
	 * getResourceDao();
	 *
	 * @return resourceDao
	 *
	 */
	@Override
	public ResourceDao getResourceDao() {
		return this.resourceDao;
	}


  @Override
public Resource getResourceByResourceId (final int resourceId) {
    return this.getResourceDao().getResourceByResourceId(resourceId);
  }

  @Override
public Resource getResourceByIName (final String iName) {
    return this.getResourceDao().getResourceByIName(iName);
  }

}

