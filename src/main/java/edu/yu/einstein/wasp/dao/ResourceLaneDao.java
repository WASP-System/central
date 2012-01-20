
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

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface ResourceLaneDao extends WaspDao<ResourceLane> {

  public ResourceLane getResourceLaneByResourceLaneId (final Integer resourceLaneId);

  public ResourceLane getResourceLaneByINameResourceId (final String iName, final Integer resourceId);

  public ResourceLane getResourceLaneByNameResourceId (final String name, final Integer resourceId);


}

