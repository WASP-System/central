
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

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface TypeResourceDao extends WaspDao<TypeResource> {

  public TypeResource getTypeResourceByTypeResourceId (final int typeResourceId);

  public TypeResource getTypeResourceByIName (final String iName);

  public TypeResource getTypeResourceByName (final String name);


}

