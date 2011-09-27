
/**
 *
 * JobCellService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobCellService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.JobCellDao;
import edu.yu.einstein.wasp.model.JobCell;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface JobCellService extends WaspService<JobCell> {

	/**
	 * setJobCellDao(JobCellDao jobCellDao)
	 *
	 * @param jobCellDao
	 *
	 */
	public void setJobCellDao(JobCellDao jobCellDao);

	/**
	 * getJobCellDao();
	 *
	 * @return jobCellDao
	 *
	 */
	public JobCellDao getJobCellDao();

  public JobCell getJobCellByJobCellId (final int jobCellId);

  public JobCell getJobCellByJobIdCellindex (final int jobId, final int cellindex);


}

