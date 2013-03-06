
/**
 *
 * JobCellSelectionDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobCellSelection Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobCellSelection;


@Transactional
@Repository
public class JobCellSelectionDaoImpl extends WaspDaoImpl<JobCellSelection> implements edu.yu.einstein.wasp.dao.JobCellSelectionDao {

	/**
	 * JobCellSelectionDaoImpl() Constructor
	 *
	 *
	 */
	public JobCellSelectionDaoImpl() {
		super();
		this.entityClass = JobCellSelection.class;
	}


	/**
	 * getJobCellByjobCellSelectionId(final int jobCellSelectionId)
	 *
	 * @param final int jobCellSelectionId
	 *
	 * @return jobCellSelection
	 */

	@Override
	@Transactional
	public JobCellSelection getJobCellSelectionByJobCellSelectionId (final int jobCellSelectionId) {
    	HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", jobCellSelectionId);

		List<JobCellSelection> results = this.findByMap(m);

		if (results.size() == 0) {
			JobCellSelection rt = new JobCellSelection();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getJobCellSelectionByJobIdCellIndex(final int jobId, final int cellIndex)
	 *
	 * @param final int jobId, final int cellIndex
	 *
	 * @return jobCellSelection
	 */

	@Override
	@Transactional
	public JobCellSelection getJobCellSelectionByJobIdCellIndex (final int jobId, final int cellIndex) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("jobId", jobId);
		m.put("cellIndex", cellIndex);

		List<JobCellSelection> results = this.findByMap(m);

		if (results.size() == 0) {
			JobCellSelection rt = new JobCellSelection();
			return rt;
		}
		return results.get(0);
	}



}

