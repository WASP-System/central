
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

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SampleLab;


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
	@Transactional
	public SampleLab getSampleLabBySampleLabId (final int sampleLabId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
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
	@Transactional
	public SampleLab getSampleLabBySampleIdLabId (final int sampleId, final int labId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
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

