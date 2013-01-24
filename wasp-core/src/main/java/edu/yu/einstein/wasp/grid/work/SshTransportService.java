package edu.yu.einstein.wasp.grid.work;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.security.Provider.Service;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.schmizz.sshj.common.Factory;
import net.schmizz.sshj.transport.cipher.AES128CBC;
import net.schmizz.sshj.transport.cipher.AES128CTR;
import net.schmizz.sshj.transport.cipher.AES192CBC;
import net.schmizz.sshj.transport.cipher.AES192CTR;
import net.schmizz.sshj.transport.cipher.AES256CBC;
import net.schmizz.sshj.transport.cipher.AES256CTR;
import net.schmizz.sshj.transport.cipher.BlowfishCBC;
import net.schmizz.sshj.transport.cipher.Cipher;
import net.schmizz.sshj.transport.cipher.TripleDESCBC;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.UserInfo;

import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;

/**
 * Service that implements both the {@link GridTransportService} and
 * {@link GridWorkService} interfaces. Provides methods to execute basic shell
 * commands (GridWorkService) over an key authenticated SSH connection
 * (GridTransportService).
 * 
 * @author calder
 * 
 */
public class SshTransportService implements GridTransportService {

	private String identityFileName;
	private static File identityFile;

	private String name;
	private String hostname;
	private String username;

	private Map<String, String> settings = new HashMap<String, String>();

	private SoftwareManager softwareManager;

	private boolean userDirIsRoot = true;

	private Properties localProperties;

	public Properties getLocalProperties() {
		return localProperties;
	}
	
	public SshTransportService() {
//		BouncyCastleProvider bc = new BouncyCastleProvider();
//		logger.debug("BC: " + bc.getInfo());
//		Security.addProvider(bc);
//		for (Service s : bc.getServices()) {
//			logger.debug(s.getAlgorithm() + ":" + s.getType());
//		}
//		List<Factory.Named<Cipher>> avail = new LinkedList<Factory.Named<Cipher>>(Arrays.<Factory.Named<Cipher>> asList(
//				new AES128CTR.Factory(),
//				new AES192CTR.Factory(),
//				new AES256CTR.Factory(),
//				new AES128CBC.Factory(),
//				new AES192CBC.Factory(),
//				new AES256CBC.Factory(),
//				new TripleDESCBC.Factory(),
//				new BlowfishCBC.Factory()));
//		
//		 boolean warn = false;
//	        // Ref. https://issues.apache.org/jira/browse/SSHD-24
//	        // "AES256 and AES192 requires unlimited cryptography extension"
//	        for (Iterator<Factory.Named<Cipher>> i = avail.iterator(); i.hasNext(); ) {
//	            final Factory.Named<Cipher> f = i.next();
//	            try {
//	                final Cipher c = f.create();
//	                final byte[] key = new byte[c.getBlockSize()];
//	                final byte[] iv = new byte[c.getIVSize()];
//	                c.init(Cipher.Mode.Encrypt, key, iv);
//	            } catch (Exception e) {
//	            	e.printStackTrace();
//	                warn = true;
//	                i.remove();
//	            }
//	        }
		
	}

	public void setLocalProperties(Properties waspSiteProperties) {
		this.localProperties = waspSiteProperties;
		String prefix = this.name + ".settings.";
		for (String key : this.localProperties.stringPropertyNames()) {
			if (key.startsWith(prefix)) {
				String newKey = key.replaceFirst(prefix, "");
				String value = this.localProperties.getProperty(key);
				settings.put(newKey, value);
				logger.debug("Configured setting for host \""
						+ this.name + "\": " + newKey + "=" + value);
			}
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(SshTransportService.class);

	@Override
	public void connect(WorkUnit w) throws GridAccessException, GridUnresolvableHostException {
		SshTransportConnection stc = new SshTransportConnection(this, w);
	}

	private static class SshUserInfo implements UserInfo {

		@Override
		public String getPassphrase() {
			// not implemented
			return null;
		}

		@Override
		public String getPassword() {
			// not implemented
			return null;
		}

		@Override
		public boolean promptPassphrase(String arg0) {
			// not implemented
			return false;
		}

		@Override
		public boolean promptPassword(String arg0) {
			// not implemented
			return false;
		}

		@Override
		public boolean promptYesNo(String arg0) {
			// not implemented
			return false;
		}

		@Override
		public void showMessage(String arg0) {
			// not implemented
		}

	}

	public void setIdentityFileName(String s) {
		this.identityFileName = s;
		setIdentityFile(s);
	}
	
	public String getIdentityFileName() {
		return identityFileName;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setSoftwareManager(SoftwareManager softwareManager) {
		this.softwareManager = softwareManager;

	}

	@Override
	public SoftwareManager getSoftwareManager() {
		return softwareManager;
	}

	@Override
	public String getConfiguredSetting(String key) {
		return settings.get(key);
	}

	@Override
	public boolean isUserDirIsRoot() {
		return userDirIsRoot;
	}

	@Override
	public void setHostName(String hostname) {
		this.hostname = hostname;
	}

	@Override
	public String getHostName() {
		return hostname;
	}

	@Override
	public void setUserName(String username) {
		this.username = username;
	}

	@Override
	public String getUserName() {
		return username;
	}

	@Override
	public File getIdentityFile() {
		return identityFile;
	}

	@Override
	public void setUserDirIsRoot(boolean isRoot) {
		this.userDirIsRoot = isRoot;
	}

	@Override
	public void setIdentityFile(String s) {

		String home = System.getProperty("user.home");
		s = home + s.replaceAll("~(.*)", "$1");
		identityFile = new File(s).getAbsoluteFile();
		logger.debug("set identity file to: " + identityFile.getAbsolutePath());
		
	}
	
	@Override
	public String prefixRemoteFile(String filespec) {
		String prefix = "";
		if (isUserDirIsRoot() && !filespec.startsWith("$HOME")) prefix = "$HOME/";
		String retval = prefix + filespec;
		return retval.replaceAll("//", "/");
	}

	@Override
	public String prefixRemoteFile(URI uri) throws FileNotFoundException, GridUnresolvableHostException {
		if ( !uri.getHost().toLowerCase().equals(hostname))
			throw new GridUnresolvableHostException("file " + uri.toString() + " not registered on " + hostname);
		// TODO: implement remote file management
		if ( !uri.getScheme().equals("file"))
			throw new FileNotFoundException("file not found " + uri.toString() + " unknown scheme " + uri.getScheme());
		
		return prefixRemoteFile(uri.getPath());
	}

}
