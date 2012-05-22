
/**
 *
 * SampleJobCellSelectionDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleJobCellSelection Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.SampleJobCellSelection;


public interface SampleJobCellSelectionDao extends WaspDao<SampleJobCellSelection> {

  public SampleJobCellSelection getSampleJobCellSelectionBySampleJobCellSelectionId (final int sampleJobCellSelectionId);

  public SampleJobCellSelection getSampleJobCellSelectionBySampleJobCellSelectionIdLibraryIndex (final int jobCellSelectionId, final int libraryIndex);


}

