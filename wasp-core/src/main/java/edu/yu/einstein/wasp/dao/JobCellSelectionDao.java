
/**
 *
 * JobCellSelectionDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobCellSelection Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.JobCellSelection;


public interface JobCellSelectionDao extends WaspDao<JobCellSelection> {

  public JobCellSelection getJobCellSelectionByJobCellSelectionId (final int jobCellSelectionId);

  public JobCellSelection getJobCellSelectionByJobIdCellIndex (final int jobId, final int cellIndex);


}

