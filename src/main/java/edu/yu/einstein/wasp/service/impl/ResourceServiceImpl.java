
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

import edu.yu.einstein.wasp.service.ResourceService;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Resource;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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
	public ResourceDao getResourceDao() {
		return this.resourceDao;
	}


  public Resource getResourceByResourceId (final Integer resourceId) {
    return this.getResourceDao().getResourceByResourceId(resourceId);
  }

  public Resource getResourceByIName (final String iName) {
    return this.getResourceDao().getResourceByIName(iName);
  }

  public Resource getResourceByName (final String name) {
    return this.getResourceDao().getResourceByName(name);
  }

}

