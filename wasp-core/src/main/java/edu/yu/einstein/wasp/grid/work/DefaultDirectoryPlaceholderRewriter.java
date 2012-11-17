/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.util.PropertyHelper;

/**
 * Default implementation of {@link DirectoryPlaceholderRewriter}.  Rewrites static WorkUnit.SCRATCH_DIR
 * and WorkUnit.RESULTS_DIR values for each host based on settings in the {@link GridTransportService}.
 * If the settings are not set, it will default to "~/" and log a warning.
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
	public void replaceDirectoryPlaceholders(GridTransportService transportService, WorkUnit w) {
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
		String wd = w.getWorkingDirectory().replaceAll(WorkUnit.SCRATCH_DIR, scratch + "/").replaceAll("//", "/").replaceAll("//", "/");
		w.setWorkingDirectory(wd);
		String rd = w.getResultsDirectory().replaceAll(WorkUnit.RESULTS_DIR, scratch + "/").replaceAll("//", "/").replaceAll("//", "/");
		w.setResultsDirectory(rd);

	}

}
