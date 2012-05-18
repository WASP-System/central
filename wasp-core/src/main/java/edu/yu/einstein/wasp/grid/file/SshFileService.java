package edu.yu.einstein.wasp.grid.file;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;

/**
 * Implementation of {@link GridFileService} that manages file transfer over SFTP using Apache Commons VFS2/jsch.
 * @author calder
 *
 */
public class SshFileService implements GridFileService {

	private final Log logger = LogFactory.getLog(getClass());
	
	private GridHostResolver hostResolver;

	private String hostKeyChecking = "no";
	private static File identityFile;
	private int timeout = 10000; // milliseconds
	private int retries = 6;

	@Override
	public void put(File localFile, String host, String remoteFile)
			throws IOException {
		
		logger.debug("put called: " + localFile + " to " + host + " as " + remoteFile);
		
		if (!localFile.exists())
			throw new RuntimeException("File " + localFile.getAbsolutePath()
					+ " not found");

		StandardFileSystemManager manager = new StandardFileSystemManager();

		try {
			manager.init();

			FileObject file = manager.resolveFile(localFile.getAbsolutePath());
			String remote = getRemoteFileString(host, remoteFile);
			FileObject destination = manager.resolveFile(remote,
					createDefaultOptions(hostKeyChecking, timeout));

			destination.copyFrom(file, Selectors.SELECT_SELF);
			logger.debug(localFile.getAbsolutePath() + " copied to " + remote);

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			manager.close();
		}
	}

	@Override
	public void get(String host, String remoteFile, File localFile)
			throws IOException {

		logger.debug("get called: " + remoteFile + " from " + host + " as " + localFile);
		
		if (!exists(host, remoteFile))
			throw new RuntimeException("File " + remoteFile + "@" + host + " not found");

		StandardFileSystemManager manager = new StandardFileSystemManager();

		try {
			manager.init();

			String remote = getRemoteFileString(host, remoteFile);
			FileObject file = manager.resolveFile(remote,
					createDefaultOptions(hostKeyChecking, timeout));
			FileObject destination = manager.resolveFile(localFile.getAbsolutePath());

			destination.copyFrom(file, Selectors.SELECT_SELF);

			logger.debug( remote + " copied to " + localFile.getAbsolutePath());

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			manager.close();
		}
	}

	/**
	 * Test for the existence of a file on a server with default attempts and timeout.
	 */
	@Override
	public boolean exists(String host, String remoteFile) throws IOException {
		return exists(host, remoteFile, retries, timeout);
	}
	
	
	public boolean exists(String host, String remoteFile, int attempts, int delayMillis) {

		logger.debug("exists called: " + remoteFile + " at " + host);
		
		int attempt = 0;
		FileObject file;
		boolean result = false;

		while (attempt < retries) {
			attempt++;
			StandardFileSystemManager manager = new StandardFileSystemManager();
			try {
				manager.init();
				// Create remote object
				file = manager.resolveFile(
						getRemoteFileString(host, remoteFile), createDefaultOptions(hostKeyChecking, timeout));

				logger.debug(file + " exists: " + file.exists());

				result = file.exists();
				
				// no exception, return result
				attempt = retries;
				
			} catch (Exception e) {
				if (attempt < retries) {
					logger.debug("failed, retrying: " + e.getCause().toString());
					// ignore exception, try again
					continue;
				}
				throw new RuntimeException(e);
			} finally {
				manager.close();
			}
		}
		return result;
	}

	@Override
	public void delete(String host, String remoteFile) throws IOException {
		StandardFileSystemManager manager = new StandardFileSystemManager();
		 
		logger.debug("delete called: " + remoteFile + " at " + host);
		
	    try {
	        manager.init();
	 
	        // Create remote object
	        FileObject file = manager.resolveFile(
	                getRemoteFileString(host, remoteFile), createDefaultOptions(hostKeyChecking, timeout));
	 
	        if (file.exists()) {
	            file.delete();
	            logger.debug("Deleted " + remoteFile + "@" + host);
	        }
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } finally {
	        manager.close();
	    }
	}

	private String getRemoteFileString(String host, String path) throws GridUnresolvableHostException {
		String remote = "sftp://" + this.hostResolver.getUsername(host) + "@" + host + "/" + path;
		logger.debug("constructed remote file string: " + remote);
		return remote;
	}

	private static FileSystemOptions createDefaultOptions(
			String hostKeyChecking, int timeout) throws FileSystemException {

		FileSystemOptions opts = new FileSystemOptions();

		SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
				opts, hostKeyChecking);
		SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
		SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, timeout);

		File[] ifs = new File[1];
		ifs[0] = identityFile.getAbsoluteFile();
		SftpFileSystemConfigBuilder.getInstance().setIdentities(opts, ifs);
		return opts;
	}

	public void setHostKeyChecking(String s) {
		if (s == "yes" || s == "true") {
			hostKeyChecking = "yes";
		}
	}

	public void setTimeout(int millis) {
		this.timeout = millis;
	}

	public void setIdentityFile(String s) {
		String home = System.getProperty("user.home");
		s = home + s.replaceAll("~(.*)", "$1");
		identityFile = new File(s).getAbsoluteFile();
	}

	@Override
	public void touch(String host, String remoteFile) throws IOException {
		StandardFileSystemManager manager = new StandardFileSystemManager();

		logger.debug("touch called: " + remoteFile + " at " + host);
		
		try {
			manager.init();

			String remote = getRemoteFileString(host, remoteFile);
			FileObject destination = manager.resolveFile(remote,
					createDefaultOptions(hostKeyChecking, timeout));

			destination.createFile();
			logger.debug(destination.getName().getPath() + " created on " + remote);

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			manager.close();
		}
		
	}

	public GridHostResolver getHostResolver() {
		return hostResolver;
	}

	public void setHostResolver(GridHostResolver hostResolver) {
		this.hostResolver = hostResolver;
	}

}
