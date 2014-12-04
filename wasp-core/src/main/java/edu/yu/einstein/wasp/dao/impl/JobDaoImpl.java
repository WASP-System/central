
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


@Transactional("entityManager")
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
	@Transactional("entityManager")
	public Job getJobByJobId (final int jobId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", jobId);

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
	@Transactional("entityManager")
	public Job getJobByNameLabId (final String name, final int labId) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("name", name);
		m.put("labId", Integer.toString(labId));

		List<Job> results = this.findByMap(m);

		if (results.size() == 0) {
			Job rt = new Job();
			return rt;
		}
		return results.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer,List<Job>> getJobSamplesByWorkflow(final int workflowId) {
		   String query=
			   "SELECT ws.samplesubtypeId, j.id, j.name\n"+
			   "FROM Job j\n"+
			   "JOIN WorkflowSampleSubtype ws ON ws.workflowId = j.workflowId\n"+
			   "WHERE j.workflowId = :workflowId\n"+
			   "ORDER BY j.updated DESC , j.name ASC\n";
		   
		   Map<Integer,List<Job>> result=new LinkedHashMap<Integer,List<Job>>();
		   
		   List<Object[]> listObj=entityManager.createQuery(query).setParameter("workflowId", workflowId).getResultList();
		   for(Object[] o:listObj) {
			   
			   Integer sampleSubtypeId=(Integer)o[0];
			   Integer jobId=(Integer)o[1];
			   String jobName=(String)o[2];
			  
			 					   
			   
			   List<Job> listByType =result.get(sampleSubtypeId);
			   if (listByType==null) {
				   listByType=new ArrayList<Job>();	
				   result.put(sampleSubtypeId, listByType);
			   }
			   Job job = new Job();
			   job.setId(jobId);
			   job.setName(jobName);
			   
			   listByType.add(job);
			 
			   
		   }
		   return result;
	}
	
	  @Override
	  public List<Job> getActiveJobs(){
		  Map<String, Integer> queryMap = new HashMap<String, Integer>();
		  queryMap.put("isActive", 1);
		  return this.findByMap(queryMap);
	  }

}

