
/**
 *
 * TypeSampleDao.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSample Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface TypeSampleDao extends WaspDao<TypeSample> {

  public TypeSample getTypeSampleByTypeSampleId (final int typeSampleId);

  public TypeSample getTypeSampleByIName (final String iName);

  public TypeSample getTypeSampleByName (final String name);


}

