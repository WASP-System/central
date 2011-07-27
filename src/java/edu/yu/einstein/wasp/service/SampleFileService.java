
/**
 *
 * SampleFileService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleFileService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.SampleFileDao;
import edu.yu.einstein.wasp.model.SampleFile;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface SampleFileService extends WaspService<SampleFile> {

  public void setSampleFileDao(SampleFileDao sampleFileDao);
  public SampleFileDao getSampleFileDao();

  public SampleFile getSampleFileBySampleFileId (final int sampleFileId);

}

