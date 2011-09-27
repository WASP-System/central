
/**
 *
 * SampleFileServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleFileService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.SampleFileService;
import edu.yu.einstein.wasp.dao.SampleFileDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.SampleFile;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SampleFileServiceImpl extends WaspServiceImpl<SampleFile> implements SampleFileService {

	/**
	 * sampleFileDao;
	 *
	 */
	private SampleFileDao sampleFileDao;

	/**
	 * setSampleFileDao(SampleFileDao sampleFileDao)
	 *
	 * @param sampleFileDao
	 *
	 */
	@Autowired
	public void setSampleFileDao(SampleFileDao sampleFileDao) {
		this.sampleFileDao = sampleFileDao;
		this.setWaspDao(sampleFileDao);
	}

	/**
	 * getSampleFileDao();
	 *
	 * @return sampleFileDao
	 *
	 */
	public SampleFileDao getSampleFileDao() {
		return this.sampleFileDao;
	}


  public SampleFile getSampleFileBySampleFileId (final int sampleFileId) {
    return this.getSampleFileDao().getSampleFileBySampleFileId(sampleFileId);
  }

}

