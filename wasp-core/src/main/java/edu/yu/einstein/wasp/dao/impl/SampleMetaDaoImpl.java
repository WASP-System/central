
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

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SampleMeta;


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

	@Override
	@Transactional
	public SampleMeta getSampleMetaBySampleMetaId (final int sampleMetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("sampleMetaId", sampleMetaId);

		List<SampleMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleMeta rt = new SampleMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSampleMetaByKSampleId(final String k, final int sampleId)
	 *
	 * @param final String k, final int sampleId
	 *
	 * @return sampleMeta
	 */

	@Override
	@Transactional
	public SampleMeta getSampleMetaByKSampleId (final String k, final int sampleId) {
    		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("k", k);
		m.put("sampleId", sampleId);

		List<SampleMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleMeta rt = new SampleMeta();
			return rt;
		}
		return results.get(0);
	}




	/**
	 * updateBySampleId (final int sampleId, final List<SampleMeta> metaList)
	 *
	 * @param sampleId
	 * @param metaList
	 *
	 */
	@Override
	@Transactional
	public void updateBySampleId (final int sampleId, final List<SampleMeta> metaList) {
		for (SampleMeta m:metaList) {
			SampleMeta currentMeta = getSampleMetaByKSampleId(m.getK(), sampleId);
			if (currentMeta.getSampleMetaId() == null){
				// metadata value not in database yet
				m.setSampleId(sampleId);
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
	
	/**
	 * updateBySampleId (final int sampleId, final SampleMeta m)
	 *
	 * @param sampleId
	 * @param m
	 *
	 */
	@Override
	@Transactional
	public void updateBySampleId (final int sampleId, final SampleMeta m) {
		SampleMeta currentMeta = getSampleMetaByKSampleId(m.getK(), sampleId);
		if (currentMeta.getSampleMetaId() == null){
			// metadata value not in database yet
			m.setSampleId(sampleId);
			entityManager.persist(m);
		} else if (!currentMeta.getV().equals(m.getV())){
			// meta exists already but value has changed
			currentMeta.setV(m.getV());
			entityManager.merge(currentMeta);
		} else{
			// no change to meta so do nothing
		}
	}


	@Override
	public List<SampleMeta> getSamplesMetaBySampleId (final int sampleId) {
		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("sampleId", sampleId);

		List<SampleMeta> results = this.findByMap(m);

		return results;
	}


}

