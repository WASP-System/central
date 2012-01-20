
/**
 *
 * TypeSampleresourcecategoryDao.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleresourcecategory Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface TypeSampleresourcecategoryDao extends WaspDao<TypeSampleresourcecategory> {

  public TypeSampleresourcecategory getTypeSampleresourcecategoryByTypeSampleresourcecategoryId (final Integer typeSampleresourcecategoryId);

  public TypeSampleresourcecategory getTypeSampleresourcecategoryByIName (final String iName);

  public TypeSampleresourcecategory getTypeSampleresourcecategoryByName (final String name);


}

