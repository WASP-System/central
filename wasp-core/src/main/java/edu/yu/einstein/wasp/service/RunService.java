/**
 * 
 */
package edu.yu.einstein.wasp.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.RunCellDao;
import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.dao.RunMetaDao;
import edu.yu.einstein.wasp.exception.RunException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;

/**
 * @author calder
 *
 */
@Service
public interface RunService extends WaspMessageHandlingService {

	/**
	 * setRunDao(RunDao runDao)
	 * 
	 * @param runDao
	 * 
	 */
	public void setRunDao(RunDao runDao);

	/**
	 * getRunDao();
	 * 
	 * @return runDao
	 * 
	 */
	public RunDao getRunDao();


	/**
	 * Get a run object by its name
	 * 
	 * @param name
	 * @return
	 */
	public Run getRunByName(String name);

	/**
	 * Get a run by its ID.
	 * 
	 * @param runId
	 * @return
	 */
	public Run getRunById(int runId);

	public RunMetaDao getRunMetaDao();

	public RunCellDao getRunCellDao();

	/**
	 * Sets up a sequencing run and sends a message via RMI to the wasp-daemon to initiate sequencing run flow
	 * @param run
	 * @throws WaspMessageBuildingException 
	 */
	public void initiateRun(Run run);

	/**
	 * Update details of existing sequencing run
	 * @param run
	 * @return
	 */
	public Run updateRun(Run run);

	/**
	 * Returns a list of runs which match the provided platform unit
	 * @param pu
	 * @return
	 * @throws SampleTypeException
	 */
	public List<Run> getRunsForPlatformUnit(Sample pu) throws SampleTypeException;

	/**
	 * Returns a list of successfully completed runs which match the provided platform unit
	 * @param pu
	 * @return
	 * @throws SampleTypeException
	 */
	public List<Run> getSuccessfullyCompletedRunsForPlatformUnit(Sample pu)	throws SampleTypeException;

	/**
	 * Returns a set of currently active runs
	 * @return
	 */
	public Set<Run> getCurrentlyActiveRuns();

	/**
	 * Returns a set of runs that completed normally
	 * @return
	 */
	public Set<Run> getSuccessfullyCompletedRuns();


	/**
	 * Gets a list of all non-control library-cell relationships on a run from cells that are marked as being successful and returns
	 * as a set of parameter maps for initiating Batch jobs
	 * @param runId
	 * @return
	 */
	public Set<SampleSource> getCellLibrariesOnSuccessfulRunCellsWithoutControls(Run run);


	/**
	 * Gets a list of all library-cell relationships on a run from cells that are marked as being successful and returns
	 * as a set of parameter maps for initiating Batch jobs
	 * @param runId
	 * @return
	 */
	public Set<SampleSource> getCellLibrariesOnSuccessfulRunCells(Run run);

	/**
	 * Gets a list of all libraries on a run from cells that are marked as being successful and returns
	 * as a set of parameter maps for initiating Batch jobs
	 * @param runId
	 * @return
	 */
	public Map<Sample, Job> getLibraryJobPairsOnSuccessfulRunCells(Run run);

	/**
	 * Gets a list of all non-control libraries on a run from cells that are marked as being successful and returns
	 * as a set of parameter maps for initiating Batch jobs
	 * @param runId
	 * @return
	 */
	public Map<Sample, Job> getLibraryJobPairsOnSuccessfulRunCellsWithoutControls(Run run);

	/**
	 * Gets a list of all non-control libraries on a run from cells that are marked as being successful
	 * @param runId
	 * @return
	 */
	public Set<Sample> getLibrariesOnSuccessfulRunCellsWithoutControls(Run run);


	/**
	 * Gets a list of all libraries on a run (including controls) from cells that are marked as being successful
	 * @param run
	 * @return
	 */
	public Set<Sample> getLibrariesOnSuccessfulRunCells(Run run);

	/**
	 * Returns true if the run is registered to be in a COMPLETED state
	 * @param run
	 * @return
	 */
	public boolean isRunSuccessfullyCompleted(Run run);

	/**
	 * Returns true if the run is registered to be in a STARTED state
	 * @param run
	 * @return
	 */
	public boolean isRunActive(Run run);

	/**
	 * Get a list of runs not currently abandoned and not being set as having been QCd
	 * @return
	 */
	public Set<Run> getRunsAwaitingQc();

	/**
	 * Returns true if ANY run that has not ben abandoned is awaiting QC
	 * @return
	 */
	public boolean isRunsAwaitingQc();

	public List<Sample> getCellsOnSuccessfulRunCellsWithoutControlsForJob(Run run, Job job);

	/**
	 * Delete sequence run
	 * @param Run run
	 * @return void
	 */
	public void delete(Run run)throws Exception;

	/**
	 * Gets sequence run record from database. If not found or if not massively-parallel sequence run, throw exception
	 * @param Integer runId
	 * @return Run run
	 */
	public Run getSequenceRun(Integer runId) throws RunException;

	/**
	 * save and initiate a run
	 * @param run
	 * @return
	 * @throws WaspMessageBuildingException 
	 */
	public Run updateAndInitiateRun(Run run);

	/**
	 * 
	 * @param run
	 * @throws WaspMessageBuildingException
	 */
	public void updateRunQcStatusSetComplete(Run run) throws WaspMessageBuildingException;

	/**
	 * 
	 * @param run
	 * @return
	 */
	public ExitStatus getRunBatchStatus(Run run);
}
