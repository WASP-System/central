
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
import org.springframework.web.multipart.MultipartFile;

import edu.yu.einstein.wasp.dao.FileDao;
import edu.yu.einstein.wasp.exception.FileUploadException;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftFile;

@Service
public interface FileService extends WaspService {

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

	/**
	 * Return a file object with specified file id
	 * @param fileId
	 * @return
	 */
    public FileHandle getFileByFileId (final int fileId);

    /**
     * Get a file based on its location
     * @param filelocation
     * @return
     */
	public FileHandle getFileByFilelocation (final String filelocation);

	/**
	 * 
	 * @param jobdraft
	 * @param mpFile
	 * @param destPath
	 * @param description
	 * @return entity-managed file object
	 * @throws FileUploadException
	 */
	public FileHandle processUploadedFile(MultipartFile mpFile, String destPath, String description) throws FileUploadException;

	
	/**
	 * links a file object to a specified jobDraft
	 * @param file
	 * @param jobDraft
	 * @return the entity-managed JobDraftFile object created
	 */
	public JobDraftFile linkFileWithJobDraft(FileHandle file, JobDraft jobDraft);

}

