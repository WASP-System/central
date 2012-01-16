
/**
 *
 * TypeResourceDao.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeResource Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.TypeResource;


public interface TypeResourceDao extends WaspDao<TypeResource> {

  public TypeResource getTypeResourceByTypeResourceId (final int typeResourceId);

  public TypeResource getTypeResourceByIName (final String iName);

  public TypeResource getTypeResourceByName (final String name);


}

