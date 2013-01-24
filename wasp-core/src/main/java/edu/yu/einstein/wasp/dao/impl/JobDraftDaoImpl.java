
/**
 *
 * JobDraftDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraft Dao Impl
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

import edu.yu.einstein.wasp.model.JobDraft;


@Transactional
@Repository
public class JobDraftDaoImpl extends WaspDaoImpl<JobDraft> implements edu.yu.einstein.wasp.dao.JobDraftDao {

	/**
	 * JobDraftDaoImpl() Constructor
	 *
	 *
	 */
	public JobDraftDaoImpl() {
		super();
		this.entityClass = JobDraft.class;
	}


	/**
	 * getJobDraftByJobDraftId(final Integer jobDraftId)
	 *
	 * @param final Integer jobDraftId
	 *
	 * @return jobDraft
	 */

	@Override
	@Transactional
	public JobDraft getJobDraftByJobDraftId (final Integer jobDraftId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("jobDraftId", jobDraftId);

		List<JobDraft> results = this.findByMap(m);

		if (results.size() == 0) {
			JobDraft rt = new JobDraft();
			return rt;
		}
		return results.get(0);
	}

	@Override
	public List<JobDraft> getPendingJobDrafts() {
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("status", "PENDING");
		return this.findByMap(queryMap);
	}

	@Override
	public List<JobDraft> getPendingJobDraftsOrderBy(String orderByColumnName, String direction) {
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("status", "PENDING");
		List <String> orderByColumnNames = new ArrayList<String> ();
		orderByColumnNames.add(orderByColumnName);
		return this.findByMapDistinctOrderBy(queryMap, null, orderByColumnNames, direction);
	}

}

