
/**
 *
 * SampleDraftCellServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraftCellService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleDraftCellDao;
import edu.yu.einstein.wasp.model.SampleDraftCell;
import edu.yu.einstein.wasp.service.SampleDraftCellService;

@Service
public class SampleDraftCellServiceImpl extends WaspServiceImpl<SampleDraftCell> implements SampleDraftCellService {

	/**
	 * sampleDraftCellDao;
	 *
	 */
	private SampleDraftCellDao sampleDraftCellDao;

	/**
	 * setSampleDraftCellDao(SampleDraftCellDao sampleDraftCellDao)
	 *
	 * @param sampleDraftCellDao
	 *
	 */
	@Override
	@Autowired
	public void setSampleDraftCellDao(SampleDraftCellDao sampleDraftCellDao) {
		this.sampleDraftCellDao = sampleDraftCellDao;
		this.setWaspDao(sampleDraftCellDao);
	}

	/**
	 * getSampleDraftCellDao();
	 *
	 * @return sampleDraftCellDao
	 *
	 */
	@Override
	public SampleDraftCellDao getSampleDraftCellDao() {
		return this.sampleDraftCellDao;
	}


  @Override
public SampleDraftCell getSampleDraftCellBySampleDraftCellId (final int sampleDraftCellId) {
    return this.getSampleDraftCellDao().getSampleDraftCellBySampleDraftCellId(sampleDraftCellId);
  }

  @Override
public SampleDraftCell getSampleDraftCellByJobdraftcellIdLibraryindex (final int jobdraftcellId, final int libraryindex) {
    return this.getSampleDraftCellDao().getSampleDraftCellByJobdraftcellIdLibraryindex(jobdraftcellId, libraryindex);
  }

}

