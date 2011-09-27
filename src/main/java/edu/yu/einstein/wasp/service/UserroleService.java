
/**
 *
 * UserroleService.java 
 * @author echeng (table2type.pl)
 *  
 * the UserroleService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.UserroleDao;
import edu.yu.einstein.wasp.model.Userrole;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface UserroleService extends WaspService<Userrole> {

	/**
	 * setUserroleDao(UserroleDao userroleDao)
	 *
	 * @param userroleDao
	 *
	 */
	public void setUserroleDao(UserroleDao userroleDao);

	/**
	 * getUserroleDao();
	 *
	 * @return userroleDao
	 *
	 */
	public UserroleDao getUserroleDao();

  public Userrole getUserroleByUserroleId (final int userroleId);

  public Userrole getUserroleByUserIdRoleId (final int UserId, final int roleId);


}

