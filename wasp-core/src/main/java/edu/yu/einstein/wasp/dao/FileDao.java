
/**
 *
 * FileDao.java 
 * @author echeng (table2type.pl)
 *  
 * the FileHandle Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.FileHandle;


public interface FileDao extends WaspDao<FileHandle> {

  public FileHandle getFileByFileId (final Integer fileId);

  public FileHandle getFileByFilelocation (final String filelocation);


}

