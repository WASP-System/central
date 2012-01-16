
/**
 *
 * ResourceLaneDao.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceLane Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.ResourceLane;


public interface ResourceLaneDao extends WaspDao<ResourceLane> {

  public ResourceLane getResourceLaneByResourceLaneId (final int resourceLaneId);

  public ResourceLane getResourceLaneByINameResourceId (final String iName, final int resourceId);

  public ResourceLane getResourceLaneByNameResourceId (final String name, final int resourceId);


}

