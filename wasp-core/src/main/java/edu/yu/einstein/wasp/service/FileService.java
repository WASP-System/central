
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

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.yu.einstein.wasp.dao.FileDao;
import edu.yu.einstein.wasp.exception.FileUploadException;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftFile;
import edu.yu.einstein.wasp.model.Sample;

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
    public File getFileByFileId (final int fileId);

    /**
     * Get a file based on its location
     * @param filelocation
     * @return
     */
	public File getFileByFilelocation (final String filelocation);

	/**
	 * 
	 * @param jobdraft
	 * @param mpFile
	 * @param destPath
	 * @param description
	 * @return entity-managed file object
	 * @throws FileUploadException
	 */
	public File processUploadedFile(MultipartFile mpFile, String destPath, String description) throws FileUploadException;

	
	/**
	 * links a file object to a specified jobDraft
	 * @param file
	 * @param jobDraft
	 * @return the entity-managed JobDraftFile object created
	 */
	public JobDraftFile linkFileWithJobDraft(File file, JobDraft jobDraft);

	/**
	 * Returns a list of files of specified fileType or an empty list if none
	 * @param fileType
	 * @return
	 */
	public List<File> getFilesByType(FileType fileType);

	
	/**
	 * Returns a list of files of specified fileType for the given library or an empty list if none.
	 * @param fileType
	 * @param library
	 * @return
	 * @throws SampleTypeException
	 */
	public List<File> getFilesForLibraryByType(Sample library, FileType fileType) throws SampleTypeException;

	/**
	 * Returns a list of files for the given library or an empty list if none.
	 * @param library
	 * @return
	 * @throws SampleTypeException
	 */
	public List<File> getFilesForLibrary(Sample library) throws SampleTypeException;

	/**
	 * Returns a Map of files for a given library associated by FileType
	 * @param library
	 * @return
	 * @throws SampleTypeException
	 */
	public Map<FileType, List<File>> getFilesForLibraryMappedToFileType(Sample library) throws SampleTypeException;
	
	public void addFile(File file);
	
	public void setSampleFile(File file, Sample sample);

	public Set<FileType> getFileTypes();
	
	public FileType getFileType(String iname);
	
	public void registerFile(File file) throws FileNotFoundException, GridException;

}

