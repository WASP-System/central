
/**
 *
 * SubtypeSampleDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SubtypeSample Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.SubtypeSample;


public interface SubtypeSampleDao extends WaspDao<SubtypeSample> {

  public SubtypeSample getSubtypeSampleBySubtypeSampleId (final int subtypeSampleId);

  public SubtypeSample getSubtypeSampleByIName (final String iName);


}

