package edu.yu.einstein.wasp.test.stubs;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.ModelDetachException;
import edu.yu.einstein.wasp.model.SampleMeta;

@Repository
public class SampleMetaDaoStub implements SampleMetaDao {
	
	// stub specific public attribute
	public List<SampleMeta> stubSampleMetaList;

	@Override
	public void persist(SampleMeta entity) {
		stubSampleMetaList.add(entity);

	}

	@Override
	public SampleMeta save(SampleMeta entity) {
		stubSampleMetaList.add(entity);
		return entity;
	}

	@Override
	public void remove(SampleMeta entity) {
		// Auto-generated method stub

	}

	@Override
	public SampleMeta merge(SampleMeta entity) {
		return entity;
	}

	@Override
	public void refresh(SampleMeta entity) {
		// Auto-generated method stub

	}

	@Override
	public SampleMeta findById(int id) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public SampleMeta getById(int id) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public SampleMeta flush(SampleMeta entity) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<SampleMeta> findAll() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public Integer removeAll() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<SampleMeta> findByMap(Map m) {
		return stubSampleMetaList;
	}

	@Override
	public List<SampleMeta> findByMapExcept(Map m) {
		return stubSampleMetaList;
	}

	@Override
	public List<SampleMeta> findDistinctOrderBy(String distinctColumnName,
			String orderByColumnName, String direction) {
		return stubSampleMetaList;
	}

	@Override
	public List<SampleMeta> findAllOrderBy(String orderByColumnName,
			String direction) {
		return stubSampleMetaList;
	}

	@Override
	public List<SampleMeta> findByMapDistinctOrderBy(Map m,
			List<String> distinctColumnNames, List<String> orderByColumnNames,
			String direction) {
		return stubSampleMetaList;
	}

	@Override
	public List<SampleMeta> findByMapOrderBy(Map m,
			List<String> orderByColumnNames, String direction) {
		return stubSampleMetaList;
	}

	@Override
	public List findDistinctMetaOrderBy(String metaKeyName, String direction) {
		return stubSampleMetaList;
	}

	@Override
	public SampleMeta getEagerLoadedDetachedEntity(SampleMeta entity)
			throws ModelDetachException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public SampleMeta getSampleMetaBySampleMetaId(int sampleMetaId) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public SampleMeta getSampleMetaByKSampleId(String k, int sampleId) {
		// Auto-generated method stub
		return null;
	}



	@Override
	public List<SampleMeta> getSamplesMetaBySampleId(int sampleId) {
		return stubSampleMetaList;
	}

	@Override
	public List<SampleMeta> findByMapsIncludesDatesDistinctOrderBy(Map m,
			Map dateMap, List<String> distinctColumnNames,
			List<String> orderByColumnAndDirectionList) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<SampleMeta> setMeta(List<SampleMeta> metaList, int modelParentId)
			throws MetadataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SampleMeta setMeta(SampleMeta meta) throws MetadataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SampleMeta> getMeta(int modelParentId) {
		// TODO Auto-generated method stub
		return null;
	}

}
