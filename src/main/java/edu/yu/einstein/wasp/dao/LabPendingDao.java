
/**
 *
 * LabPendingDao.java 
 * @author echeng (table2type.pl)
 *  
 * the LabPending Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.LabPending;


public interface LabPendingDao extends WaspDao<LabPending> {

  public LabPending getLabPendingByLabPendingId (final int labPendingId);


}

