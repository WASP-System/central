
/**
 *
 * LabPendingMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the LabPendingMeta Dao Impl
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

import edu.yu.einstein.wasp.model.LabPendingMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class LabPendingMetaDaoImpl extends WaspDaoImpl<LabPendingMeta> implements edu.yu.einstein.wasp.dao.LabPendingMetaDao {

	/**
	 * LabPendingMetaDaoImpl() Constructor
	 *
	 *
	 */
	public LabPendingMetaDaoImpl() {
		super();
		this.entityClass = LabPendingMeta.class;
	}


	/**
	 * getLabPendingMetaByLabPendingMetaId(final int labPendingMetaId)
	 *
	 * @param final int labPendingMetaId
	 *
	 * @return labPendingMeta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public LabPendingMeta getLabPendingMetaByLabPendingMetaId (final int labPendingMetaId) {
    		HashMap m = new HashMap();
		m.put("labPendingMetaId", labPendingMetaId);

		List<LabPendingMeta> results = (List<LabPendingMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			LabPendingMeta rt = new LabPendingMeta();
			return rt;
		}
		return (LabPendingMeta) results.get(0);
	}



	/**
	 * getLabPendingMetaByKLabpendingId(final String k, final int labpendingId)
	 *
	 * @param final String k, final int labpendingId
	 *
	 * @return labPendingMeta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public LabPendingMeta getLabPendingMetaByKLabpendingId (final String k, final int labpendingId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("labpendingId", labpendingId);

		List<LabPendingMeta> results = (List<LabPendingMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			LabPendingMeta rt = new LabPendingMeta();
			return rt;
		}
		return (LabPendingMeta) results.get(0);
	}



	/**
	 * updateByLabpendingId (final string area, final int labpendingId, final List<LabPendingMeta> metaList)
	 *
	 * @param labpendingId
	 * @param metaList
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByLabpendingId (final String area, final int labpendingId, final List<LabPendingMeta> metaList) {

		getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				em.createNativeQuery("delete from labpendingmeta where labpendingId=:labpendingId and k like :area").setParameter("labpendingId", labpendingId).setParameter("area", area + ".%").executeUpdate();

				for (LabPendingMeta m:metaList) {
					m.setLabpendingId(labpendingId);
					em.persist(m);
				}
        			return null;
			}
		});
	}


	/**
	 * updateByLabpendingId (final int labpendingId, final List<LabPendingMeta> metaList)
	 *
	 * @param labpendingId
	 * @param metaList
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByLabpendingId (final int labpendingId, final List<LabPendingMeta> metaList) {

		getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				em.createNativeQuery("delete from labpendingmeta where labpendingId=:labpendingId").setParameter("labpendingId", labpendingId).executeUpdate();

				for (LabPendingMeta m:metaList) {
					m.setLabpendingId(labpendingId);
					em.persist(m);
				}
        			return null;
			}
		});
	}



}

