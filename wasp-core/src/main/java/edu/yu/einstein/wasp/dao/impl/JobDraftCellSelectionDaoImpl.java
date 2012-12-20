
/**
 *
 * JobDraftCellSelectionDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftCellSelection Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobDraftCellSelection;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class JobDraftCellSelectionDaoImpl extends WaspDaoImpl<JobDraftCellSelection> implements edu.yu.einstein.wasp.dao.JobDraftCellSelectionDao {

	/**
	 * JobDraftCellSelectionDaoImpl() Constructor
	 *
	 *
	 */
	public JobDraftCellSelectionDaoImpl() {
		super();
		this.entityClass = JobDraftCellSelection.class;
	}


	/**
	 * getJobDraftCellByJobDraftCellSelectionId(final Integer jobDraftCellSelectionId)
	 *
	 * @param final Integer jobDraftCellSelectionId
	 *
	 * @return jobDraftCellSelection
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public JobDraftCellSelection getJobDraftCellSelectionByJobDraftCellSelectionId (final Integer jobDraftCellSelectionId) {
    		HashMap m = new HashMap();
		m.put("jobDraftCellSelectionId", jobDraftCellSelectionId);

		List<JobDraftCellSelection> results = this.findByMap(m);

		if (results.size() == 0) {
			JobDraftCellSelection rt = new JobDraftCellSelection();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getJobDraftCellByJobDraftIdCellindex(final Integer jobDraftId, final Integer cellIndex)
	 *
	 * @param final Integer jobDraftId, final Integer cellIndex
	 *
	 * @return jobDraftCellSelection
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public JobDraftCellSelection getJobDraftCellSelectionByJobDraftIdCellIndex (final Integer jobDraftId, final Integer cellIndex) {
    		HashMap m = new HashMap();
		m.put("jobDraftId", jobDraftId);
		m.put("cellIndex", cellIndex);

		List<JobDraftCellSelection> results = this.findByMap(m);

		if (results.size() == 0) {
			JobDraftCellSelection rt = new JobDraftCellSelection();
			return rt;
		}
		return results.get(0);
	}



}

