
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

import java.util.ArrayList;
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
				"WHERE ss.sample = :cell and " +
				"(ss.sourceSample.sampleType.iName = 'library' or ss.sourceSample.sampleType.iName = 'facilityLibrary' or ss.sourceSample.sampleType.iName = 'virtualLibrary')";
		TypedQuery<SampleSource> ssq = this.entityManager
				.createQuery(query, SampleSource.class)
				.setParameter("cell", cell);
		return ssq.getResultList();
	}
	
	@Override
	public List<SampleSource> getCellLibrariesForLibrary(Sample library) {
		// tried to combine these into one statement with "WHERE ss.sourceSample = :library and (ss.sample is null or ss.sample.sampleType.iName = 'cell')"
		// but this failed to handle the nulls (probably because, unlike java, it attempts to evaluate the ss.sample.sampleType.iName = 'cell' on the null object
		// even when the first statement (ss.sample is null) is false. asmclellan
		List<SampleSource> results = new ArrayList<>();
		String query = "SELECT DISTINCT ss from SampleSource as ss " +
				"WHERE ss.sourceSample = :library and ss.sample is null";
		TypedQuery<SampleSource> ssq = this.entityManager
				.createQuery(query, SampleSource.class)
				.setParameter("library", library);
		results.addAll(ssq.getResultList());
		
		query = "SELECT DISTINCT ss from SampleSource as ss " +
				"WHERE ss.sourceSample = :library and ss.sample.sampleType.iName = 'cell'";
		ssq = this.entityManager
				.createQuery(query, SampleSource.class)
				.setParameter("library", library);
		results.addAll(ssq.getResultList());
		return results;
	}

}

