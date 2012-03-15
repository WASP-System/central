
/**
 *
 * JobDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Job Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Job;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class JobDaoImpl extends WaspDaoImpl<Job> implements edu.yu.einstein.wasp.dao.JobDao {

	
	/**
	 * JobDaoImpl() Constructor
	 *
	 *
	 */
	public JobDaoImpl() {
		super();
		this.entityClass = Job.class;
	}


	/**
	 * getJobByJobId(final int jobId)
	 *
	 * @param final int jobId
	 *
	 * @return job
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Job getJobByJobId (final int jobId) {
    		HashMap m = new HashMap();
		m.put("jobId", jobId);

		List<Job> results = this.findByMap(m);

		if (results.size() == 0) {
			Job rt = new Job();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getJobByNameLabId(final String name, final int labId)
	 *
	 * @param final String name, final int labId
	 *
	 * @return job
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Job getJobByNameLabId (final String name, final int labId) {
    		HashMap m = new HashMap();
		m.put("name", name);
		m.put("labId", labId);

		List<Job> results = this.findByMap(m);

		if (results.size() == 0) {
			Job rt = new Job();
			return rt;
		}
		return results.get(0);
	}

	@Override
	public Map<Integer,List<Job>> getJobSamplesByWorkflow(final int workflowId) {
		   String sql=
			   "SELECT ws.subtypesampleId, j.jobId, j.name\n"+
			   "FROM job j\n"+
			   "JOIN workflowsubtypesample ws ON ws.workflowId = j.workflowId\n"+
			   "WHERE j.workflowId = :workflowId\n"+
			   "ORDER BY j.lastupdts DESC , j.name ASC\n";
		   
		   Map<Integer,List<Job>> result=new LinkedHashMap<Integer,List<Job>>();
		   
		   List<Object[]> listObj=entityManager.createNativeQuery(sql).setParameter("workflowId", workflowId).getResultList();
		   for(Object[] o:listObj) {
			   
			   Integer subtypeSampleId=(Integer)o[0];
			   Integer jobId=(Integer)o[1];
			   String jobName=(String)o[2];
			  
			 					   
			   
			   List<Job> listByType =result.get(subtypeSampleId);
			   if (listByType==null) {
				   listByType=new ArrayList<Job>();	
				   result.put(subtypeSampleId, listByType);
			   }
			   Job job = new Job();
			   job.setJobId(jobId);
			   job.setName(jobName);
			   
			   listByType.add(job);
			 
			   
		   }
		   return result;
	}
	
	  @Override
	  public List<Job> getActiveJobs(){
		  Map queryMap = new HashMap();
		  queryMap.put("isActive", 1);
		  return this.findByMap(queryMap);
	  }

}

