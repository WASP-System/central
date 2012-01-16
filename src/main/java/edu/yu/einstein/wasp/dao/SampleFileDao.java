
/**
 *
 * SampleFileDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleFile Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.SampleFile;


public interface SampleFileDao extends WaspDao<SampleFile> {

  public SampleFile getSampleFileBySampleFileId (final int sampleFileId);


}

