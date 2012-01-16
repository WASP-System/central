
/**
 *
 * StatesampleDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Statesample Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.Statesample;


public interface StatesampleDao extends WaspDao<Statesample> {

  public Statesample getStatesampleByStatesampleId (final int statesampleId);


}

