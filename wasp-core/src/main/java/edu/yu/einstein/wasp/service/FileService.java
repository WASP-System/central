
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

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.FileDao;
import edu.yu.einstein.wasp.model.File;

@Service
public interface FileService extends WaspService {

	/**
	 * setFileDao(FileDao fileDao)
	 *
	 * @param fileDao
	 *
	 */
	void setFileDao(FileDao fileDao);

	/**
	 * getFileDao();
	 *
	 * @return fileDao
	 *
	 */
	FileDao getFileDao();

    File getFileByFileId (final int fileId);

    //returns File object with contentType/md5hash and sizek fields populated.
    File getMetaInformation(String filePath) throws java.io.IOException;

	public File getFileByFilelocation (final String filelocation);

}

