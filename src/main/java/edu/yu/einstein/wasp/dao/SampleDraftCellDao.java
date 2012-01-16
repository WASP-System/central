
/**
 *
 * SampleDraftCellDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraftCell Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.SampleDraftCell;


public interface SampleDraftCellDao extends WaspDao<SampleDraftCell> {

  public SampleDraftCell getSampleDraftCellBySampleDraftCellId (final int sampleDraftCellId);

  public SampleDraftCell getSampleDraftCellByJobdraftcellIdLibraryindex (final int jobdraftcellId, final int libraryindex);


}

