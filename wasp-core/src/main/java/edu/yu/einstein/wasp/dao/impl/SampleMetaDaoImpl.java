
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

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleMetaDaoImpl extends WaspMetaDaoImpl<SampleMeta> implements edu.yu.einstein.wasp.dao.SampleMetaDao {

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
	@SuppressWarnings("unchecked")
	@Transactional
	public SampleMeta getSampleMetaBySampleMetaId (final int sampleMetaId) {
    		HashMap m = new HashMap();
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
	@SuppressWarnings("unchecked")
	@Transactional
	public SampleMeta getSampleMetaByKSampleId (final String k, final int sampleId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("sampleId", sampleId);

		List<SampleMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleMeta rt = new SampleMeta();
			return rt;
		}
		return results.get(0);
	}




	@Override
	public List<SampleMeta> getSamplesMetaBySampleId (final int sampleId) {
		HashMap m = new HashMap();
		m.put("sampleId", sampleId);

		List<SampleMeta> results = this.findByMap(m);

		return results;
	}


}

