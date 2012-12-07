/**
 * 
 */
package edu.yu.einstein.wasp.grid.file;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * Interface defining remote file management tasks.  Associated with a single transport service.
 * 
 * GridFileService is for lightweight file movement and testing operations.  Files that are downloaded
 * by the user should be accessed through generating a URL with a {@link FileUrlResolver} and passed off to
 * a host running a "wasp-file" instance. Heavyweight file transfer should be done using a
 * {@link GridFileMoverService}. 
 * 
 * @author calder
 *
 */
public interface GridFileService {
	
	/**
	 * Instructs the GridFileService implementation to transfer a file from a given location to another.  
	 * All authentication should be managed by the GridFileService's configuration.
	 * 
	 * @param localFile original file
	 * @param remoteFile path to new file location
	 * @throws IOException
	 */
	public void put(File localFile, String remoteFile) throws IOException;
	
	/**
	 * Instructs the GridFileService implementation to transfer a file from a given location to another.  
	 * All authentication should be managed by the GridFileService's configuration.
	 * 
	 * @param remoteFile path to new file location
	 * @param localFile original file
	 * @throws IOException
	 */
	public void get(String remoteFile, File localFile) throws IOException;
	
	/**
	 * Test for the existence of a remote file.
	 * 
	 * @param host
	 * @param remoteFile
	 * @return file exists?
	 * @throws IOException
	 */
	public boolean exists(String remoteFile) throws IOException;
	
	/**
	 * Touches a file on a remote host.
	 * 
	 * @param host
	 * @param remoteFile
	 * @throws IOException
	 */
	public void touch(String remoteFile) throws IOException;
	
	/**
	 * delete a remote file
	 * @param host
	 * @param remoteFile
	 * @throws IOException
	 */
	public void delete(String remoteFile) throws IOException;

	/**
	 * Take a string representing a file's remote location in URI or string format and convert it into a file URL
	 * for storage in the WASP local database.  For example, files might be located in ${illumina.data.dir}/file.txt or
	 * the SFTP implementation may report that the file is at "sftp://wasp@remotehost.edu/illumina/file.txt".  The 
	 * result will be a uri with value "file://remotehost.edu/illumina/file.txt".
	 * 
	 * @param file
	 * @return
	 */
	public URI remoteFileRepresentationToLocalURI(String file);

}
