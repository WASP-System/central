
/**
 *
 * RoleDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Role Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Role;


@Transactional
@Repository
public class RoleDaoImpl extends WaspDaoImpl<Role> implements edu.yu.einstein.wasp.dao.RoleDao {

	/**
	 * RoleDaoImpl() Constructor
	 *
	 *
	 */
	public RoleDaoImpl() {
		super();
		this.entityClass = Role.class;
	}


	/**
	 * getRoleByRoleId(final int roleId)
	 *
	 * @param final int roleId
	 *
	 * @return role
	 */

	@Override
	@Transactional
	public Role getRoleByRoleId (final int roleId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", roleId);

		List<Role> results = this.findByMap(m);

		if (results.size() == 0) {
			Role rt = new Role();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getRoleByRoleName(final String roleName)
	 *
	 * @param final String roleName
	 *
	 * @return role
	 */

	@Override
	@Transactional
	public Role getRoleByRoleName (final String roleName) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("roleName", roleName);

		List<Role> results = this.findByMap(m);

		if (results.size() == 0) {
			Role rt = new Role();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getRoleByName(final String name)
	 *
	 * @param final String name
	 *
	 * @return role
	 */

	@Override
	@Transactional
	public Role getRoleByName (final String name) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("name", name);

		List<Role> results = this.findByMap(m);

		if (results.size() == 0) {
			Role rt = new Role();
			return rt;
		}
		return results.get(0);
	}



}

