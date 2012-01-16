
/**
 *
 * DepartmentDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Department Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Department;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class DepartmentDaoImpl extends WaspDaoImpl<Department> implements edu.yu.einstein.wasp.dao.DepartmentDao {

	/**
	 * DepartmentDaoImpl() Constructor
	 *
	 *
	 */
	public DepartmentDaoImpl() {
		super();
		this.entityClass = Department.class;
	}


	/**
	 * getDepartmentByDepartmentId(final int departmentId)
	 *
	 * @param final int departmentId
	 *
	 * @return department
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Department getDepartmentByDepartmentId (final int departmentId) {
    		HashMap m = new HashMap();
		m.put("departmentId", departmentId);

		List<Department> results = this.findByMap(m);

		if (results.size() == 0) {
			Department rt = new Department();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getDepartmentByName(final String name)
	 *
	 * @param final String name
	 *
	 * @return department
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Department getDepartmentByName (final String name) {
    		HashMap m = new HashMap();
		m.put("name", name);

		List<Department> results = this.findByMap(m);

		if (results.size() == 0) {
			Department rt = new Department();
			return rt;
		}
		return results.get(0);
	}



}

