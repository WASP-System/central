
/**
 *
 * UserroleDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Userrole Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Userrole;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class UserroleDaoImpl extends WaspDaoImpl<Userrole> implements edu.yu.einstein.wasp.dao.UserroleDao {

	/**
	 * UserroleDaoImpl() Constructor
	 *
	 *
	 */
	public UserroleDaoImpl() {
		super();
		this.entityClass = Userrole.class;
	}


	/**
	 * getUserroleByUserroleId(final int userroleId)
	 *
	 * @param final int userroleId
	 *
	 * @return userrole
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Userrole getUserroleByUserroleId (final int userroleId) {
    		HashMap m = new HashMap();
		m.put("userroleId", userroleId);

		List<Userrole> results = (List<Userrole>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Userrole rt = new Userrole();
			return rt;
		}
		return (Userrole) results.get(0);
	}



	/**
	 * getUserroleByUserIdRoleId(final int UserId, final int roleId)
	 *
	 * @param final int UserId, final int roleId
	 *
	 * @return userrole
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Userrole getUserroleByUserIdRoleId (final int UserId, final int roleId) {
    		HashMap m = new HashMap();
		m.put("UserId", UserId);
		m.put("roleId", roleId);

		List<Userrole> results = (List<Userrole>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Userrole rt = new Userrole();
			return rt;
		}
		return (Userrole) results.get(0);
	}



}

