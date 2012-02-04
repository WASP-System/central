
/**
 *
 * DepartmentUserService.java 
 * @author echeng (table2type.pl)
 *  
 * the DepartmentUserService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.DepartmentUserDao;
import edu.yu.einstein.wasp.model.DepartmentUser;

@Service
public interface DepartmentUserService extends WaspService<DepartmentUser> {

	/**
	 * setDepartmentUserDao(DepartmentUserDao departmentUserDao)
	 *
	 * @param departmentUserDao
	 *
	 */
	public void setDepartmentUserDao(DepartmentUserDao departmentUserDao);

	/**
	 * getDepartmentUserDao();
	 *
	 * @return departmentUserDao
	 *
	 */
	public DepartmentUserDao getDepartmentUserDao();

  public DepartmentUser getDepartmentUserByDepartmentUserId (final int departmentUserId);

  public DepartmentUser getDepartmentUserByDepartmentIdUserId (final int departmentId, final int UserId);


}

