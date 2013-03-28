
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
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.yu.einstein.wasp.dao.FileHandleDao;
import edu.yu.einstein.wasp.exception.FileUploadException;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftFile;
import edu.yu.einstein.wasp.model.Sample;

@Service
public interface FileService extends WaspService {

	/**
	 * setFileHandleDao(FileHandleDao fileDao)
	 *
	 * @param fileDao
	 *
	 */
	public void setFileHandleDao(FileHandleDao fileDao);

	/**
	 * getFileDao();
	 *
	 * @return fileDao
	 *
	 */
	public FileHandleDao getFileHandleDao();

	/**
	 * Return a file object with specified file id
	 * @param fileId
	 * @return
	 */
    public FileHandle getFileHandleById (final int id);
    
    /**
     * Return a file group
     * @param id
     * @return
     */
    public FileGroup getFileGroupById (final int id);

	/**
	 * 
	 * @param jobdraft
	 * @param mpFile
	 * @param destPath
	 * @param description
	 * @return entity-managed file object
	 * @throws FileUploadException
	 */
	public FileGroup processUploadedFile(MultipartFile mpFile, JobDraft jobDraft, String description);
	
	public FileGroup promoteJobDraftFileGroupToJob(Job job, FileGroup filegroup) throws GridUnresolvableHostException, IOException;

	
	/**
	 * links a file object to a specified jobDraft
	 * @param file
	 * @param jobDraft
	 * @return the entity-managed JobDraftFile object created
	 */
	public JobDraftFile linkFileGroupWithJobDraft(FileGroup file, JobDraft jobDraft);

	/**
	 * Returns a list of files of specified fileType or an empty list if none
	 * @param fileType
	 * @return
	 */
	public Set<FileGroup> getFilesByType(FileType fileType);

	
	/**
	 * Returns a list of files of specified fileType for the given library or an empty list if none.
	 * @param fileType
	 * @param library
	 * @return
	 * @throws SampleTypeException
	 */
	public Set<FileGroup> getFilesForLibraryByType(Sample library, FileType fileType) throws SampleTypeException;

	/**
	 * Returns a list of files for the given library or an empty list if none.
	 * @param library
	 * @return
	 * @throws SampleTypeException
	 */
	public Set<FileGroup> getFilesForLibrary(Sample library) throws SampleTypeException;

	/**
	 * Returns a Map of files for a given library associated by FileType
	 * @param library
	 * @return
	 * @throws SampleTypeException
	 */
	public Map<FileType, Set<FileGroup>> getFilesForLibraryMappedToFileType(Sample library) throws SampleTypeException;
	
	public void addFile(FileHandle file);
	
	public void addFileGroup(FileGroup group);
	
	public void setSampleFile(FileGroup file, Sample sample);

	public Set<FileType> getFileTypes();
	
	public FileType getFileType(String iname);
	
	public void register(FileHandle file) throws FileNotFoundException, GridException;
	
	public void register(FileGroup group) throws FileNotFoundException, GridException;
	
	public FileHandle getFileHandle(UUID uuid) throws FileNotFoundException;

	public List<Map> getFileDetailsByFileType(FileGroup filegroup);

}

