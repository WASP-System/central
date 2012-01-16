
/**
 *
 * RolesetDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Roleset Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.Roleset;


public interface RolesetDao extends WaspDao<Roleset> {

  public Roleset getRolesetByRolesetId (final int rolesetId);

  public Roleset getRolesetByParentroleIdChildroleId (final int parentroleId, final int childroleId);


}

