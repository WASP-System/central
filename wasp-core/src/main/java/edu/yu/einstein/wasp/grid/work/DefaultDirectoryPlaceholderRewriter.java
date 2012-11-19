/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.util.PropertyHelper;
import edu.yu.einstein.wasp.grid.MisconfiguredWorkUnitException;

/**
 * Default implementation of {@link DirectoryPlaceholderRewriter}.  Rewrites static WorkUnit.SCRATCH_DIR
 * and WorkUnit.RESULTS_DIR values for each host based on settings in the {@link GridTransportService}.
 * 
 * If the user does not overwrite the default value of the workingDirectory, this implementation will
 * change it to "${hostname.scratch.dir}/workunit.getId()/".
 * 
 * If the user does not overwrite the default value of the resultsDirectory, this implementation will
 * change it to "${hostname.results.dir}/workunit.getRunId()/" and throw an exception if runId is not set.
 * 
 * If the settings are not are not configured, it will default to "~/" and log a warning.
 * 
 * 
 * @author calder
 *
 */
public class DefaultDirectoryPlaceholderRewriter implements DirectoryPlaceholderRewriter {


	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.grid.work.DirectoryPlaceholderRewriter#replaceDirectoryPlaceholders(edu.yu.einstein.wasp.grid.work.GridTransportService, edu.yu.einstein.wasp.grid.work.WorkUnit)
	 */
	@Override
	public void replaceDirectoryPlaceholders(GridTransportService transportService, WorkUnit w) throws MisconfiguredWorkUnitException {
		String scratch = transportService.getConfiguredSetting("scratch.dir");
		String results = transportService.getConfiguredSetting("results.dir");
		if (!PropertyHelper.isSet(scratch)) {
			logger.warn("Scratch directory has not been set!  Set this value in the properties file of the config project: (" 
					+ transportService.getName() + ".settings.scratch.dir).  Defaulting to \"~/\".");
			scratch = "~/";
		}
		if (!PropertyHelper.isSet(results)) {
			logger.warn("Scratch directory has not been set!  Set this value in the properties file of the config project: (" 
					+ transportService.getName() + ".settings.results.dir).  Defaulting to \"~/\".");
			results = "~/";
		}
		String wd = w.getWorkingDirectory();
		if (wd.contains(WorkUnit.SCRATCH_DIR_PLACEHOLDER)) {
			wd.replaceAll(WorkUnit.SCRATCH_DIR_PLACEHOLDER, scratch + "/" + w.getId() + "/").replaceAll("//", "/").replaceAll("//", "/");
			w.setWorkingDirectory(wd);
		}
		String rd = w.getResultsDirectory();
		if (rd.contains(WorkUnit.RESULTS_DIR_PLACEHOLDER)) {
			if (w.getRunId() == null) {
				throw new MisconfiguredWorkUnitException("WorkUnit attempted to use default results location, "
						+ "(${hostname.results.dir}/workunit.getRunId()/), but runID was null!");
			}
			rd.replaceAll(WorkUnit.RESULTS_DIR_PLACEHOLDER, results + "/" + w.getRunId() + "/").replaceAll("//", "/").replaceAll("//", "/");
			w.setResultsDirectory(rd);
		}
		
		

	}

}
