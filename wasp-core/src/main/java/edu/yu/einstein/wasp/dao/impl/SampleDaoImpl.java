
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


@Transactional("entityManager")
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
	@Transactional("entityManager")
	public Sample getSampleBySampleId (final int sampleId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", sampleId);

		List<Sample> results = this.findByMap(m);

		if (results.size() == 0) {
			Sample rt = new Sample();
			return rt;
		}
		return results.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sample> getSamplesByJobId (final int jobId) {
		   String sql=
			   "SELECT s.id, s.name\n"+
			   "FROM jobsample js\n"+
			   "JOIN sample s ON s.id = js.sampleId\n"+
			   "WHERE jobId = :jobId\n"+
			   "ORDER by s.name";
			  
		   
		   List<Sample> result=new ArrayList<Sample>();
		   
		   List<Object[]> listObj=entityManager.createNativeQuery(sql).setParameter("jobId", jobId).getResultList();
		   
		   for(Object[] o:listObj) {
			   
			   Integer sampleId=(Integer)o[0];					  
			   String name=(String)o[1];
			   
			   Sample sample = new Sample();
			   sample.setId(sampleId);
			   sample.setName(name);
			   
			   result.add(sample);
			   
		   } 
		   return result;
	}


	@Override
	public Sample getSampleByName(String name) {
		HashMap<String, String> m = new HashMap<String, String>();
		m.put("name", name);

		List<Sample> results = this.findByMap(m);

		if (results.size() == 0) {
			Sample rt = new Sample();
			return rt;
		}
		return results.get(0);
	}
	
	@Override
	public List<Sample> findAllPlatformUnits() {
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("sampleType.iName", "platformunit");
//		queryMap.put("sampleType.sampleTypeId", 5);
		return this.findByMap(queryMap);
	}
	
	@Override
	public List<Sample> findAllPlatformUnitsOrderByDescending() {
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("sampleType.iName", "platformunit");
		List<String> orderByColumnNames = new ArrayList<String>();
		orderByColumnNames.add("sampleId");
		String direction = "desc";
		return this.findByMapDistinctOrderBy(queryMap, null, orderByColumnNames, direction);
	}
	
	@Override
	public List<Sample> getActiveSamples() {
		Map<String, Integer> queryMap = new HashMap<String, Integer>();
		queryMap.put("isActive", 1);
		return this.findByMap(queryMap);
	}
}

