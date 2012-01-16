
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
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.UserPendingMeta;

@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
	@Transactional
	public UserPendingMeta getUserPendingMetaByUserPendingMetaId (final int userPendingMetaId) {
    		HashMap m = new HashMap();
		m.put("userPendingMetaId", userPendingMetaId);

		List<UserPendingMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			UserPendingMeta rt = new UserPendingMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getUserPendingMetaByKUserpendingId(final String k, final int userpendingId)
	 *
	 * @param final String k, final int userpendingId
	 *
	 * @return userPendingMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public UserPendingMeta getUserPendingMetaByKUserpendingId (final String k, final int userpendingId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("userpendingId", userpendingId);

		List<UserPendingMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			UserPendingMeta rt = new UserPendingMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * updateByUserpendingId (final string area, final int userpendingId, final List<UserPendingMeta> metaList)
	 *
	 * @param userpendingId
	 * @param metaList
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByUserpendingId (final String area, final int userpendingId, final List<UserPendingMeta> metaList) {
		entityManager.createNativeQuery("delete from userpendingmeta where userpendingId=:userpendingId and k like :area").setParameter("userpendingId", userpendingId).setParameter("area", area + ".%").executeUpdate();

		for (UserPendingMeta m:metaList) {
			m.setUserpendingId(userpendingId);
			entityManager.persist(m);
		}
	}


	/**
	 * updateByUserpendingId (final int userpendingId, final List<UserPendingMeta> metaList)
	 *
	 * @param userpendingId
	 * @param metaList
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByUserpendingId (final int userpendingId, final List<UserPendingMeta> metaList) {
		entityManager.createNativeQuery("delete from userpendingmeta where userpendingId=:userpendingId").setParameter("userpendingId", userpendingId).executeUpdate();

		for (UserPendingMeta m:metaList) {
			m.setUserpendingId(userpendingId);
			entityManager.persist(m);
		}
	}



}

