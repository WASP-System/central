package edu.yu.einstein.wasp.grid.file;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.grid.work.GridTransportConnection;

/**
 * 
 * @author asmclellan
 *
 */
public class LocalhostFileService implements GridFileService {
	
	private final static Logger logger = LoggerFactory.getLogger(LocalhostFileService.class);
	
	private static boolean userDirIsRoot = true;
	
	
	private GridTransportConnection transportConnection;

	public LocalhostFileService(GridTransportConnection transportConnection) {
		this.transportConnection = transportConnection;
		logger.debug("configured transport service: " + transportConnection.getHostName());
	}
	
	public void setUserDirIsRoot(boolean isRoot) {
		userDirIsRoot = isRoot;
	}
	
	@Override
	public void put(File localFile, String remoteFile) throws IOException {
		Path remote = getLocalhostFilePath(remoteFile);
		logger.debug("put called: " + localFile + " to localhost as " + remote);
		Files.copy(localFile.toPath(), remote, StandardCopyOption.REPLACE_EXISTING);
	}

	@Override
	public void get(String remoteFile, File localFile) throws IOException {
		Path remote = getLocalhostFilePath(remoteFile);
		logger.debug("get called: " + remote + " from localhost as " + localFile);
		Files.copy(remote, localFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	@Override
	public boolean exists(String remoteFile) throws IOException {
		Path remote = getLocalhostFilePath(remoteFile);
		boolean fileExists = Files.exists(remote);
		logger.debug("exists called: " + remote + ": result=" + fileExists);
		return fileExists;
	}

	@Override
	public void touch(String remoteFile) throws IOException {
		Path remote = getLocalhostFilePath(remoteFile);
		logger.debug("touch called: " + remote);
		try {
			Files.createFile(remote);
		} catch (FileAlreadyExistsException e){
			logger.info("not touching " + remote + " as already exists");
		}
	}

	@Override
	public void mkdir(String remoteDir) throws IOException {
		Path remote = getLocalhostFilePath(remoteDir);
		logger.debug("mkdir called: " + remote);
		try {
			Files.createDirectory(remote);
		} catch (FileAlreadyExistsException e){
			logger.info("cannot mkdir " + remote + " as already exists");
		}
	}

	@Override
	public void delete(String remoteFile) throws IOException {
		Path remote = getLocalhostFilePath(remoteFile);
		logger.debug("delete called: " + remote);
		Files.deleteIfExists(remote);
	}

	@Override
	public void move(String origin, String destination) throws IOException {
		Path originPath = getLocalhostFilePath(origin);
		Path destinationPath = getLocalhostFilePath(destination);
		logger.debug("move called: " + originPath + " to " + destinationPath + " at localhost");
		Files.move(originPath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
	}

	@Override
	public void copy(String origin, String destination) throws IOException {
		Path originPath = getLocalhostFilePath(origin);
		Path destinationPath = getLocalhostFilePath(destination);
		logger.debug("copy called: " + originPath + " to " + destinationPath + " at localhost");
		Files.copy(originPath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
	}

	@Override
	public URI remoteFileRepresentationToLocalURI(String file) {
		String fileURI;
		if (file.startsWith("sftp://")) {
			logger.debug("calling remoteFileRepresentationToLocalURI() on a localhost file service with an sftp file"); 
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
	
	public Path getLocalhostFilePath(String path) {
		Path pathObj = null;
		if (userDirIsRoot){
			if (path.startsWith("/"))
				path = path.replaceFirst("/", "");
			pathObj = Paths.get(System.getProperty("user.home") + "/" + path);
		}
		else
			pathObj = Paths.get(path);
		logger.debug("constructed path: " + pathObj);
		return pathObj;
	}

}
