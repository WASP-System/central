
/**
 *
 * StateDao.java 
 * @author echeng (table2type.pl)
 *  
 * the State Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.State;


public interface StateDao extends WaspDao<State> {

  public State getStateByStateId (final int stateId);


}

