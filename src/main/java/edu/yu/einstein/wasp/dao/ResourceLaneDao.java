
/**
 *
 * ResourceLaneDao.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceLaneDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface ResourceLaneDao extends WaspDao<ResourceLane> {

  public ResourceLane getResourceLaneByResourceLaneId (final int resourceLaneId);

  public ResourceLane getResourceLaneByINameResourceId (final String iName, final int resourceId);

  public ResourceLane getResourceLaneByNameResourceId (final String name, final int resourceId);

}

