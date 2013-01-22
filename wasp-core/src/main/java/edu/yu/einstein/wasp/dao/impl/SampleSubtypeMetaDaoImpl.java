
/**
 *
 * SampleSubtypeMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSubtypeMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SampleSubtypeMeta;


@Transactional
@Repository
public class SampleSubtypeMetaDaoImpl extends WaspDaoImpl<SampleSubtypeMeta> implements edu.yu.einstein.wasp.dao.SampleSubtypeMetaDao {

	/**
	 * SampleSubtypeMetaDaoImpl() Constructor
	 *
	 *
	 */
	public SampleSubtypeMetaDaoImpl() {
		super();
		this.entityClass = SampleSubtypeMeta.class;
	}


	/**
	 * getSampleSubtypeMetaBySampleSubtypeMetaId(final int sampleSubtypeMetaId)
	 *
	 * @param final int sampleSubtypeMetaId
	 *
	 * @return sampleSubtypeMeta
	 */

	@Override
	@Transactional
	public SampleSubtypeMeta getSampleSubtypeMetaBySampleSubtypeMetaId (final int sampleSubtypeMetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("sampleSubtypeMetaId", sampleSubtypeMetaId);

		List<SampleSubtypeMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleSubtypeMeta rt = new SampleSubtypeMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSampleSubtypeMetaByKSampleSubtypeId(final String k, final int sampleSubtypeId)
	 *
	 * @param final String k, final int sampleSubtypeId
	 *
	 * @return sampleSubtypeMeta
	 */

	@Override
	@Transactional
	public SampleSubtypeMeta getSampleSubtypeMetaByKSampleSubtypeId (final String k, final int sampleSubtypeId) {
    		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("k", k);
		m.put("sampleSubtypeId", sampleSubtypeId);

		List<SampleSubtypeMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleSubtypeMeta rt = new SampleSubtypeMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * updateBySampleSubtypeId (final int sampleSubtypeId, final List<SampleSubtypeMeta> metaList)
	 *
	 * @param sampleSubtypeId
	 * @param metaList
	 *
	 */
	@Override
	@Transactional
	public void updateBySampleSubtypeId (final int sampleSubtypeId, final List<SampleSubtypeMeta> metaList) {
		for (SampleSubtypeMeta m:metaList) {
			SampleSubtypeMeta currentMeta = getSampleSubtypeMetaByKSampleSubtypeId(m.getK(), sampleSubtypeId);
			if (currentMeta.getSampleSubtypeMetaId() == null){
				// metadata value not in database yet
				m.setSampleSubtypeId(sampleSubtypeId);
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
	public List<SampleSubtypeMeta> getSampleSubtypesMetaBySampleSubtypeId (final int sampleSubtypeId) {
		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("sampleSubtypeId", sampleSubtypeId);

		List<SampleSubtypeMeta> results = this.findByMap(m);

		return results;
	}


}

