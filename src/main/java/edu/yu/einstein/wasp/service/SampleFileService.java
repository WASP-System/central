
/**
 *
 * SampleFileService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleFileService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleFileDao;
import edu.yu.einstein.wasp.model.SampleFile;

@Service
public interface SampleFileService extends WaspService<SampleFile> {

	/**
	 * setSampleFileDao(SampleFileDao sampleFileDao)
	 *
	 * @param sampleFileDao
	 *
	 */
	public void setSampleFileDao(SampleFileDao sampleFileDao);

	/**
	 * getSampleFileDao();
	 *
	 * @return sampleFileDao
	 *
	 */
	public SampleFileDao getSampleFileDao();

  public SampleFile getSampleFileBySampleFileId (final int sampleFileId);


}

