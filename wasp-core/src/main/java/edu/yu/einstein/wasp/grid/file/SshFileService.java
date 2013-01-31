package edu.yu.einstein.wasp.grid.file;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;

/**
 * Implementation of {@link GridFileService} that manages file transfer over
 * SFTP using Apache Commons VFS2/jsch.
 * 
 * @author calder
 * 
 */
public class SshFileService implements GridFileService {
	
	private GridTransportConnection transportConnection;

	private final static Logger logger = LoggerFactory.getLogger(SshFileService.class);

	private String hostKeyChecking = "no";
	private static File identityFile;
	private static boolean userDirIsRoot = true;
	private int timeout = 10000; // milliseconds
	private int retries = 6;
	
	public void setTimeout(int millis) {
		this.timeout = millis;
	}
	public void setRetries(int retries) {
		this.retries = retries;
	}

	public SshFileService(GridTransportConnection transportConnection) {
		this.transportConnection = transportConnection;
		identityFile = transportConnection.getIdentityFile();
		logger.debug("configured transport service: " + transportConnection.getUserName() + "@" + transportConnection.getHostName());
	}

	public void setUserDirIsRoot(boolean isRoot) {
		SshFileService.userDirIsRoot = isRoot;
	}

	@Override
	public void put(File localFile, String remoteFile)
			throws IOException {

		logger.debug("put called: " + localFile + " to " + transportConnection.getHostName() + " as " + remoteFile);

		if (!localFile.exists())
			throw new IOException("File " + localFile.getAbsolutePath()
					+ " not found");

		StandardFileSystemManager manager = new StandardFileSystemManager();

		try {
			manager.init();

			FileObject file = manager.resolveFile(localFile.getAbsolutePath());
			String remote = getRemoteFileURL(remoteFile);
			logger.debug("preparing to copy: " + file.toString() + " to: " + remote);
			FileSystemOptions options = createDefaultOptions(hostKeyChecking, timeout);
			FileObject destination = manager.resolveFile(remote, options);

			destination.copyFrom(file, Selectors.SELECT_SELF);
			logger.debug(localFile.getAbsolutePath() + " copied to " + remote + " (" + destination.getName().getFriendlyURI() + ")");

		} catch (Exception e) {
			logger.error("problem copying file: " + e.getLocalizedMessage());
			throw new IOException(e);
		} finally {
			manager.close();
		}
	}

	@Override
	public void get(String remoteFile, File localFile)
			throws IOException {

		logger.debug("get called: " + remoteFile + " from " + transportConnection.getHostName() + " as " + localFile);

		StandardFileSystemManager manager = new StandardFileSystemManager();

		try {
			if (!exists(remoteFile))
				throw new IOException("File " + remoteFile + "@" + transportConnection.getHostName() + " not found");
			
			manager.init();

			String remote = getRemoteFileURL(remoteFile);
			FileObject file = manager.resolveFile(remote,
					createDefaultOptions(hostKeyChecking, timeout));
			FileObject destination = manager.resolveFile(localFile.getAbsolutePath());

			destination.copyFrom(file, Selectors.SELECT_SELF);

			logger.debug(remote + " copied to " + localFile.getAbsolutePath());

		} catch (Exception e) {
			logger.error("problem getting file: " + e.getLocalizedMessage());
			throw new IOException(e);
		} finally {
			manager.close();
		}
	}

	/**
	 * Test for the existence of a file on a server with default attempts and
	 * timeout.
	 */
	@Override
	public boolean exists(String remoteFile) throws IOException {
		return exists(remoteFile, retries, timeout);
	}

	private boolean exists(String remoteFile, int attempts, int delayMillis) throws IOException {

		logger.debug("exists called: " + remoteFile + " at " + transportConnection.getHostName());

		int attempt = 0;
		FileObject file;
		boolean result = false;

		while (attempt <= retries) {
			attempt++;
			StandardFileSystemManager manager = new StandardFileSystemManager();
			try {
				manager.init();
				// Create remote object
				file = manager.resolveFile(
						getRemoteFileURL(remoteFile), createDefaultOptions(hostKeyChecking, timeout));

				result = file.exists();

				// no exception, return result
				break;

			} catch (Exception e) {
				// FileSystemException, GridUnresolvableHostException
				logger.debug("caught exception in retry block: " + e.getLocalizedMessage());
				if (attempt <= retries) {
					logger.debug("failed, retrying: " + e.getCause().toString());
					// ignore exception, try again
					continue;
				}
				logger.error(e.getLocalizedMessage());
				throw new IOException(e.getLocalizedMessage());
			} finally {
				manager.close();
			}
		}
		logger.debug(remoteFile + " exists: " + result);
		return result;
	}

