
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
		TypedQuery<SampleSource> query = this.entityManager.createQuery("select s from SampleSource s where s.sample = :cell order by s.id", SampleSource.class);
		query.setParameter("cell", cell);
		return query.getResultList();
	}
	
	@Override
	public List<SampleSource> getCellLibrariesForLibrary(Sample library) {
		TypedQuery<SampleSource> query = this.entityManager.createQuery("select s from SampleSource s where s.sourceSample = :library order by s.id", SampleSource.class);
		query.setParameter("library", library);
		return query.getResultList();
	}

}

