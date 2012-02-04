
/**
 *
 * JobDraftCellDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftCell Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.JobDraftCell;


public interface JobDraftCellDao extends WaspDao<JobDraftCell> {

  public JobDraftCell getJobDraftCellByJobDraftCellId (final Integer jobDraftCellId);

  public JobDraftCell getJobDraftCellByJobdraftIdCellindex (final Integer jobdraftId, final Integer cellindex);


}

