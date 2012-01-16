
/**
 *
 * SampleLabDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleLab Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SampleLab;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleLabDaoImpl extends WaspDaoImpl<SampleLab> implements edu.yu.einstein.wasp.dao.SampleLabDao {

	/**
	 * SampleLabDaoImpl() Constructor
	 *
	 *
	 */
	public SampleLabDaoImpl() {
		super();
		this.entityClass = SampleLab.class;
	}


	/**
	 * getSampleLabBySampleLabId(final int sampleLabId)
	 *
	 * @param final int sampleLabId
	 *
	 * @return sampleLab
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public SampleLab getSampleLabBySampleLabId (final int sampleLabId) {
    		HashMap m = new HashMap();
		m.put("sampleLabId", sampleLabId);

		List<SampleLab> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleLab rt = new SampleLab();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSampleLabBySampleIdLabId(final int sampleId, final int labId)
	 *
	 * @param final int sampleId, final int labId
	 *
	 * @return sampleLab
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public SampleLab getSampleLabBySampleIdLabId (final int sampleId, final int labId) {
    		HashMap m = new HashMap();
		m.put("sampleId", sampleId);
		m.put("labId", labId);

		List<SampleLab> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleLab rt = new SampleLab();
			return rt;
		}
		return results.get(0);
	}



}

