package edu.yu.einstein.wasp.grid.work;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.UserInfo;

import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
/**
 * Service that implements both the {@link GridTransportService} and {@link GridWorkService} interfaces.  
 * Provides methods to execute basic shell commands (GridWorkService) over an key authenticated
 * SSH connection (GridTransportService). 
 * 
 * @author calder
 *
 */
public class SshService implements GridWorkService, GridTransportService {
	
	private GridTransportService transportService;
	
	private GridHostResolver hostResolver;
	
	private String hostKeyChecking = "no";
	private static File identityFile;
	
	private String name;
	
	private SoftwareManager softwareManager;
	
	// not used in ssh implementation
	private String jobNamePrefix;
	
	private static final Log logger = LogFactory.getLog(SshService.class);

	@Override
	public boolean isFinished(GridResult g) throws GridAccessException {
		return true;
	}

	@Override
	public void connect(WorkUnit w) throws GridAccessException, GridUnresolvableHostException {
		SshTransportConnection stc = new SshTransportConnection(hostResolver, hostKeyChecking, identityFile, w);
		w.setConnection(stc);
	}
	@Override
	public GridTransportConnection connect(String hostname) throws GridAccessException, GridUnresolvableHostException {
		return new SshTransportConnection(this.hostResolver, hostname, hostKeyChecking, identityFile);
	}
	

	@Override
	public GridResult execute(WorkUnit w) throws GridAccessException, GridUnresolvableHostException {
		logger.debug("attempting to execute " + w.getId());
		transportService.connect(w);
		GridResult result = w.getConnection().sendExecToRemote(w);
		return result;
		
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
	
	public void setTransportService(GridTransportService ts) {
		this.transportService = ts;
	}
	
	public GridTransportService getTransportService() {
		return this.transportService;
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

	public void setHostResolver(GridHostResolver hostResolver) {
		this.hostResolver = hostResolver;
	}
	
	public GridHostResolver getHostResolver() {
		return this.hostResolver;
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
	public void setJobNamePrefix(String name) {
		this.jobNamePrefix = name;
	}

	@Override
	public void setAvailableParallelEnvironments(List<String> pe) {
		// MPI PE not available in direct ssh mode 
	}

	@Override
	public List<String> getAvailableParallelEnvironments() {
		// MPI PE not available in direct ssh mode
		return new ArrayList<String>();
	}

}
