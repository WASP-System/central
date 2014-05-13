
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

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;


@Transactional("entityManager")
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

	@Override
	@Transactional("entityManager")
	public SampleSource getSampleSourceBySampleSourceId (final int sampleSourceId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", sampleSourceId);

		List<SampleSource> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleSource rt = new SampleSource();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSampleSourceBySampleIdMultiplexindex(final int sampleId, final int multiplexindex)
	 *
	 * @param final int sampleId, final int multiplexindex
	 *
	 * @return sampleSource
	 */

	@Override
	@Transactional("entityManager")
	public SampleSource getSampleSourceBySampleIdMultiplexindex (final int sampleId, final int multiplexindex) {
    	HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("sampleId", sampleId);
		m.put("multiplexindex", multiplexindex);

		List<SampleSource> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleSource rt = new SampleSource();
			return rt;
		}
		return results.get(0);
	}


	@Override
	public List<SampleSource> getCellLibrariesForCell(Sample cell) {
		String query = "SELECT DISTINCT ss from SampleSource as ss " +
				"JOIN ss.sample as cell " +
				"WHERE cell = :cell and " +
				"(ss.sourceSample.sampleType.iName = 'library' or ss.sourceSample.sampleType.iName = 'facilityLibrary' or ss.sourceSample.sampleType.iName = 'virtualLibrary')";
		TypedQuery<SampleSource> ssq = this.entityManager
				.createQuery(query, SampleSource.class)
				.setParameter("cell", cell);
		return ssq.getResultList();
	}
	
	@Override
	public List<SampleSource> getCellLibrariesForLibrary(Sample library) {
		String query = "SELECT DISTINCT ss from SampleSource as ss " +
				"JOIN ss.sourceSample as lib " +
				"WHERE lib = :library and ss.sample.sampleType.iName = 'cell'";
		TypedQuery<SampleSource> ssq = this.entityManager
				.createQuery(query, SampleSource.class)
				.setParameter("library", library);
		return ssq.getResultList();
	}

}

