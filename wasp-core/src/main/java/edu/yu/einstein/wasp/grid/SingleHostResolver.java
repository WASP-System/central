/**
 * 
 */
package edu.yu.einstein.wasp.grid;

import java.util.Set;

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
	private String account;
	
	private final Log logger = LogFactory.getLog(getClass());
	private String queue;
	private String maxRunTime;
	private String project;
	private String mailRecipient;
	private String mailCircumstances;
	private Set<String> parallelEnvironments;
	
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

	public String getAccount() {
		return account;
	}

	/**
	 * Set host and fabric specific account information
	 * @param account
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getQueue(WorkUnit w) {
		return queue;
	}
	public void setQueue(String queue) {
		this.queue = queue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMaxRunTime(WorkUnit w) {
		return maxRunTime;
	}
	public void setMaxRunTime(String max) {
		this.maxRunTime = max;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAccount(WorkUnit w) {
		return account;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProject(WorkUnit w) {
		return project;
	}
	public void setProject(String proj) {
		this.project = proj;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMailRecipient(WorkUnit w) {
		return mailRecipient;
	}
	public void setMailRecipient(String mail) {
		this.mailRecipient = mail;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMailCircumstances(WorkUnit w) {
		return mailCircumstances;
	}
	public void setMailCircumstances(String circ) {
		this.mailCircumstances = circ;
	}

	@Override
	public void setAvailableParallelEnvironments(Set<String> pe) {
		this.parallelEnvironments = pe;
		
	}

	@Override
	public String getParallelEnvironmentString(WorkUnit w) {
		return (String) parallelEnvironments.toArray()[0];
	}

}
