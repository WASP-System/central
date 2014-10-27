/**
 * 
 */
package edu.yu.einstein.wasp.grid;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * A GridHostResolver implementation that will choose appropriate hosts based on the presence of
 * configured software at the remote host.  Ties will be resolved by the 1) the presence of files
 * on the remote host, then in order of hosts stored in its list.
 * 
 * @author calder
 *
 */
public class SoftwareBasedGridHostResolver extends AbstractGridHostResolver {

	private Map<GridWorkService, List<SoftwarePackage>> gridWorkServiceMap = new LinkedHashMap<GridWorkService,List<SoftwarePackage>>();
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 
	 */
	public SoftwareBasedGridHostResolver(Map<GridWorkService, List<SoftwarePackage>> gridWorkServices) {
		this.gridWorkServiceMap.putAll(gridWorkServices);
	}
	
	/**
	 * return a list of GridWorkServices that have the appropriate software
	 * 
	 * @param w
	 * @return
	 */
	private List<GridWorkService> getPossibleDestinations(WorkUnitGridConfiguration c) {
		List<GridWorkService> retval = new ArrayList<GridWorkService>();
		for (GridWorkService ws : gridWorkServiceMap.keySet()) {
			List<SoftwarePackage> soft = gridWorkServiceMap.get(ws);
			if (soft.containsAll(c.getSoftwareDependencies())) {
				retval.add(ws);
			}
		}
		return retval;
	}
	
	/**
	 * from a list of possible GridWorkServices (already proven to have appropriate software),
	 * pick one.
	 * 
	 * If one or more has the files staged, return the first.
	 * if none has files staged, return the first.
	 * 
	 * @return
	 */
	private GridWorkService resolveTies(List<GridWorkService> workServices, WorkUnitGridConfiguration c) {
		return null;
	}
	
	/**
	 * Check to see if the host is the primary host or already has the files staged for use
	 * 
	 * @param gridWorkService
	 * @param w
	 * @return
	 */
	private boolean hasStagedFiles(GridWorkService gridWorkService, WorkUnit w) {
		return false;
	}
	
	private GridWorkService getWorkService(WorkUnitGridConfiguration c) throws GridUnresolvableHostException {
		List<GridWorkService> wServ = getPossibleDestinations(c);
		if (wServ.size() == 0) {
			List<SoftwarePackage> swps = c.getSoftwareDependencies();
			ArrayList<String> sw = new ArrayList<String>(swps.size());
			for (SoftwarePackage s : swps)
				 sw.add(s.getSoftwareName() + "/" + s.getSoftwareVersion());
			String mess = "Unable to find suitable host for work unit requesting: " + StringUtils.join(sw.toArray(), ", ") + ".";
			logger.warn(mess);
			throw new GridUnresolvableHostException(mess);
		}
		if (wServ.size() == 1)
			return wServ.get(0);
		return resolveTies(wServ, c);
	}
	
	/**
	 * Check to see if the host has the proper software available
	 * 
	 * @param software
	 * @param workService
	 * @return
	 */
	private boolean hasSoftware(List<SoftwarePackage> software, GridWorkService workService) {
		return false;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String getHostname(WorkUnitGridConfiguration c) throws GridUnresolvableHostException {
		return getWorkService(c).getTransportConnection().getHostName();
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String getUsername(WorkUnitGridConfiguration c) throws GridUnresolvableHostException {
		return getWorkService(c).getTransportConnection().getUserName();
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String getUsername(String hostname) throws GridUnresolvableHostException {
		for (GridWorkService gws : gridWorkServiceMap.keySet()) {
			if (gws.getTransportConnection().getHostName().equals(hostname))
				return gws.getTransportConnection().getUserName();
		}
		throw new GridUnresolvableHostException("Unable to find host with name " + hostname);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public GridWorkService getGridWorkService(WorkUnitGridConfiguration c) throws GridUnresolvableHostException {
		return getWorkService(c);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public GridWorkService getGridWorkService(GridResult r) throws GridUnresolvableHostException {
		String hostname = r.getHostname();
		return getGridWorkService(hostname);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public GridWorkService getGridWorkService(String hostname) throws GridUnresolvableHostException {
		for (GridWorkService gws : gridWorkServiceMap.keySet()) {
			if (gws.getTransportConnection().getHostName().equals(hostname))
				return gws;
		}
		String mess = "Unknown host: " + hostname;
		logger.warn(mess);
		throw new GridUnresolvableHostException(mess);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public List<GridWorkService> getAvailableWorkServices() {
		List<GridWorkService> retval = new ArrayList<GridWorkService>();
		retval.addAll(gridWorkServiceMap.keySet());
		return retval;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String getParallelEnvironmentString(WorkUnitGridConfiguration c) {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public GridResult execute(WorkUnit w) throws GridException {
		return getWorkService(w.getConfiguration()).execute(w);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFinished(GridResult g) throws GridException {
		return getGridWorkService(g).isFinished(g);
	}

}
