
/**
 *
 * SampleCellDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleCell Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.SampleCell;


public interface SampleCellDao extends WaspDao<SampleCell> {

  public SampleCell getSampleCellBySampleCellId (final int sampleCellId);

  public SampleCell getSampleCellByJobcellIdLibraryindex (final int jobcellId, final int libraryindex);


}

