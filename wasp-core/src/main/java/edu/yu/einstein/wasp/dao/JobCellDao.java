
/**
 *
 * JobCellDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobCell Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.JobCell;


public interface JobCellDao extends WaspDao<JobCell> {

  public JobCell getJobCellByJobCellId (final int jobCellId);

  public JobCell getJobCellByJobIdCellindex (final int jobId, final int cellindex);


}

