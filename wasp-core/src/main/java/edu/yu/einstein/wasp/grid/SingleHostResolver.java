/**
 * 
 */
package edu.yu.einstein.wasp.grid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.yu.einstein.wasp.grid.work.WorkUnit;

/**
 * Default implementation of GridHostResolver.  Provides a mechanism for sending {@link WorkUnit}s out to a single
 * host.
 * 
 * @author calder
 *
 */
public class SingleHostResolver extends AbstractGridHostResolver implements GridHostResolver {
	
	private String hostname;
	private String username;
	
	private final Log logger = LogFactory.getLog(getClass());
	
	public SingleHostResolver() {
	}
	
	public SingleHostResolver(String host, String username) {
		this.hostname = host;
		this.username = username;
	}

	public String getHostname(WorkUnit w) {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
		
		logger.debug("set remote host execution to: " + hostname);
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getUsername(WorkUnit w) {
		return username;
	}

	@Override
	public String getUsername(String hostname) {
		return username;
	}

}
