
/**
 *
 * JobDraftCellSelectionDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftCellSelection Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.JobDraftCellSelection;


public interface JobDraftCellSelectionDao extends WaspDao<JobDraftCellSelection> {

  public JobDraftCellSelection getJobDraftCellSelectionByJobDraftCellSelectionId (final Integer jobDraftCellSelectionId);

  public JobDraftCellSelection getJobDraftCellSelectionByJobDraftIdCellIndex (final Integer jobDraftId, final Integer cellIndex);


}

