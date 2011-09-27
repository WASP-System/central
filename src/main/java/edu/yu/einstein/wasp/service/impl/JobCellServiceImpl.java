
/**
 *
 * JobCellServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobCellService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.JobCellService;
import edu.yu.einstein.wasp.dao.JobCellDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.JobCell;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobCellServiceImpl extends WaspServiceImpl<JobCell> implements JobCellService {

	/**
	 * jobCellDao;
	 *
	 */
	private JobCellDao jobCellDao;

	/**
	 * setJobCellDao(JobCellDao jobCellDao)
	 *
	 * @param jobCellDao
	 *
	 */
	@Autowired
	public void setJobCellDao(JobCellDao jobCellDao) {
		this.jobCellDao = jobCellDao;
		this.setWaspDao(jobCellDao);
	}

	/**
	 * getJobCellDao();
	 *
	 * @return jobCellDao
	 *
	 */
	public JobCellDao getJobCellDao() {
		return this.jobCellDao;
	}


  public JobCell getJobCellByJobCellId (final int jobCellId) {
    return this.getJobCellDao().getJobCellByJobCellId(jobCellId);
  }

  public JobCell getJobCellByJobIdCellindex (final int jobId, final int cellindex) {
    return this.getJobCellDao().getJobCellByJobIdCellindex(jobId, cellindex);
  }

}

