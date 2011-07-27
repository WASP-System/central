
/**
 *
 * DepartmentService.java 
 * @author echeng (table2type.pl)
 *  
 * the DepartmentService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.DepartmentDao;
import edu.yu.einstein.wasp.model.Department;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface DepartmentService extends WaspService<Department> {

  public void setDepartmentDao(DepartmentDao departmentDao);
  public DepartmentDao getDepartmentDao();

  public Department getDepartmentByDepartmentId (final int departmentId);

  public Department getDepartmentByName (final String name);

}

