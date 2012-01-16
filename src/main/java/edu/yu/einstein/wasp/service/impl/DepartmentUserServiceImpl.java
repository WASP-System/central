
/**
 *
 * DepartmentUserServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the DepartmentUserService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.DepartmentUserDao;
import edu.yu.einstein.wasp.model.DepartmentUser;
import edu.yu.einstein.wasp.service.DepartmentUserService;

@Service
public class DepartmentUserServiceImpl extends WaspServiceImpl<DepartmentUser> implements DepartmentUserService {

	/**
	 * departmentUserDao;
	 *
	 */
	private DepartmentUserDao departmentUserDao;

	/**
	 * setDepartmentUserDao(DepartmentUserDao departmentUserDao)
	 *
	 * @param departmentUserDao
	 *
	 */
	@Autowired
	public void setDepartmentUserDao(DepartmentUserDao departmentUserDao) {
		this.departmentUserDao = departmentUserDao;
		this.setWaspDao(departmentUserDao);
	}

	/**
	 * getDepartmentUserDao();
	 *
	 * @return departmentUserDao
	 *
	 */
	public DepartmentUserDao getDepartmentUserDao() {
		return this.departmentUserDao;
	}


  public DepartmentUser getDepartmentUserByDepartmentUserId (final int departmentUserId) {
    return this.getDepartmentUserDao().getDepartmentUserByDepartmentUserId(departmentUserId);
  }

  public DepartmentUser getDepartmentUserByDepartmentIdUserId (final int departmentId, final int UserId) {
    return this.getDepartmentUserDao().getDepartmentUserByDepartmentIdUserId(departmentId, UserId);
  }

}

