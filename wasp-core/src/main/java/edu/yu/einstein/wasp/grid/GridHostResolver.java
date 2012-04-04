package edu.yu.einstein.wasp.grid;

import java.net.URI;
import java.net.URISyntaxException;

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

}
