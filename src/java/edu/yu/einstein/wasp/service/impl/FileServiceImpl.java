
/**
 *
 * FileService.java 
 * @author echeng (table2type.pl)
 *  
 * the FileService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.dao.FileDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.File;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FileServiceImpl extends WaspServiceImpl<File> implements FileService {

  private FileDao fileDao;
  @Autowired
  public void setFileDao(FileDao fileDao) {
    this.fileDao = fileDao;
    this.setWaspDao(fileDao);
  }
  public FileDao getFileDao() {
    return this.fileDao;
  }

  // **

  
  public File getFileByFileId (final int fileId) {
    return this.getFileDao().getFileByFileId(fileId);
  }
}

