
/**
 *
 * FileHandleDao.java 
 * @author echeng (table2type.pl)
 *  
 * the FileHandle Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.FileHandle;


public interface FileHandleDao extends WaspDao<FileHandle> {

  public FileHandle getFileHandleById (final Integer id);

}

