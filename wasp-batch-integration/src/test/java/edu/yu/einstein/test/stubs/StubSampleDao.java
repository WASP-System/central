package edu.yu.einstein.test.stubs;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.exception.ModelDetachException;
import edu.yu.einstein.wasp.model.Sample;

@Repository
public class StubSampleDao implements SampleDao {
	
	public Sample sample = new Sample(); // stub specific attribute. For manipulating a sample object.

	@Override
	public void persist(Sample entity) {
		// Auto-generated method stub

	}

	@Override
	public Sample save(Sample entity) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Sample entity) {
		// Auto-generated method stub

	}

	@Override
	public Sample merge(Sample entity) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void refresh(Sample entity) {
		// Auto-generated method stub

	}

	@Override
	public Sample findById(int id) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public Sample getById(int id) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public Sample flush(Sample entity) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> findAll() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public Integer removeAll() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> findByMap(Map m) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> findByMapExcept(Map m) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> findDistinctOrderBy(String distinctColumnName,
			String orderByColumnName, String direction) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> findAllOrderBy(String orderByColumnName,
			String direction) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> findByMapDistinctOrderBy(Map m,
			List<String> distinctColumnNames, List<String> orderByColumnNames,
			String direction) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List findDistinctMetaOrderBy(String metaKeyName, String direction) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public Sample getEagerLoadedDetachedEntity(Sample entity)
			throws ModelDetachException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public Sample getSampleBySampleId(int sampleId) {
		// Stub-specific implementation: return a new Sample object with the sampleId as requested
		sample.setSampleId(sampleId);
		return sample;
	}

	@Override
	public List<Sample> getSamplesByJobId(int jobId) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public Sample getSampleByName(String name) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> findAllPlatformUnits() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> getActiveSamples() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> findByMapOrderBy(Map m,
			List<String> orderByColumnNames, String direction) {
		// TODO Auto-generated method stub
		return null;
	}

}
