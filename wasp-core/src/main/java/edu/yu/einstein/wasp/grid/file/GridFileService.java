/**
 * 
 */
package edu.yu.einstein.wasp.grid.file;

import java.io.File;
import java.io.IOException;

import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridTransportService;

/**
 * Interface defining remote file management tasks.  Associated with a single transport service.
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

}
