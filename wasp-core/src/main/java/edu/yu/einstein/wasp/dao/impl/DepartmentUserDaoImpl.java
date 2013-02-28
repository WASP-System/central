
/**
 *
 * DepartmentUserDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the DepartmentUser Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.DepartmentUser;


@Transactional
@Repository
public class DepartmentUserDaoImpl extends WaspDaoImpl<DepartmentUser> implements edu.yu.einstein.wasp.dao.DepartmentUserDao {

	/**
	 * DepartmentUserDaoImpl() Constructor
	 *
	 *
	 */
	public DepartmentUserDaoImpl() {
		super();
		this.entityClass = DepartmentUser.class;
	}


	/**
	 * getDepartmentUserByDepartmentUserId(final int departmentUserId)
	 *
	 * @param final int departmentUserId
	 *
	 * @return departmentUser
	 */

	@Override
	@Transactional
	public DepartmentUser getDepartmentUserByDepartmentUserId (final int departmentUserId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", departmentUserId);

		List<DepartmentUser> results = this.findByMap(m);

		if (results.size() == 0) {
			DepartmentUser rt = new DepartmentUser();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getDepartmentUserByDepartmentIdUserId(final int departmentId, final int UserId)
	 *
	 * @param final int departmentId, final int UserId
	 *
	 * @return departmentUser
	 */

	@Override
	@Transactional
	public DepartmentUser getDepartmentUserByDepartmentIdUserId (final int departmentId, final int userId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("departmentId", departmentId);
		m.put("userId", userId);

		List<DepartmentUser> results = this.findByMap(m);

		if (results.size() == 0) {
			DepartmentUser rt = new DepartmentUser();
			return rt;
		}
		return results.get(0);
	}



}

