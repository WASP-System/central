/**
 * 
 */
package edu.yu.einstein.wasp.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportService;
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
	public List getAvailableWorkServices() {
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
	public GridResult execute(WorkUnit w) throws GridAccessException,
			GridUnresolvableHostException, GridExecutionException {
		logger.debug("executing WorkUnit: " + w.toString() + " handing off to: " + getGridWorkService(w).getTransportService().getName());
		return getGridWorkService(w).execute(w);
	}

	@Override
	public boolean isFinished(GridResult g) throws GridAccessException,
			GridExecutionException, GridUnresolvableHostException {
		return gws.isFinished(g);
	}

	@Override
	public GridWorkService getGridWorkService(GridResult r) {
		return gws;
	}

}
