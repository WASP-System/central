
/**
 *
 * DepartmentUserDao.java 
 * @author echeng (table2type.pl)
 *  
 * the DepartmentUserDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface DepartmentUserDao extends WaspDao<DepartmentUser> {

  public DepartmentUser getDepartmentUserByDepartmentUserId (final int departmentUserId);

  public DepartmentUser getDepartmentUserByDepartmentIdUserId (final int departmentId, final int UserId);

}

