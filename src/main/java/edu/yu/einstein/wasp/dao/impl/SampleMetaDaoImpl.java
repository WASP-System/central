
/**
 *
 * SampleMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleMeta Dao Impl
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

import edu.yu.einstein.wasp.model.SampleMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleMetaDaoImpl extends WaspDaoImpl<SampleMeta> implements edu.yu.einstein.wasp.dao.SampleMetaDao {

	/**
	 * SampleMetaDaoImpl() Constructor
	 *
	 *
	 */
	public SampleMetaDaoImpl() {
		super();
		this.entityClass = SampleMeta.class;
	}


	/**
	 * getSampleMetaBySampleMetaId(final int sampleMetaId)
	 *
	 * @param final int sampleMetaId
	 *
	 * @return sampleMeta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public SampleMeta getSampleMetaBySampleMetaId (final int sampleMetaId) {
    		HashMap m = new HashMap();
		m.put("sampleMetaId", sampleMetaId);

		List<SampleMeta> results = (List<SampleMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			SampleMeta rt = new SampleMeta();
			return rt;
		}
		return (SampleMeta) results.get(0);
	}



	/**
	 * getSampleMetaByKSampleId(final String k, final int sampleId)
	 *
	 * @param final String k, final int sampleId
	 *
	 * @return sampleMeta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public SampleMeta getSampleMetaByKSampleId (final String k, final int sampleId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("sampleId", sampleId);

		List<SampleMeta> results = (List<SampleMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			SampleMeta rt = new SampleMeta();
			return rt;
		}
		return (SampleMeta) results.get(0);
	}
	
	public List<SampleMeta> getSamplesMetaBySampleId (final int sampleId) {
		HashMap m = new HashMap();	
		m.put("sampleId", sampleId);

		List<SampleMeta> results = (List<SampleMeta>) this.findByMap((Map) m);

		return results;
		
	}



	/**
	 * updateBySampleId (final int sampleId, final List<SampleMeta> metaList)
	 *
	 * @param sampleId
	 * @param metaList
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateBySampleId (final int sampleId, final List<SampleMeta> metaList) {

		getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				em.createNativeQuery("delete from samplemeta where sampleId=:sampleId").setParameter("sampleId", sampleId).executeUpdate();

				for (SampleMeta m:metaList) {
					m.setSampleId(sampleId);
					em.persist(m);
				}
        			return null;
			}
		});
	}



}

