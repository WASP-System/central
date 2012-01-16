
/**
 *
 * JobDraftCellServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftCellService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobDraftCellDao;
import edu.yu.einstein.wasp.model.JobDraftCell;
import edu.yu.einstein.wasp.service.JobDraftCellService;

@Service
public class JobDraftCellServiceImpl extends WaspServiceImpl<JobDraftCell> implements JobDraftCellService {

	/**
	 * jobDraftCellDao;
	 *
	 */
	private JobDraftCellDao jobDraftCellDao;

	/**
	 * setJobDraftCellDao(JobDraftCellDao jobDraftCellDao)
	 *
	 * @param jobDraftCellDao
	 *
	 */
	@Override
	@Autowired
	public void setJobDraftCellDao(JobDraftCellDao jobDraftCellDao) {
		this.jobDraftCellDao = jobDraftCellDao;
		this.setWaspDao(jobDraftCellDao);
	}

	/**
	 * getJobDraftCellDao();
	 *
	 * @return jobDraftCellDao
	 *
	 */
	@Override
	public JobDraftCellDao getJobDraftCellDao() {
		return this.jobDraftCellDao;
	}


  @Override
public JobDraftCell getJobDraftCellByJobDraftCellId (final int jobDraftCellId) {
    return this.getJobDraftCellDao().getJobDraftCellByJobDraftCellId(jobDraftCellId);
  }

  @Override
public JobDraftCell getJobDraftCellByJobdraftIdCellindex (final int jobdraftId, final int cellindex) {
    return this.getJobDraftCellDao().getJobDraftCellByJobdraftIdCellindex(jobdraftId, cellindex);
  }

}

