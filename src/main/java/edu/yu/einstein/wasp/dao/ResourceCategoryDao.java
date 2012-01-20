
/**
 *
 * ResourceCategoryDao.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceCategory Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface ResourceCategoryDao extends WaspDao<ResourceCategory> {

  public ResourceCategory getResourceCategoryByResourceCategoryId (final Integer resourceCategoryId);

  public ResourceCategory getResourceCategoryByIName (final String iName);

  public ResourceCategory getResourceCategoryByName (final String name);


}

