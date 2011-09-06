
/**
 *
 * FileService.java 
 * @author echeng (table2type.pl)
 *  
 * the FileService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.FileDao;
import edu.yu.einstein.wasp.model.File;

@Service
public interface FileService extends WaspService<File> {

  public void setFileDao(FileDao fileDao);
  public FileDao getFileDao();

  public File getFileByFileId (final int fileId);

}

