
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.yu.einstein.wasp.dao.FileDao;
import edu.yu.einstein.wasp.dao.JobDraftFileDao;
import edu.yu.einstein.wasp.exception.FileUploadException;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.model.FileType;
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
  
 
	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getFileByFilelocation (final String filelocation) {
		return this.getFileDao().getFileByFilelocation(filelocation);
	}
	
	/**
	 * TODO: CLEAN UP THIS HORRIBLE TURD
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
		logger.debug("Uploading file '"+fileName+"' to '"+absolutePath+"' (size="+fileSizeK+"Kb, md5Hash="+md5Hash+")");
		java.io.File newFile = new java.io.File(absolutePath);
		try{
			mpFile.transferTo(newFile);
		} catch(Exception e){
			throw new FileUploadException("File upload failure trying to save '"+absolutePath+"': "+e.getMessage());
		}
		File file = new File();
		file.setDescription(description);
		//file.setFileURI(absolutePath);
		file.setIsActive(1);
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

