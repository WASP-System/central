
/**
 *
 * ResourceDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Resource Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface ResourceDao extends WaspDao<Resource> {

  public Resource getResourceByResourceId (final int resourceId);

  public Resource getResourceByIName (final String iName);


}

