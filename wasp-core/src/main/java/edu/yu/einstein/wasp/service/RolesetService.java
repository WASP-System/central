
/**
 *
 * RolesetService.java 
 * @author echeng (table2type.pl)
 *  
 * the RolesetService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.RolesetDao;
import edu.yu.einstein.wasp.model.Roleset;

@Service
public interface RolesetService extends WaspService<Roleset> {

	/**
	 * setRolesetDao(RolesetDao rolesetDao)
	 *
	 * @param rolesetDao
	 *
	 */
	public void setRolesetDao(RolesetDao rolesetDao);

	/**
	 * getRolesetDao();
	 *
	 * @return rolesetDao
	 *
	 */
	public RolesetDao getRolesetDao();

  public Roleset getRolesetByRolesetId (final int rolesetId);

  public Roleset getRolesetByParentroleIdChildroleId (final int parentroleId, final int childroleId);


}

