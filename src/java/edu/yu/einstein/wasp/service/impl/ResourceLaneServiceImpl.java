
/**
 *
 * ResourceLaneService.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceLaneService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.ResourceLaneService;
import edu.yu.einstein.wasp.dao.ResourceLaneDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.ResourceLane;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResourceLaneServiceImpl extends WaspServiceImpl<ResourceLane> implements ResourceLaneService {

  private ResourceLaneDao resourceLaneDao;
  @Autowired
  public void setResourceLaneDao(ResourceLaneDao resourceLaneDao) {
    this.resourceLaneDao = resourceLaneDao;
    this.setWaspDao(resourceLaneDao);
  }
  public ResourceLaneDao getResourceLaneDao() {
    return this.resourceLaneDao;
  }

  // **

  
  public ResourceLane getResourceLaneByResourceLaneId (final int resourceLaneId) {
    return this.getResourceLaneDao().getResourceLaneByResourceLaneId(resourceLaneId);
  }

  public ResourceLane getResourceLaneByINameResourceId (final String iName, final int resourceId) {
    return this.getResourceLaneDao().getResourceLaneByINameResourceId(iName, resourceId);
  }

  public ResourceLane getResourceLaneByNameResourceId (final String name, final int resourceId) {
    return this.getResourceLaneDao().getResourceLaneByNameResourceId(name, resourceId);
  }
}

