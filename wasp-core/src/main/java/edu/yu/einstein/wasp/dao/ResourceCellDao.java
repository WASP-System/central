
/**
 *
 * ResourceCellDao.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceCell Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.ResourceCell;


public interface ResourceCellDao extends WaspDao<ResourceCell> {

  public ResourceCell getResourceCellByResourceCellId (final Integer resourceCellId);

  public ResourceCell getResourceCellByINameResourceId (final String iName, final Integer resourceId);

  public ResourceCell getResourceCellByNameResourceId (final String name, final Integer resourceId);


}

