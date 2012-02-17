/**
 *
 * JobDraftServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.service.JobDraftService;

@Service
public class JobDraftServiceImpl extends WaspServiceImpl<JobDraft> implements JobDraftService {

	/**
	 * jobDraftDao;
	 * 
	 */
	private JobDraftDao	jobDraftDao;

	/**
	 * setJobDraftDao(JobDraftDao jobDraftDao)
	 * 
	 * @param jobDraftDao
	 * 
	 */
	@Override
	@Autowired
	public void setJobDraftDao(JobDraftDao jobDraftDao) {
		this.jobDraftDao = jobDraftDao;
		this.setWaspDao(jobDraftDao);
	}

	/**
	 * getJobDraftDao();
	 * 
	 * @return jobDraftDao
	 * 
	 */
	@Override
	public JobDraftDao getJobDraftDao() {
		return this.jobDraftDao;
	}

	@Override
	public JobDraft getJobDraftByJobDraftId(final Integer jobDraftId) {
		return this.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JobDraft> getPendingJobDrafts() {
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("status", "PENDING");
		return this.findByMap(queryMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JobDraft> getPendingJobDraftsOrderBy(String orderByColumnName, String direction) {
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("status", "PENDING");
		List <String> orderByColumnNames = new ArrayList<String> ();
		orderByColumnNames.add(orderByColumnName);
		return this.findByMapDistinctOrderBy(queryMap, null, orderByColumnNames, direction);
	}
}
