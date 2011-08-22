
/**
 *
 * DepartmentUserService.java 
 * @author echeng (table2type.pl)
 *  
 * the DepartmentUserService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.DepartmentUserDao;
import edu.yu.einstein.wasp.model.DepartmentUser;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface DepartmentUserService extends WaspService<DepartmentUser> {

  public void setDepartmentUserDao(DepartmentUserDao departmentUserDao);
  public DepartmentUserDao getDepartmentUserDao();

  public DepartmentUser getDepartmentUserByDepartmentUserId (final int departmentUserId);

  public DepartmentUser getDepartmentUserByDepartmentIdUserId (final int departmentId, final int UserId);

}

