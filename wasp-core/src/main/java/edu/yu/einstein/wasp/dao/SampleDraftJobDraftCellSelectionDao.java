
/**
 *
 * SampleDraftJobDraftCellSelectionDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraftJobDraftCellSelection Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.SampleDraftJobDraftCellSelection;


public interface SampleDraftJobDraftCellSelectionDao extends WaspDao<SampleDraftJobDraftCellSelection> {

  public SampleDraftJobDraftCellSelection getSampleDraftJobDraftCellSelectionBySampleDraftJobDraftCellSelectionId (final int sampleDraftJobDraftCellSelectionId);

  public SampleDraftJobDraftCellSelection getSampleDraftJobDraftCellSelectionByJobDraftCellSelectionIdLibraryIndex (final int jobDraftCellSelectionId, final int libraryIndex);


}

