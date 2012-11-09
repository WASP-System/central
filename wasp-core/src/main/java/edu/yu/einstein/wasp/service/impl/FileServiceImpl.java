
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.yu.einstein.wasp.dao.FileDao;
import edu.yu.einstein.wasp.dao.JobDraftFileDao;
import edu.yu.einstein.wasp.exception.FileUploadException;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftFile;
import edu.yu.einstein.wasp.service.FileService;

@Service
@Transactional("entityManager")
public class FileServiceImpl extends WaspServiceImpl implements FileService {


	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private JobDraftFileDao jobDraftFileDao;
	
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
	@Override
	@Autowired
	public void setFileDao(FileDao fileDao) {
		this.fileDao = fileDao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileDao getFileDao() {
		return this.fileDao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getFileByFileId (final int fileId) {
		return this.getFileDao().getFileByFileId(fileId);
	}
  
	  //returns File object with contentType/md5hash and sizek fields populated.
	  @Override
	  public File getMetaInformation(String filePath) throws java.io.IOException {
		  	File file = new File();
		  	file.setContentType(getMimeType(filePath));
		  	file.setMd5hash(getMD5(filePath));
		  	file.setSizek(getFileSizeKb(filePath));
		  	file.setAbsolutePath(filePath);
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
 
	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getFileByFilelocation (final String filelocation) {
		return this.getFileDao().getFileByFilelocation(filelocation);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override 
	public File processUploadedFile(MultipartFile mpFile, String destPath, String description) throws FileUploadException{
		String noSpacesFileName = mpFile.getOriginalFilename().replaceAll("\\s+", "_");
		//String absolutePath = destPath+"/"+mpFile.getOriginalFilename();
		String absolutePath = destPath+"/"+noSpacesFileName;
		java.io.File pathFile = new java.io.File(destPath);
		if (!pathFile.exists()){
			try{
				pathFile.mkdir();
			} catch(Exception e){
				throw new FileUploadException("File upload failure trying to create '"+destPath+"': "+e.getMessage());
			}
		}
						
		String md5Hash = "";
		try {
			md5Hash = DigestUtils.md5Hex(mpFile.getInputStream());
		} catch (IOException e) {
			//logger.warn("Cannot generate MD5 Hash for '"+mpFile.getOriginalFilename()+"': "+ e.getMessage());
			logger.warn("Cannot generate MD5 Hash for '"+noSpacesFileName+"': "+ e.getMessage());
		}
		//String fileName = mpFile.getOriginalFilename();
		String fileName = mpFile.getOriginalFilename().replaceAll("\\s+", "_");
		Integer fileSizeK = (int)((mpFile.getSize()/1024) + 0.5);
		String contentType = mpFile.getContentType();
		logger.debug("Uploading file '"+fileName+"' to '"+absolutePath+"' (type="+contentType+", size="+fileSizeK+"Kb, md5Hash="+md5Hash+")");
		java.io.File newFile = new java.io.File(absolutePath);
		try{
			mpFile.transferTo(newFile);
		} catch(Exception e){
			throw new FileUploadException("File upload failure trying to save '"+absolutePath+"': "+e.getMessage());
		}
		File file = new File();
		file.setDescription(description);
		file.setAbsolutePath(absolutePath);
		file.setIsActive(1);
		file.setContentType(contentType);
		file.setMd5hash(md5Hash);
		file.setSizek(fileSizeK);		
		return fileDao.save(file);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JobDraftFile linkFileWithJobDraft(File file, JobDraft jobDraft){
		JobDraftFile jobDraftFile = new JobDraftFile();
		jobDraftFile.setFile(file);
		jobDraftFile.setJobDraft(jobDraft);
		return jobDraftFileDao.save(jobDraftFile);
	}
	
}

