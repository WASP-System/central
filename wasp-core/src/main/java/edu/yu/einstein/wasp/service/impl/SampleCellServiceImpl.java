
/**
 *
 * SampleCellServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleCellService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleCellDao;
import edu.yu.einstein.wasp.model.SampleCell;
import edu.yu.einstein.wasp.service.SampleCellService;

@Service
public class SampleCellServiceImpl extends WaspServiceImpl<SampleCell> implements SampleCellService {

	/**
	 * sampleCellDao;
	 *
	 */
	private SampleCellDao sampleCellDao;

	/**
	 * setSampleCellDao(SampleCellDao sampleCellDao)
	 *
	 * @param sampleCellDao
	 *
	 */
	@Override
	@Autowired
	public void setSampleCellDao(SampleCellDao sampleCellDao) {
		this.sampleCellDao = sampleCellDao;
		this.setWaspDao(sampleCellDao);
	}

	/**
	 * getSampleCellDao();
	 *
	 * @return sampleCellDao
	 *
	 */
	@Override
	public SampleCellDao getSampleCellDao() {
		return this.sampleCellDao;
	}


  @Override
public SampleCell getSampleCellBySampleCellId (final int sampleCellId) {
    return this.getSampleCellDao().getSampleCellBySampleCellId(sampleCellId);
  }

  @Override
public SampleCell getSampleCellByJobcellIdLibraryindex (final int jobcellId, final int libraryindex) {
    return this.getSampleCellDao().getSampleCellByJobcellIdLibraryindex(jobcellId, libraryindex);
  }

}

