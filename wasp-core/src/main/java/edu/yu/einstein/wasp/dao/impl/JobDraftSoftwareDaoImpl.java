
/**
 *
 * JobDraftSoftwareDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftSoftware Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobDraftSoftware;


@Transactional("entityManager")
@Repository
public class JobDraftSoftwareDaoImpl extends WaspDaoImpl<JobDraftSoftware> implements edu.yu.einstein.wasp.dao.JobDraftSoftwareDao {

	/**
	 * JobDraftSoftwareDaoImpl() Constructor
	 *
	 *
	 */
	public JobDraftSoftwareDaoImpl() {
		super();
		this.entityClass = JobDraftSoftware.class;
	}


	/**
	 * getJobDraftSoftwareByJobDraftSoftwareId(final Integer jobDraftSoftwareId)
	 *
	 * @param final Integer jobDraftSoftwareId
	 *
	 * @return jobDraftSoftware
	 */

	@Override
	@Transactional("entityManager")
	public JobDraftSoftware getJobDraftSoftwareByJobDraftSoftwareId (final Integer jobDraftSoftwareId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", jobDraftSoftwareId);

		List<JobDraftSoftware> results = this.findByMap(m);

		if (results.size() == 0) {
			JobDraftSoftware rt = new JobDraftSoftware();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getJobDraftSoftwareBySoftwareIdJobDraftId(final Integer softwareId, final Integer jobDraftId)
	 *
	 * @param final Integer softwareId, final Integer jobDraftId
	 *
	 * @return jobDraftSoftware
	 */

	@Override
	@Transactional("entityManager")
	public JobDraftSoftware getJobDraftSoftwareBySoftwareIdJobDraftId (final Integer softwareId, final Integer jobDraftId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("softwareId", softwareId);
		m.put("jobDraftId", jobDraftId);

		List<JobDraftSoftware> results = this.findByMap(m);

		if (results.size() == 0) {
			JobDraftSoftware rt = new JobDraftSoftware();
			return rt;
		}
		return results.get(0);
	}



}

