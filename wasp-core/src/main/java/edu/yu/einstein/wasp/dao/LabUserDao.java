
/**
 *
 * LabUserDao.java 
 * @author echeng (table2type.pl)
 *  
 * the LabUser Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.LabUser;


public interface LabUserDao extends WaspDao<LabUser> {

  public LabUser getLabUserByLabUserId (final int labUserId);

  public LabUser getLabUserByLabIdUserId (final int labId, final int userId);


}

