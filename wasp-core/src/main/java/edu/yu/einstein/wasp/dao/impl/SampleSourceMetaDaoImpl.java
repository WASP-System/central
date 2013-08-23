
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


@Transactional("entityManager")
@Repository
public class SampleSourceMetaDaoImpl extends WaspMetaDaoImpl<SampleSourceMeta> implements edu.yu.einstein.wasp.dao.SampleSourceMetaDao {

	/**
	 * SampleSourceMetaDaoImpl() Constructor
	 *
	 *
	 */
	public SampleSourceMetaDaoImpl() {
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
	@Transactional("entityManager")
	public SampleSourceMeta getSampleSourceMetaBySampleSourceMetaId (final int sampleSourceMetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", sampleSourceMetaId);

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
	@Transactional("entityManager")
	public SampleSourceMeta getSampleSourceMetaByKSampleSourceId (final String k, final int sampleSourceId) {
    	HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("k", k);
		m.put("sampleSourceId", sampleSourceId);

		List<SampleSourceMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleSourceMeta rt = new SampleSourceMeta();
			return rt;
		}
		return results.get(0);
	}


	@Override
	public List<SampleSourceMeta> getSampleSourceMetaBySampleSourceId (final int sampleSourceId) {
		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("sampleSourceId", sampleSourceId);

		List<SampleSourceMeta> results = this.findByMap(m);

		return results;
	}


}

