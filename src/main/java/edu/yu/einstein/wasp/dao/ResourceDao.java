
/**
 *
 * ResourceDao.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface ResourceDao extends WaspDao<Resource> {

  public Resource getResourceByResourceId (final int resourceId);

  public Resource getResourceByName (final String name);

}

