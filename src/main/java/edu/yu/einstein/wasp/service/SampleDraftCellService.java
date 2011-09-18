
/**
 *
 * SampleDraftCellService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraftCellService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.SampleDraftCellDao;
import edu.yu.einstein.wasp.model.SampleDraftCell;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface SampleDraftCellService extends WaspService<SampleDraftCell> {

	/**
	 * setSampleDraftCellDao(SampleDraftCellDao sampleDraftCellDao)
	 *
	 * @param sampleDraftCellDao
	 *
	 */
	public void setSampleDraftCellDao(SampleDraftCellDao sampleDraftCellDao);

	/**
	 * getSampleDraftCellDao();
	 *
	 * @return sampleDraftCellDao
	 *
	 */
	public SampleDraftCellDao getSampleDraftCellDao();

  public SampleDraftCell getSampleDraftCellBySampleDraftCellId (final int sampleDraftCellId);

  public SampleDraftCell getSampleDraftCellByJobdraftcellIdLibraryindex (final int jobdraftcellId, final int libraryindex);


}

