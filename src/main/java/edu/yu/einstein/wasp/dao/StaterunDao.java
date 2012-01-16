
/**
 *
 * StaterunDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Staterun Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.Staterun;


public interface StaterunDao extends WaspDao<Staterun> {

  public Staterun getStaterunByStaterunId (final int staterunId);


}

