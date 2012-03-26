
/**
 *
 * ResourceTypeDao.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceType Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.ResourceType;


public interface ResourceTypeDao extends WaspDao<ResourceType> {

  public ResourceType getResourceTypeByResourceTypeId (final Integer resourceTypeId);

  public ResourceType getResourceTypeByIName (final String iName);

  public ResourceType getResourceTypeByName (final String name);


}