	@Override
	public void delete(String remoteFile) throws IOException {
		StandardFileSystemManager manager = new StandardFileSystemManager();

		logger.debug("delete called: " + remoteFile + " at " + transportConnection.getHostName());

		try {
			manager.init();

			// Create remote object
			FileObject file = manager.resolveFile(
					getRemoteFileURL(remoteFile), createDefaultOptions(hostKeyChecking, timeout));

			if (file.exists()) {
				file.delete();
				logger.debug("Deleted " + remoteFile + "@" + transportConnection.getHostName());
			}
		} catch (Exception e) {
			logger.error("problem deleting file: " + e.getLocalizedMessage());
			throw new IOException(e);
		} finally {
			manager.close();
		}
	}

	private String getRemoteFileURL(String path) throws GridUnresolvableHostException {
		String remote = "sftp://" + transportConnection.getUserName() + "@" + transportConnection.getHostName() + "/" + path;
		logger.debug("constructed remote file string: " + remote);
		return remote;
	}

	private static FileSystemOptions createDefaultOptions(String hostKeyChecking, int timeout) throws FileSystemException {

		FileSystemOptions opts = new FileSystemOptions();

		SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, hostKeyChecking);
		SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, userDirIsRoot);
		SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, timeout);

		File[] ifs = new File[1];
		ifs[0] = identityFile.getAbsoluteFile();
		SftpFileSystemConfigBuilder.getInstance().setIdentities(opts, ifs);
		logger.debug(opts.toString());
		return opts;
	}

	public void setHostKeyChecking(String s) {
		if (s == "yes" || s == "true") {
			hostKeyChecking = "yes";
		}
	}

	public void setIdentityFile(String s) {
		String home = System.getProperty("user.home");
		s = home + s.replaceAll("~(.*)", "$1");
		identityFile = new File(s).getAbsoluteFile();
	}

	@Override
	public void touch(String remoteFile) throws IOException {
		StandardFileSystemManager manager = new StandardFileSystemManager();

		logger.debug("touch called: " + remoteFile + " at " + transportConnection.getHostName());

		try {
			manager.init();

			String remote = getRemoteFileURL(remoteFile);
			FileObject destination = manager.resolveFile(remote,
					createDefaultOptions(hostKeyChecking, timeout));

			destination.createFile();
			logger.debug(destination.getName().getPath() + " created on " + remote);

		} catch (Exception e) {
			logger.error("problem touching file: " + e.getLocalizedMessage());
			throw new IOException(e);
		} finally {
			manager.close();
		}

	}
	
	@Override
	public URI remoteFileRepresentationToLocalURI(String file) {
		String fileURI;
		if (file.startsWith("sftp://")) {
			if (file.contains("@")) {
				fileURI = "file://" + file.substring(file.indexOf("@") + 1);
			} else {
				fileURI = file.replace("sftp://", "file://");
			}
		} else {
			logger.debug("creating host-based file url of: " + file.toString());
			fileURI = "file://" + transportConnection.getHostName() + "/" + file;
		}
		URI result = URI.create(fileURI).normalize();
		logger.debug("remote file URI: " + result.toString());
		return result;
	}
	@Override
	public void mkdir(String remoteDir) throws IOException {
		StandardFileSystemManager manager = new StandardFileSystemManager();

		logger.debug("mkdir called: " + remoteDir + " at " + transportConnection.getHostName());

		try {
			manager.init();

			String remote = getRemoteFileURL(remoteDir);
			FileObject destination = manager.resolveFile(remote,
					createDefaultOptions(hostKeyChecking, timeout));

			if (!destination.exists())
				destination.createFolder();
			
			logger.debug(destination.getName().getPath() + " created on " + remote);

		} catch (Exception e) {
			logger.error("problem creating directory: " + e.getLocalizedMessage());
			throw new IOException(e);
		} finally {
			manager.close();
		}
		
	}

}
