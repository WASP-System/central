
/**
 *
 * DepartmentServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the DepartmentService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.DepartmentService;
import edu.yu.einstein.wasp.dao.DepartmentDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Department;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentServiceImpl extends WaspServiceImpl<Department> implements DepartmentService {

	/**
	 * departmentDao;
	 *
	 */
	private DepartmentDao departmentDao;

	/**
	 * setDepartmentDao(DepartmentDao departmentDao)
	 *
	 * @param departmentDao
	 *
	 */
	@Autowired
	public void setDepartmentDao(DepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
		this.setWaspDao(departmentDao);
	}

	/**
	 * getDepartmentDao();
	 *
	 * @return departmentDao
	 *
	 */
	public DepartmentDao getDepartmentDao() {
		return this.departmentDao;
	}


  public Department getDepartmentByDepartmentId (final int departmentId) {
    return this.getDepartmentDao().getDepartmentByDepartmentId(departmentId);
  }

  public Department getDepartmentByName (final String name) {
    return this.getDepartmentDao().getDepartmentByName(name);
  }

}

