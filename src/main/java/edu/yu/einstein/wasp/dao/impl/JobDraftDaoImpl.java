
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobDraft;

@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	@Transactional
	public JobDraft getJobDraftByJobDraftId (final Integer jobDraftId) {
    		HashMap m = new HashMap();
		m.put("jobDraftId", jobDraftId);

		List<JobDraft> results = (List<JobDraft>) this.findByMap((Map) m);

		if (results.size() == 0) {
			JobDraft rt = new JobDraft();
			return rt;
		}
		return (JobDraft) results.get(0);
	}



}

