
/**
 *
 * JobDraftCellService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftCellService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobDraftCellDao;
import edu.yu.einstein.wasp.model.JobDraftCell;

@Service
public interface JobDraftCellService extends WaspService<JobDraftCell> {

	/**
	 * setJobDraftCellDao(JobDraftCellDao jobDraftCellDao)
	 *
	 * @param jobDraftCellDao
	 *
	 */
	public void setJobDraftCellDao(JobDraftCellDao jobDraftCellDao);

	/**
	 * getJobDraftCellDao();
	 *
	 * @return jobDraftCellDao
	 *
	 */
	public JobDraftCellDao getJobDraftCellDao();

  public JobDraftCell getJobDraftCellByJobDraftCellId (final int jobDraftCellId);

  public JobDraftCell getJobDraftCellByJobdraftIdCellindex (final int jobdraftId, final int cellindex);


}

