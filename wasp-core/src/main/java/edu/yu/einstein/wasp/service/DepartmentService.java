
/**
 *
 * DepartmentService.java 
 * @author echeng (table2type.pl)
 *  
 * the DepartmentService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.DepartmentDao;
import edu.yu.einstein.wasp.model.Department;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.LabPending;

@Service
public interface DepartmentService extends WaspService<Department> {

	/**
	 * setDepartmentDao(DepartmentDao departmentDao)
	 *
	 * @param departmentDao
	 *
	 */
	public void setDepartmentDao(DepartmentDao departmentDao);

	/**
	 * getDepartmentDao();
	 *
	 * @return departmentDao
	 *
	 */
	public DepartmentDao getDepartmentDao();

  public Department getDepartmentByDepartmentId (final int departmentId);

  public Department getDepartmentByName (final String name);
  
  public List<Department> getDepartmentsByName (final String name);

  public int getDepartmentAdminPendingTasks();
  public int getDepartmentAdminPendingTasks(List<LabPending> labsPendingDaApprovalList, List<Job> jobsPendingDaApprovalList);

  public List<Department> getActiveDepartments();
}

