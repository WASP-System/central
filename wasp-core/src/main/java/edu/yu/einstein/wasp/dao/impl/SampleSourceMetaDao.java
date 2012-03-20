
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
import edu.yu.einstein.wasp.model.UserPendingMeta;

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
		for (SampleSourceMeta m:metaList) {
			SampleSourceMeta currentMeta = getSampleSourceMetaByKSampleSourceId(m.getK(), sampleSourceId);
			if (currentMeta.getSampleSourceMetaId() == null){
				// metadata value not in database yet
				m.setSampleSourceId(sampleSourceId);
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


	@Override
	public List<SampleSourceMeta> getSampleSourceMetaBySampleSourceId (final int sampleSourceId) {
		HashMap m = new HashMap();
		m.put("sampleSourceId", sampleSourceId);

		List<SampleSourceMeta> results = this.findByMap(m);

		return results;
	}


}

