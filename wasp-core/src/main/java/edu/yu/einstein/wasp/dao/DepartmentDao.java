
/**
 *
 * DepartmentDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Department Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.Department;


public interface DepartmentDao extends WaspDao<Department> {

  public Department getDepartmentByDepartmentId (final int departmentId);

  public Department getDepartmentByName (final String name);

  public List<Department> getActiveDepartments();


}

