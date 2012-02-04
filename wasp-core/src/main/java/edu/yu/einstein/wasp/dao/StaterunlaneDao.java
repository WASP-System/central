
/**
 *
 * StaterunlaneDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Staterunlane Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.Staterunlane;


public interface StaterunlaneDao extends WaspDao<Staterunlane> {

  public Staterunlane getStaterunlaneByStaterunlaneId (final int staterunlaneId);


}

