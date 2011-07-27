
/**
 *
 * ResourceLaneService.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceLaneService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.ResourceLaneDao;
import edu.yu.einstein.wasp.model.ResourceLane;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface ResourceLaneService extends WaspService<ResourceLane> {

  public void setResourceLaneDao(ResourceLaneDao resourceLaneDao);
  public ResourceLaneDao getResourceLaneDao();

  public ResourceLane getResourceLaneByResourceLaneId (final int resourceLaneId);

  public ResourceLane getResourceLaneByINameResourceId (final String iName, final int resourceId);

  public ResourceLane getResourceLaneByNameResourceId (final String name, final int resourceId);

}

