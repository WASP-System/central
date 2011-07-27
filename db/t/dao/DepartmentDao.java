
/**
 *
 * DepartmentDao.java 
 * @author echeng (table2type.pl)
 *  
 * the DepartmentDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface DepartmentDao extends WaspDao<Department> {

  public Department getDepartmentByDepartmentId (final int departmentId);

  public Department getDepartmentByName (final String name);

}

