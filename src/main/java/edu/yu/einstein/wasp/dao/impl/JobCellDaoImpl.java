
/**
 *
 * JobCellDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobCell Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobCell;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class JobCellDaoImpl extends WaspDaoImpl<JobCell> implements edu.yu.einstein.wasp.dao.JobCellDao {

	/**
	 * JobCellDaoImpl() Constructor
	 *
	 *
	 */
	public JobCellDaoImpl() {
		super();
		this.entityClass = JobCell.class;
	}


	/**
	 * getJobCellByJobCellId(final int jobCellId)
	 *
	 * @param final int jobCellId
	 *
	 * @return jobCell
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public JobCell getJobCellByJobCellId (final int jobCellId) {
    		HashMap m = new HashMap();
		m.put("jobCellId", jobCellId);

		List<JobCell> results = this.findByMap(m);

		if (results.size() == 0) {
			JobCell rt = new JobCell();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getJobCellByJobIdCellindex(final int jobId, final int cellindex)
	 *
	 * @param final int jobId, final int cellindex
	 *
	 * @return jobCell
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public JobCell getJobCellByJobIdCellindex (final int jobId, final int cellindex) {
    		HashMap m = new HashMap();
		m.put("jobId", jobId);
		m.put("cellindex", cellindex);

		List<JobCell> results = this.findByMap(m);

		if (results.size() == 0) {
			JobCell rt = new JobCell();
			return rt;
		}
		return results.get(0);
	}



}

