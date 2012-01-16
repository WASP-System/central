
/**
 *
 * FileServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the FileService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.FileDao;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.service.FileService;

@Service
public class FileServiceImpl extends WaspServiceImpl<File> implements FileService {

	@Autowired
	private MessageSource messageSource;
	
	/**
	 * fileDao;
	 *
	 */
	private FileDao fileDao;

	/**
	 * setFileDao(FileDao fileDao)
	 *
	 * @param fileDao
	 *
	 */
	@Autowired
	public void setFileDao(FileDao fileDao) {
		this.fileDao = fileDao;
		this.setWaspDao(fileDao);
	}

	/**
	 * getFileDao();
	 *
	 * @return fileDao
	 *
	 */
	public FileDao getFileDao() {
		return this.fileDao;
	}


  public File getFileByFileId (final int fileId) {
    return this.getFileDao().getFileByFileId(fileId);
  }
  
  //returns File object with contentType/md5hash and sizek fields populated.
  public File getMetaInformation(String filePath) throws java.io.IOException {
	  	File file = new File();
	  	file.setContenttype(getMimeType(filePath));
	  	file.setMd5hash(getMD5(filePath));
	  	file.setSizek(getFileSizeKb(filePath));
	  	file.setFilelocation(filePath);
	  	return file;
  }

  	//Returns mime type based on file extension. If extension is unknown, returns  "content/unknown"
  	//To add new "extension/content type" mapping use /wasp/uiField/list.do admin screen.
    //For example, to map ".pdf" extension to "application/pdf" content type add this property (to English locale):
    //contentTypeMap.pdf.data=application/pdf
	private String getMimeType(String filePath) throws java.io.IOException {
		
		int dot = filePath.lastIndexOf(".");
	    String ext=filePath.substring(dot + 1);
				
	    WaspMessageSourceImpl src=(WaspMessageSourceImpl)messageSource;
	    return src.getUSMessage("contentTypeMap."+ext+".data","content/unknown"); 

	}

	
	private String getMD5(String filePath) throws IOException {
		byte[] data=FileUtils.readFileToByteArray(new java.io.File(filePath));
		return DigestUtils.md5Hex(data);		
	}

	
	private int getFileSizeKb(String filePath) throws IOException {
		java.io.File file = new java.io.File(filePath);
		return (int)file.length()/1024;
	}
	
}

