/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	public void replaceDirectoryPlaceholders(GridTransportConnection transportConnection, WorkUnit w) {
		String tmp = transportConnection.getConfiguredSetting("tmp.dir");
		String scratch = transportConnection.getConfiguredSetting("scratch.dir");
		String results = transportConnection.getConfiguredSetting("results.dir");
		logger.debug("scratch: " + scratch + " & " + "results: " + results);
		logger.debug("Work unit configured with work: " + w.getConfiguration().getWorkingDirectory() + " & results: " + w.getConfiguration().getResultsDirectory());
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
		if (!PropertyHelper.isSet(tmp)) {
			logger.warn("Tmp directory has not been set!  Set this value in the properties file of the config project: ("
					+ transportConnection.getName() + ".settings.tmp.dir).  Defaulting to \"/tmp/\".");
			tmp = "/tmp/";
		}
		
		// rewrite the tmp directory.
		String td = w.getConfiguration().getTmpDirectory();
		if (td.contains(WorkUnitGridConfiguration.TMP_DIR_PLACEHOLDER))
			w.getConfiguration().setTmpDirectory(replaceTmp(td, tmp, "TMP_", w));
		
		// rewrite the scratch directory.  By definition, scratch is not in results.
		String wd = w.getConfiguration().getWorkingDirectory();
		if (wd.contains(WorkUnitGridConfiguration.TMP_DIR_PLACEHOLDER)){
			w.getConfiguration().setWorkingDirectory(replaceTmp(wd, tmp, "", w));
			w.getConfiguration().setWorkingDirectoryRelativeToRoot(true);
		} else 
			w.getConfiguration().setWorkingDirectory(replaceScratch(wd, scratch, w));
		
		// results might be in scratch, or in results, results should be careful to set a subdir
		String rd = w.getConfiguration().getResultsDirectory();
		if (rd.contains(WorkUnitGridConfiguration.SCRATCH_DIR_PLACEHOLDER)) {
			w.getConfiguration().setResultsDirectory(replaceScratch(rd, scratch, w));
		} else if (rd.contains(WorkUnitGridConfiguration.TMP_DIR_PLACEHOLDER)){
			w.getConfiguration().setResultsDirectory(replaceTmp(rd, tmp, "", w));
		} else if (rd.equals(WorkUnitGridConfiguration.RESULTS_DIR_PLACEHOLDER)) {
			// files need to go into $results/"somewhere"
			logger.info("WorkUnit configured with default results location. Assuming not required.");
		} else {
			w.getConfiguration().setResultsDirectory(replaceResults(rd, results));
		}

		logger.debug("After processing, work unit configured with work: " + w.getConfiguration().getWorkingDirectory() + " & results: " + w.getConfiguration().getResultsDirectory() +
				" & tmp: " + w.getConfiguration().getTmpDirectory());

	}
	
	private String replaceTmp(String s, String tmp, String subFolderPrefix, WorkUnit w) {
		return s.replaceAll(WorkUnitGridConfiguration.TMP_DIR_PLACEHOLDER, tmp + "/" + subFolderPrefix + w.getId() + "/").replaceAll("//", "/");
	}
	
	private String replaceScratch(String s, String scratch, WorkUnit w) {
		return s.replaceAll(WorkUnitGridConfiguration.SCRATCH_DIR_PLACEHOLDER, scratch + "/" + w.getId() + "/").replaceAll("//", "/");
	}
	
	private String replaceResults(String s, String results) {
		return s.replaceAll(WorkUnitGridConfiguration.RESULTS_DIR_PLACEHOLDER, results + "/").replaceAll("//", "/");
	}

}
