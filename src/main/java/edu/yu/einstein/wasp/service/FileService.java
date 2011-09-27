
/**
 *
 * FileService.java 
 * @author echeng (table2type.pl)
 *  
 * the FileService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.FileDao;
import edu.yu.einstein.wasp.model.File;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface FileService extends WaspService<File> {

	/**
	 * setFileDao(FileDao fileDao)
	 *
	 * @param fileDao
	 *
	 */
	public void setFileDao(FileDao fileDao);

	/**
	 * getFileDao();
	 *
	 * @return fileDao
	 *
	 */
	public FileDao getFileDao();

  public File getFileByFileId (final int fileId);


}

