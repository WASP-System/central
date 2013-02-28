
/**
 *
 * JobDraftFileDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftFile Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobDraftFile;


@Transactional
@Repository
public class JobDraftFileDaoImpl extends WaspDaoImpl<JobDraftFile> implements edu.yu.einstein.wasp.dao.JobDraftFileDao {

	/**
	 * JobDraftFileDaoImpl() Constructor
	 *
	 *
	 */
	public JobDraftFileDaoImpl() {
		super();
		this.entityClass = JobDraftFile.class;
	}


	/**
	 * getJobDraftFileByJobDraftFileId(final int jobFileId)
	 *
	 * @param final int jobFileId
	 *
	 * @return jobFile
	 */

	@Override
	@Transactional
	public JobDraftFile getJobDraftFileByJobDraftFileId (final int jobFileId) {
    		HashMap<String, Integer>m = new HashMap<String, Integer>();
		m.put("id", jobFileId);

		List<JobDraftFile> results = this.findByMap(m);

		if (results.size() == 0) {
			JobDraftFile rt = new JobDraftFile();
			return rt;
		}
		return results.get(0);
	}



}

