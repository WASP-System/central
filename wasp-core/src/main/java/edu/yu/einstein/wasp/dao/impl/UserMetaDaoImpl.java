
/**
 *
 * UserMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the UserMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.UserMeta;


@Transactional
@Repository
public class UserMetaDaoImpl extends WaspMetaDaoImpl<UserMeta> implements edu.yu.einstein.wasp.dao.UserMetaDao {

	/**
	 * UserMetaDaoImpl() Constructor
	 *
	 *
	 */
	public UserMetaDaoImpl() {
		super();
		this.entityClass = UserMeta.class;
	}


	/**
	 * getUserMetaByUserMetaId(final int userMetaId)
	 *
	 * @param final int userMetaId
	 *
	 * @return userMeta
	 */

	@Override
	@Transactional
	public UserMeta getUserMetaByUserMetaId (final int userMetaId) {
    	HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("userMetaId", userMetaId);

		List<UserMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			UserMeta rt = new UserMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getUserMetaByKUserId(final String k, final int UserId)
	 *
	 * @param final String k, final int UserId
	 *
	 * @return userMeta
	 */

	@Override
	@Transactional
	public UserMeta getUserMetaByKUserId (final String k, final int UserId) {
    	HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("k", k);
		m.put("UserId", UserId);

		List<UserMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			UserMeta rt = new UserMeta();
			return rt;
		}
		return results.get(0);
	}


}

