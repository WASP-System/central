
/**
 *
 * SampleDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Sample Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Sample;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleDaoImpl extends WaspDaoImpl<Sample> implements edu.yu.einstein.wasp.dao.SampleDao {

	/**
	 * SampleDaoImpl() Constructor
	 *
	 *
	 */
	public SampleDaoImpl() {
		super();
		this.entityClass = Sample.class;
	}


	/**
	 * getSampleBySampleId(final int sampleId)
	 *
	 * @param final int sampleId
	 *
	 * @return sample
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Sample getSampleBySampleId (final int sampleId) {
    		HashMap m = new HashMap();
		m.put("sampleId", sampleId);

		List<Sample> results = this.findByMap(m);

		if (results.size() == 0) {
			Sample rt = new Sample();
			return rt;
		}
		return results.get(0);
	}

	@Override
	public List<Sample> getSamplesByJobId (final int jobId) {
		   String sql=
			   "SELECT s.sampleId, s.name\n"+
			   "FROM jobsample js\n"+
			   "JOIN sample s ON s.sampleId = js.sampleId\n"+
			   "WHERE jobId = :jobId\n"+
			   "ORDER by s.name";
			  
		   
		   List<Sample> result=new ArrayList<Sample>();
		   
		   List<Object[]> listObj=entityManager.createNativeQuery(sql).setParameter("jobId", jobId).getResultList();
		   
		   for(Object[] o:listObj) {
			   
			   Integer sampleId=(Integer)o[0];					  
			   String name=(String)o[1];
			   
			   Sample sample = new Sample();
			   sample.setSampleId(sampleId);
			   sample.setName(name);
			   
			   result.add(sample);
			   
		   } 
		   return result;
	}


	@Override
	public Sample getSampleByName(String name) {
		HashMap m = new HashMap();
		m.put("name", name);

		List<Sample> results = this.findByMap(m);

		if (results.size() == 0) {
			Sample rt = new Sample();
			return rt;
		}
		return results.get(0);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Sample> findAllPlatformUnits() {
		Map queryMap = new HashMap();
		queryMap.put("typeSample.iName", "platformunit");
//		queryMap.put("typeSample.typeSampleId", 5);
		return this.findByMap(queryMap);
	}
	
	@Override
	public List<Sample> getActiveSamples() {
		Map queryMap = new HashMap();
		queryMap.put("isActive", 1);
		return this.findByMap(queryMap);
	}
}

