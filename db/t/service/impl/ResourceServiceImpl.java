
/**
 *
 * ResourceService.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.ResourceService;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Resource;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResourceServiceImpl extends WaspServiceImpl<Resource> implements ResourceService {

  private ResourceDao resourceDao;
  @Autowired
  public void setResourceDao(ResourceDao resourceDao) {
    this.resourceDao = resourceDao;
    this.setWaspDao(resourceDao);
  }
  public ResourceDao getResourceDao() {
    return this.resourceDao;
  }

  // **

  
  public Resource getResourceByResourceId (final int resourceId) {
    return this.getResourceDao().getResourceByResourceId(resourceId);
  }

  public Resource getResourceByName (final String name) {
    return this.getResourceDao().getResourceByName(name);
  }
}

