
/**
 *
 * StateruncellDao.java 
 * @author echeng (table2type.pl)
 *  
 * the StateRunCell Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.StateRunCell;


public interface StateRunCellDao extends WaspDao<StateRunCell> {

  public StateRunCell getStateRunCellByStateruncellId (final int stateruncellId);


}

