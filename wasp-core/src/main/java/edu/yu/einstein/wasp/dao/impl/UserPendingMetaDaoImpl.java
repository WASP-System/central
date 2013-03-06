
/**
 *
 * UserPendingMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the UserPendingMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.UserPendingMeta;


@Transactional
@Repository
public class UserPendingMetaDaoImpl extends WaspMetaDaoImpl<UserPendingMeta> implements edu.yu.einstein.wasp.dao.UserPendingMetaDao {

	/**
	 * UserPendingMetaDaoImpl() Constructor
	 *
	 *
	 */
	public UserPendingMetaDaoImpl() {
		super();
		this.entityClass = UserPendingMeta.class;
	}


	/**
	 * getUserPendingMetaByUserPendingMetaId(final int userPendingMetaId)
	 *
	 * @param final int userPendingMetaId
	 *
	 * @return userPendingMeta
	 */

	@Override
	@Transactional
	public UserPendingMeta getUserPendingMetaByUserPendingMetaId (final int userPendingMetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", userPendingMetaId);

		List<UserPendingMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			UserPendingMeta rt = new UserPendingMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getUserPendingMetaByKUserpendingId(final String k, final int userPendingId)
	 *
	 * @param final String k, final int userPendingId
	 *
	 * @return userPendingMeta
	 */

	@Override
	@Transactional
	public UserPendingMeta getUserPendingMetaByKUserpendingId (final String k, final int userPendingId) {
    		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("k", k);
		m.put("userPendingId", userPendingId);

		List<UserPendingMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			UserPendingMeta rt = new UserPendingMeta();
			return rt;
		}
		return results.get(0);
	}

}

