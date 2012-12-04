
/**
 *
 * FileDao.java 
 * @author echeng (table2type.pl)
 *  
 * the File Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.File;


public interface FileDao extends WaspDao<File> {

  public File getFileByFileId (final Integer fileId);

  public File getFileByFilelocation (final String filelocation);


}

