
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
public class UserPendingMetaDaoImpl extends WaspDaoImpl<UserPendingMeta> implements edu.yu.einstein.wasp.dao.UserPendingMetaDao {

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
		m.put("userPendingMetaId", userPendingMetaId);

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


	/**
	 * updateByUserpendingId (final int userPendingId, final List<UserPendingMeta> metaList)
	 *
	 * @param userPendingId
	 * @param metaList
	 *
	 */
	@Override
	@Transactional
	public void updateByUserpendingId (final int userPendingId, final List<UserPendingMeta> metaList) {
		for (UserPendingMeta m:metaList) {
			UserPendingMeta currentMeta = getUserPendingMetaByKUserpendingId(m.getK(), userPendingId);
			if (currentMeta.getUserPendingMetaId() == null){
				// metadata value not in database yet
				m.setUserPendingId(userPendingId);
				entityManager.persist(m);
			} else if (!currentMeta.getV().equals(m.getV())){
				// meta exists already but value has changed
				currentMeta.setV(m.getV());
				entityManager.merge(currentMeta);
			} else{
				// no change to meta so do nothing
			}
		}
	}



}

