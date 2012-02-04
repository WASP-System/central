
/**
 *
 * WorkflowtasksourceDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowtasksource Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.Workflowtasksource;


public interface WorkflowtasksourceDao extends WaspDao<Workflowtasksource> {

  public Workflowtasksource getWorkflowtasksourceByWorkflowtasksourceId (final Integer workflowtasksourceId);


}

