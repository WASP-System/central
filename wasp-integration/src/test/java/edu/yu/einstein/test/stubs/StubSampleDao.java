package edu.yu.einstein.test.stubs;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.exception.ModelDetachException;
import edu.yu.einstein.wasp.model.Sample;

@Repository
public class StubSampleDao implements SampleDao {
	
	public Sample sample = new Sample();

	@Override
	public void persist(Sample entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public Sample save(Sample entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Sample entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public Sample merge(Sample entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh(Sample entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public Sample findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sample getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sample flush(Sample entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer removeAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> findByMap(Map m) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> findByMapExcept(Map m) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> findDistinctOrderBy(String distinctColumnName,
			String orderByColumnName, String direction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> findAllOrderBy(String orderByColumnName,
			String direction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> findByMapDistinctOrderBy(Map m,
			List<String> distinctColumnNames, List<String> orderByColumnNames,
			String direction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List findDistinctMetaOrderBy(String metaKeyName, String direction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sample getEagerLoadedDetachedEntity(Sample entity)
			throws ModelDetachException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sample getSampleBySampleId(int sampleId) {
		// Stub Method: return a new Sample object with the sampleId as requested
		sample.setSampleId(sampleId);
		return sample;
	}

	@Override
	public List<Sample> getSamplesByJobId(int jobId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sample getSampleByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> findAllPlatformUnits() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> getActiveSamples() {
		// TODO Auto-generated method stub
		return null;
	}

}
