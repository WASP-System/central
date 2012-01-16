
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleFileDao;
import edu.yu.einstein.wasp.model.SampleFile;
import edu.yu.einstein.wasp.service.SampleFileService;

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

