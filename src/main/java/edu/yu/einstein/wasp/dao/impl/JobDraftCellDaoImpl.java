
/**
 *
 * JobDraftCellDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftCell Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobDraftCell;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class JobDraftCellDaoImpl extends WaspDaoImpl<JobDraftCell> implements edu.yu.einstein.wasp.dao.JobDraftCellDao {

	/**
	 * JobDraftCellDaoImpl() Constructor
	 *
	 *
	 */
	public JobDraftCellDaoImpl() {
		super();
		this.entityClass = JobDraftCell.class;
	}


	/**
	 * getJobDraftCellByJobDraftCellId(final int jobDraftCellId)
	 *
	 * @param final int jobDraftCellId
	 *
	 * @return jobDraftCell
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public JobDraftCell getJobDraftCellByJobDraftCellId (final int jobDraftCellId) {
    		HashMap m = new HashMap();
		m.put("jobDraftCellId", jobDraftCellId);

		List<JobDraftCell> results = this.findByMap(m);

		if (results.size() == 0) {
			JobDraftCell rt = new JobDraftCell();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getJobDraftCellByJobdraftIdCellindex(final int jobdraftId, final int cellindex)
	 *
	 * @param final int jobdraftId, final int cellindex
	 *
	 * @return jobDraftCell
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public JobDraftCell getJobDraftCellByJobdraftIdCellindex (final int jobdraftId, final int cellindex) {
    		HashMap m = new HashMap();
		m.put("jobdraftId", jobdraftId);
		m.put("cellindex", cellindex);

		List<JobDraftCell> results = this.findByMap(m);

		if (results.size() == 0) {
			JobDraftCell rt = new JobDraftCell();
			return rt;
		}
		return results.get(0);
	}



}

