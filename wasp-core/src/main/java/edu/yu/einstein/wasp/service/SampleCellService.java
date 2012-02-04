
/**
 *
 * SampleCellService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleCellService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleCellDao;
import edu.yu.einstein.wasp.model.SampleCell;

@Service
public interface SampleCellService extends WaspService<SampleCell> {

	/**
	 * setSampleCellDao(SampleCellDao sampleCellDao)
	 *
	 * @param sampleCellDao
	 *
	 */
	public void setSampleCellDao(SampleCellDao sampleCellDao);

	/**
	 * getSampleCellDao();
	 *
	 * @return sampleCellDao
	 *
	 */
	public SampleCellDao getSampleCellDao();

  public SampleCell getSampleCellBySampleCellId (final int sampleCellId);

  public SampleCell getSampleCellByJobcellIdLibraryindex (final int jobcellId, final int libraryindex);


}

