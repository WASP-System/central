/**
 * 
 */
package edu.yu.einstein.wasp.grid.file;

import java.net.URL;

import edu.yu.einstein.wasp.exception.LoginNameException;
import edu.yu.einstein.wasp.model.FileHandle;

/**
 * 
 * A FileUrlResolver returns a URL for a WASP file object.
 * 
 * @author calder
 *
 */
public interface FileUrlResolver {
	
	/**
	 * Get a URL to a file.  The implementation of this can grant access to files on the same
	 * or a remote host.  
	 * 
	 * @param file
	 * @return
	 * @throws SecurityException
	 * @throws LoginNameException
	 */
	public URL getURL(FileHandle file) throws SecurityException, LoginNameException;

}
