/**
 * 
 */
package edu.yu.einstein.wasp.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.RunCellDao;
import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.dao.RunMetaDao;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.User;

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
	 * @param runName
	 * @param machineInstance
	 * @param platformUnit
	 * @param technician
	 * @param readLength
	 * @param readType
	 * @param dateStart
	 * @return
	 * @throws SampleTypeException, WaspMessageBuildingException
	 */
	public Run initiateRun(String runName, Resource machineInstance, Sample platformUnit, User technician, String readLength, String readType, Date dateStart ) throws SampleTypeException;

	/**
	 * Update details of existing sequencing run
	 * @param run
	 * @param runName
	 * @param machineInstance
	 * @param platformUnit
	 * @param technician
	 * @param readLength
	 * @param readType
	 * @param dateStart
	 * @return
	 */
	public Run updateRun(Run run, String runName, Resource machineInstance, Sample platformUnit, User technician, String readLength, String readType, Date dateStart);

	/**
	 * Returns a list of runs which match the provided platform unit
	 * @param pu
	 * @return
	 * @throws SampleTypeException
	 */
	public List<Run> getRunsForPlatformUnit(Sample pu) throws SampleTypeException;

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

	public void launchBatchJob(String flow, Map<String, String> jobParameters) throws WaspMessageBuildingException;
	
	
	/**
	   * Gets a list of all non-control library-cell relationships on a run from cells that are marked as being successful and returns
	   * as a set of parameter maps for initiating Batch jobs
	   * @param runId
	   * @return
	   */
	public Set<SampleSource> getLibraryCellPairsOnSuccessfulRunCellsWithoutControls(Run run);

	  
	/**
	   * Gets a list of all library-cell relationships on a run from cells that are marked as being successful and returns
	   * as a set of parameter maps for initiating Batch jobs
	   * @param runId
	   * @return
	   */
	public Set<SampleSource> getLibraryCellPairsOnSuccessfulRunCells(Run run);

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
	  
	  

	  
	  
	  
	  
	  
	  
}
