
/**
 *
 * LabUserDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the LabUser Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.LabUser;


@Transactional("entityManager")
@Repository
public class LabUserDaoImpl extends WaspDaoImpl<LabUser> implements edu.yu.einstein.wasp.dao.LabUserDao {

	/**
	 * LabUserDaoImpl() Constructor
	 *
	 *
	 */
	public LabUserDaoImpl() {
		super();
		this.entityClass = LabUser.class;
	}


	/**
	 * getLabUserByLabUserId(final int labUserId)
	 *
	 * @param final int labUserId
	 *
	 * @return labUser
	 */

	@Override
	@Transactional("entityManager")
	public LabUser getLabUserByLabUserId (final int labUserId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", labUserId);

		List<LabUser> results = this.findByMap(m);

		if (results.size() == 0) {
			LabUser rt = new LabUser();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getLabUserByLabIdUserId(final int labId, final int UserId)
	 *
	 * @param final int labId, final int UserId
	 *
	 * @return labUser
	 */

	@Override
	@Transactional("entityManager")
	public LabUser getLabUserByLabIdUserId (final int labId, final int userId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("labId", labId);
		m.put("userId", userId);

		List<LabUser> results = this.findByMap(m);

		if (results.size() == 0) {
			LabUser rt = new LabUser();
			return rt;
		}
		return results.get(0);
	}



}

