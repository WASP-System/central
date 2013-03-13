
/**
 *
 * UserroleDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Userrole Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.Userrole;


public interface UserroleDao extends WaspDao<Userrole> {

  public Userrole getUserroleByUserroleId (final int userroleId);

  public Userrole getUserroleByUserIdRoleId (final int userId, final int roleId);


}

