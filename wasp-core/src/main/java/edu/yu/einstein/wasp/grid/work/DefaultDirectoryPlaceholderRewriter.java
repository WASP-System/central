/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.grid.MisconfiguredWorkUnitException;
import edu.yu.einstein.wasp.util.PropertyHelper;

/**
 * Default implementation of {@link DirectoryPlaceholderRewriter}. Rewrites
 * static WorkUnit.SCRATCH_DIR and WorkUnit.RESULTS_DIR values for each host
 * based on settings in the {@link GridTransportService}.
 * 
 * If the user does not overwrite the default value of the workingDirectory,
 * this implementation will change it to
 * "${hostname.scratch.dir}/workunit.getId()/".
 * 
 * If the user does not overwrite the default value of the resultsDirectory,
 * this implementation will change it to
 * "${hostname.results.dir}/workunit.getRunId()/" and throw an exception if
 * runId is not set.
 * 
 * If the settings are not are not configured, it will default to "$HOME/" and
 * log a warning.
 * 
 * 
 * @author calder
 * 
 */
public class DefaultDirectoryPlaceholderRewriter implements DirectoryPlaceholderRewriter {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.yu.einstein.wasp.grid.work.DirectoryPlaceholderRewriter#
	 * replaceDirectoryPlaceholders
	 * (edu.yu.einstein.wasp.grid.work.GridTransportService,
	 * edu.yu.einstein.wasp.grid.work.WorkUnit)
	 */
	@Override
	public void replaceDirectoryPlaceholders(GridTransportConnection transportConnection, WorkUnit w) throws MisconfiguredWorkUnitException {
		String scratch = transportConnection.getConfiguredSetting("scratch.dir");
		String results = transportConnection.getConfiguredSetting("results.dir");
		logger.debug("scratch: " + scratch + " & " + "results: " + results);
		logger.debug("Work unit configured with work: " + w.getWorkingDirectory() + " & results: " + w.getResultsDirectory());
		if (!PropertyHelper.isSet(scratch)) {
			logger.warn("Scratch directory has not been set!  Set this value in the properties file of the config project: ("
					+ transportConnection.getName() + ".settings.scratch.dir).  Defaulting to \"$HOME/\".");
			scratch = "$HOME/";
		}
		if (!PropertyHelper.isSet(results)) {
			logger.warn("Scratch directory has not been set!  Set this value in the properties file of the config project: ("
					+ transportConnection.getName() + ".settings.results.dir).  Defaulting to \"$HOME/\".");
			results = "$HOME/";
		}
		
		// rewrite the scratch directory.  By definition, scratch is not in results.
		String wd = w.getWorkingDirectory();
		w.setWorkingDirectory(replaceScratch(wd, scratch, w));
		
		// results might be in scratch, or in results, results should be careful to set a subdir
		String rd = w.getResultsDirectory();
		if (rd.contains(WorkUnit.SCRATCH_DIR_PLACEHOLDER)) {
			w.setResultsDirectory(replaceScratch(rd, scratch, w));
		} else if (rd.equals(WorkUnit.RESULTS_DIR_PLACEHOLDER)) {
			// files need to go into $results/"somewhere"
			throw new MisconfiguredWorkUnitException("WorkUnit attempted to use default results location, "
					+ "must set a subfolder.");
		} else {
			w.setResultsDirectory(replaceResults(rd, results));
		}

		logger.debug("After processing, work unit configured with work: " + w.getWorkingDirectory() + " & results: " + w.getResultsDirectory());

	}
	
	private String replaceScratch(String s, String scratch, WorkUnit w) {
		return s.replaceAll(WorkUnit.SCRATCH_DIR_PLACEHOLDER, scratch + "/" + w.getId() + "/").replaceAll("//", "/").replaceAll("//", "/");
	}
	
	private String replaceResults(String s, String results) {
		return s.replaceAll(WorkUnit.RESULTS_DIR_PLACEHOLDER, results + "/").replaceAll("//", "/").replaceAll("//", "/");
	}

}
