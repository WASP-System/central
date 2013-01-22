
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


@Transactional
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
	@Transactional
	public LabUser getLabUserByLabUserId (final int labUserId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("labUserId", labUserId);

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
	@Transactional
	public LabUser getLabUserByLabIdUserId (final int labId, final int UserId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("labId", labId);
		m.put("UserId", UserId);

		List<LabUser> results = this.findByMap(m);

		if (results.size() == 0) {
			LabUser rt = new LabUser();
			return rt;
		}
		return results.get(0);
	}



}

