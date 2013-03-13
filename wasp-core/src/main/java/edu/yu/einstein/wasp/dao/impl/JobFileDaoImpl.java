
/**
 *
 * JobFileDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobFile Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobFile;


@Transactional
@Repository
public class JobFileDaoImpl extends WaspDaoImpl<JobFile> implements edu.yu.einstein.wasp.dao.JobFileDao {

	/**
	 * JobFileDaoImpl() Constructor
	 *
	 *
	 */
	public JobFileDaoImpl() {
		super();
		this.entityClass = JobFile.class;
	}


	/**
	 * getJobFileByJobFileId(final int jobFileId)
	 *
	 * @param final int jobFileId
	 *
	 * @return jobFile
	 */

	@Override
	@Transactional
	public JobFile getJobFileByJobFileId (final int jobFileId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", jobFileId);

		List<JobFile> results = this.findByMap(m);

		if (results.size() == 0) {
			JobFile rt = new JobFile();
			return rt;
		}
		return results.get(0);
	}



}

