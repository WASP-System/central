
/**
 *
 * SampleSourceDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSource Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SampleSource;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleSourceDaoImpl extends WaspDaoImpl<SampleSource> implements edu.yu.einstein.wasp.dao.SampleSourceDao {

	/**
	 * SampleSourceDaoImpl() Constructor
	 *
	 *
	 */
	public SampleSourceDaoImpl() {
		super();
		this.entityClass = SampleSource.class;
	}


	/**
	 * getSampleSourceBySampleSourceId(final int sampleSourceId)
	 *
	 * @param final int sampleSourceId
	 *
	 * @return sampleSource
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public SampleSource getSampleSourceBySampleSourceId (final int sampleSourceId) {
    		HashMap m = new HashMap();
		m.put("sampleSourceId", sampleSourceId);

		List<SampleSource> results = (List<SampleSource>) this.findByMap((Map) m);

		if (results.size() == 0) {
			SampleSource rt = new SampleSource();
			return rt;
		}
		return (SampleSource) results.get(0);
	}



	/**
	 * getSampleSourceBySampleIdMultiplexindex(final int sampleId, final int multiplexindex)
	 *
	 * @param final int sampleId, final int multiplexindex
	 *
	 * @return sampleSource
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public SampleSource getSampleSourceBySampleIdMultiplexindex (final int sampleId, final int multiplexindex) {
    		HashMap m = new HashMap();
		m.put("sampleId", sampleId);
		m.put("multiplexindex", multiplexindex);

		List<SampleSource> results = (List<SampleSource>) this.findByMap((Map) m);

		if (results.size() == 0) {
			SampleSource rt = new SampleSource();
			return rt;
		}
		return (SampleSource) results.get(0);
	}



}

