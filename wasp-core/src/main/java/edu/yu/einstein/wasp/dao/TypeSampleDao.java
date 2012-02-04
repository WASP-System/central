
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

import edu.yu.einstein.wasp.model.TypeSample;


public interface TypeSampleDao extends WaspDao<TypeSample> {

  public TypeSample getTypeSampleByTypeSampleId (final int typeSampleId);

  public TypeSample getTypeSampleByIName (final String iName);

  public TypeSample getTypeSampleByName (final String name);


}

