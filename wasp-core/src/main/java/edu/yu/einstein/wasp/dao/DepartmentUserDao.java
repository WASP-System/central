
/**
 *
 * DepartmentUserDao.java 
 * @author echeng (table2type.pl)
 *  
 * the DepartmentUser Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.DepartmentUser;


public interface DepartmentUserDao extends WaspDao<DepartmentUser> {

  public DepartmentUser getDepartmentUserByDepartmentUserId (final int departmentUserId);

  public DepartmentUser getDepartmentUserByDepartmentIdUserId (final int departmentId, final int UserId);


}

