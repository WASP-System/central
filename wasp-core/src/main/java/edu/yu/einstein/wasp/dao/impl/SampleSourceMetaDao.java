
/**
 *
 * SampleSourceMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSourceMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SampleSourceMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleSourceMetaDao extends WaspDaoImpl<SampleSourceMeta> implements edu.yu.einstein.wasp.dao.SampleSourceMetaDao {

	/**
	 * SampleSourceMetaDaoImpl() Constructor
	 *
	 *
	 */
	public SampleSourceMetaDao() {
		super();
		this.entityClass = SampleSourceMeta.class;
	}


	/**
	 * getSampleSourceMetaBySampleSourceMetaId(final int sampleSourceMetaId)
	 *
	 * @param final int sampleSourceMetaId
	 *
	 * @return sampleSourceMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public SampleSourceMeta getSampleSourceMetaBySampleSourceMetaId (final int sampleSourceMetaId) {
    		HashMap m = new HashMap();
		m.put("sampleSourceMetaId", sampleSourceMetaId);

		List<SampleSourceMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleSourceMeta rt = new SampleSourceMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSampleSourceMetaByKSampleSourceId(final String k, final int sampleSourceId)
	 *
	 * @param final String k, final int sampleSourceId
	 *
	 * @return sampleSourceMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public SampleSourceMeta getSampleSourceMetaByKSampleSourceId (final String k, final int sampleSourceId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("sampleSourceId", sampleSourceId);

		List<SampleSourceMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleSourceMeta rt = new SampleSourceMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * updateBySampleSourceId (final string area, final int sampleSourceId, final List<SampleSourceMeta> metaList)
	 *
	 * @param sampleSourceId
	 * @param metaList
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateBySampleSourceId (final String area, final int sampleSourceId, final List<SampleSourceMeta> metaList) {
		entityManager.createNativeQuery("delete from samplemeta where sampleSourceId=:sampleSourceId and k like :area").setParameter("sampleSourceId", sampleSourceId).setParameter("area", area + ".%").executeUpdate();

		for (SampleSourceMeta m:metaList) {
			m.setSampleSourceId(sampleSourceId);
			entityManager.persist(m);
		}
	}


	/**
	 * updateBySampleSourceId (final int sampleSourceId, final List<SampleSourceMeta> metaList)
	 *
	 * @param sampleSourceId
	 * @param metaList
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateBySampleSourceId (final int sampleSourceId, final List<SampleSourceMeta> metaList) {
		entityManager.createNativeQuery("delete from samplemeta where sampleSourceId=:sampleSourceId").setParameter("sampleSourceId", sampleSourceId).executeUpdate();

		for (SampleSourceMeta m:metaList) {
			m.setSampleSourceId(sampleSourceId);
			entityManager.persist(m);
		}
	}


	@Override
	public List<SampleSourceMeta> getSampleSourceMetaBySampleSourceId (final int sampleSourceId) {
		HashMap m = new HashMap();
		m.put("sampleSourceId", sampleSourceId);

		List<SampleSourceMeta> results = this.findByMap(m);

		return results;
	}


}
