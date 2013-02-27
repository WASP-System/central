
/**
 *
 * SampleSourceFileDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSourceFile Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.SampleSourceFile;


public interface SampleSourceFileDao extends WaspDao<SampleSourceFile> {

  public SampleSourceFile getSampleSourceFileBySampleSourceFileId (final int sampleSourceFileId);


}

