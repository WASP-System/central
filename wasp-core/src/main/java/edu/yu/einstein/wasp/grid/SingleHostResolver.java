/**
 * 
 */
package edu.yu.einstein.wasp.grid;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;

/**
 * Default implementation of GridHostResolver.  Provides a mechanism for sending {@link WorkUnit}s out to a single
 * host.
 * 
 * @author calder
 *
 */
public class SingleHostResolver extends AbstractGridHostResolver {
	
	private String account;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private GridWorkService gws;
	
	public SingleHostResolver(GridWorkService gridWorkService) {
		this.gws = gridWorkService;
	}

	public String getHostname(WorkUnit w) {
		return gws.getTransportService().getHostName();
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return gws.getTransportService().getUserName();
	}

	@Override
	public String getUsername(WorkUnit w) {
		return gws.getTransportService().getUserName();
	}

	@Override
	public String getUsername(String hostname) {
		return this.gws.getTransportService().getUserName();
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

	
	

	@Override
	public GridWorkService getGridWorkService(WorkUnit w) {
		return gws;
	}

	@Override
	public List<GridWorkService> getAvailableWorkServices() {
		List<GridWorkService> l = new ArrayList<GridWorkService>();
		l.add(gws);
		return l;
	}

	@Override
	public String getParallelEnvironmentString(WorkUnit w) {
		// TODO: too simplistic, only works with one PE
		return (String) gws.getAvailableParallelEnvironments().toArray()[0];
	}

	@Override
	public GridResult execute(WorkUnit w) throws GridException {
		logger.debug("executing WorkUnit: " + w.toString() + " handing off to: " + getGridWorkService(w).getTransportService().getName());
		return getGridWorkService(w).execute(w);
	}

	@Override
	public boolean isFinished(GridResult g) throws GridException {
		return gws.isFinished(g);
	}

	@Override
	public GridWorkService getGridWorkService(GridResult r) {
		return gws;
	}

	@Override
	public GridWorkService getGridWorkService(String hostname) throws GridUnresolvableHostException {
		if (! gws.getTransportService().getHostName().toLowerCase().equals(hostname.toLowerCase())) {
			String message = "host " + hostname + " not known";
			logger.error(message);
			throw new GridUnresolvableHostException(message);
		}
		return gws;
	}

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.grid.GridHostResolver#getWorkUnit()
	 */
//	@Override
//	public WorkUnit createWorkUnit() {
//		//do nothing method overridden by spring
//		return null;
//	}

}
