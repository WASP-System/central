
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
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

	@SuppressWarnings("unchecked")
	@Transactional
	public UserPendingMeta getUserPendingMetaByUserPendingMetaId (final int userPendingMetaId) {
    		HashMap m = new HashMap();
		m.put("userPendingMetaId", userPendingMetaId);

		List<UserPendingMeta> results = (List<UserPendingMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			UserPendingMeta rt = new UserPendingMeta();
			return rt;
		}
		return (UserPendingMeta) results.get(0);
	}



	/**
	 * getUserPendingMetaByKUserpendingId(final String k, final int userpendingId)
	 *
	 * @param final String k, final int userpendingId
	 *
	 * @return userPendingMeta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public UserPendingMeta getUserPendingMetaByKUserpendingId (final String k, final int userpendingId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("userpendingId", userpendingId);

		List<UserPendingMeta> results = (List<UserPendingMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			UserPendingMeta rt = new UserPendingMeta();
			return rt;
		}
		return (UserPendingMeta) results.get(0);
	}



	/**
	 * updateByUserpendingId (final string area, final int userpendingId, final List<UserPendingMeta> metaList)
	 *
	 * @param userpendingId
	 * @param metaList
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByUserpendingId (final String area, final int userpendingId, final List<UserPendingMeta> metaList) {

		getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				em.createNativeQuery("delete from userpendingmeta where userpendingId=:userpendingId and k like :area").setParameter("userpendingId", userpendingId).setParameter("area", area + ".%").executeUpdate();

				for (UserPendingMeta m:metaList) {
					m.setUserpendingId(userpendingId);
					em.persist(m);
				}
        			return null;
			}
		});
	}


	/**
	 * updateByUserpendingId (final int userpendingId, final List<UserPendingMeta> metaList)
	 *
	 * @param userpendingId
	 * @param metaList
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByUserpendingId (final int userpendingId, final List<UserPendingMeta> metaList) {

		getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				em.createNativeQuery("delete from userpendingmeta where userpendingId=:userpendingId").setParameter("userpendingId", userpendingId).executeUpdate();

				for (UserPendingMeta m:metaList) {
					m.setUserpendingId(userpendingId);
					em.persist(m);
				}
        			return null;
			}
		});
	}



}

