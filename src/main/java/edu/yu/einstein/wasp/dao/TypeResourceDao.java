
/**
 *
 * TypeResourceDao.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeResourceDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface TypeResourceDao extends WaspDao<TypeResource> {

  public TypeResource getTypeResourceByTypeResourceId (final int typeResourceId);

  public TypeResource getTypeResourceByIName (final String iName);

  public TypeResource getTypeResourceByName (final String name);

}

