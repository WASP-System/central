
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

import edu.yu.einstein.wasp.dao.FileDao;
import edu.yu.einstein.wasp.model.File;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface FileService extends WaspService<File> {

  public void setFileDao(FileDao fileDao);
  public FileDao getFileDao();

  public File getFileByFileId (final int fileId);

}

