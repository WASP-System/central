
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

import edu.yu.einstein.wasp.service.SampleCellService;
import edu.yu.einstein.wasp.dao.SampleCellDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.SampleCell;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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
	public SampleCellDao getSampleCellDao() {
		return this.sampleCellDao;
	}


  public SampleCell getSampleCellBySampleCellId (final int sampleCellId) {
    return this.getSampleCellDao().getSampleCellBySampleCellId(sampleCellId);
  }

  public SampleCell getSampleCellByJobcellIdLibraryindex (final int jobcellId, final int libraryindex) {
    return this.getSampleCellDao().getSampleCellByJobcellIdLibraryindex(jobcellId, libraryindex);
  }

}

