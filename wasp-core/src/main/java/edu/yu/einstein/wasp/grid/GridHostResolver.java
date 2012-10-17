package edu.yu.einstein.wasp.grid;

import java.util.List;
import java.util.Set;

import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;

/**
 * Mechanism for fine grained control over where remote jobs are sent.  GridHostResolvers need to implement this 
 * interface and extend the AbstractGridHostResolver class.
 * 
 * @author calder
 *
 */
public interface GridHostResolver {
	
	/**
	 * Returns the name of the host for which the unit of work is to be sent.  
	 * The results of this function should be stable: it is the GridHostResolver's responsibility.
	 * (eg. a pure round-robin approach could create unexpected results).
	 * 
	 * GridHostResolvers need to be able to handle the {@link WorkUnit}s executionEnvironments, which
	 * and implement a strategy for when the host can not be determined (see {@link AbstractGridHostResolver}.
	 * 
	 * @param w
	 * @return
	 */
	public String getHostname(WorkUnit w) throws GridUnresolvableHostException;
	
	/**
	 * Returns the username in use at the destination host.  Username/hostname should be stable for
	 * a given {@link WorkUnit}.
	 * @param w
	 * @return
	 * @throws GridUnresolvableHostException
	 */
	public String getUsername(WorkUnit w) throws GridUnresolvableHostException;
	
	/**
	 * Returns the username at the given host.
	 * @param hostname
	 * @return
	 * @throws GridUnresolvableHostException
	 */
	public String getUsername(String hostname) throws GridUnresolvableHostException;
	
	/**
	 * return a host specific string for the job queue specification.
	 * @param w
	 * @return queue or null
	 */
	public String getQueue(WorkUnit w);
	
	/**
	 * return a host specific string for the job maximum runtime.
	 * @param w
	 * @return runtime or null
	 */
	public String getMaxRunTime(WorkUnit w);
	
	/**
	 * return a host specific string for the account under which to run the job.
	 * @param w
	 * @return account or null
	 */
	public String getAccount(WorkUnit w);
	
	/**
	 * return a host specific string for the project.
	 * @param w
	 * @return project or null
	 */
	public String getProject(WorkUnit w);
	
	/**
	 * return a host specific string for email notification recipients.
	 * @param w
	 * @return email list or null
	 */
	public String getMailRecipient(WorkUnit w);
	
	/**
	 * return a host specific string for the curcumstances for sending email notifications.
	 * @param w
	 * @return notifications or null
	 */
	public String getMailCircumstances(WorkUnit w);
	
	/**
	 * Given a work unit, return the appropriate work service.  
	 * @param w
	 */
	public GridWorkService getGridWorkService(WorkUnit w);
	
	/**
	 * Set up the host resolver with {@link GridWorkSercice}s.
	 * @param gws
	 */
	public void setAvailableWorkServices(List<GridWorkService> gws);
	
	/**
	 * return an appropriate parallel environment string for the host. Should be requested only when required.
	 * @param w
	 * @return string or null
	 */
	public String getParallelEnvironmentString(WorkUnit w);
	
	
}
