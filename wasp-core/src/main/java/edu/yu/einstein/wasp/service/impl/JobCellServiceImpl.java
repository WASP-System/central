
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobCellDao;
import edu.yu.einstein.wasp.model.JobCell;
import edu.yu.einstein.wasp.service.JobCellService;

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
	@Override
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
	@Override
	public JobCellDao getJobCellDao() {
		return this.jobCellDao;
	}


  @Override
public JobCell getJobCellByJobCellId (final int jobCellId) {
    return this.getJobCellDao().getJobCellByJobCellId(jobCellId);
  }

  @Override
public JobCell getJobCellByJobIdCellindex (final int jobId, final int cellindex) {
    return this.getJobCellDao().getJobCellByJobIdCellindex(jobId, cellindex);
  }

}

