
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

import edu.yu.einstein.wasp.service.JobDraftCellService;
import edu.yu.einstein.wasp.dao.JobDraftCellDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.JobDraftCell;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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
	public JobDraftCellDao getJobDraftCellDao() {
		return this.jobDraftCellDao;
	}


  public JobDraftCell getJobDraftCellByJobDraftCellId (final Integer jobDraftCellId) {
    return this.getJobDraftCellDao().getJobDraftCellByJobDraftCellId(jobDraftCellId);
  }

  public JobDraftCell getJobDraftCellByJobdraftIdCellindex (final Integer jobdraftId, final Integer cellindex) {
    return this.getJobDraftCellDao().getJobDraftCellByJobdraftIdCellindex(jobdraftId, cellindex);
  }

}

