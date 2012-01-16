
/**
 *
 * WorkflowtaskDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowtask Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.Workflowtask;


public interface WorkflowtaskDao extends WaspDao<Workflowtask> {

  public Workflowtask getWorkflowtaskByWorkflowtaskId (final int workflowtaskId);


}

