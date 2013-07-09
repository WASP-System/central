/**
 * 
 */
package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.batch.core.extension.JobExplorerWasp;
import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.dao.RunCellDao;
import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.dao.RunMetaDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.exception.InvalidParameterException;
import edu.yu.einstein.wasp.exception.MetaAttributeNotFoundException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.RunException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspRunTask;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.JobStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.RunStatusMessageTemplate;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunCell;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.plugin.RunQcProviding;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.sequence.SequenceReadProperties;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.service.WorkflowService;

/**
 * @author calder
 *
 */
@Service
@Transactional("entityManager")
public class RunServiceImpl extends WaspMessageHandlingServiceImpl implements RunService {
	
	private static Logger logger = LoggerFactory.getLogger(RunServiceImpl.class);
	
	protected RunDao runDao;
	
	@Autowired
	protected SampleService sampleService;
	
	@Autowired
	protected WorkflowService workflowService;
	
	@Autowired
	private SampleMetaDao sampleMetaDao;
	
	@Autowired
	private WaspPluginRegistry waspPluginRegistry;
	
	@Autowired
	private UserService userService;
	
	protected JobExplorerWasp batchJobExplorer;
	
	@Autowired
	void setJobExplorer(JobExplorer jobExplorer){
		this.batchJobExplorer = (JobExplorerWasp) jobExplorer;
	}
	

	@Override
	@Autowired
	public void setRunDao(RunDao runDao) {
		this.runDao = runDao;

	}

	@Override
	public RunDao getRunDao() {
		return this.runDao;
	}
	
	
	private RunMetaDao runMetaDao;

	@Autowired
	public void setRunMetaDao(RunMetaDao runMetaDao) {
		this.runMetaDao = runMetaDao;
	}
	
	
	
	@Override
	public RunMetaDao getRunMetaDao() {
		return runMetaDao;
	}
	
	private RunCellDao runCellDao;
	
	@Autowired
	public void setRunCellDao(RunCellDao runCellDao) {
		this.runCellDao = runCellDao;
	}

	@Override
	public RunCellDao getRunCellDao() {
		return runCellDao;
	}

	@Override
	public Run getRunByName(String name) {
		Map<String, String> m = new HashMap<String,String>();
		m.put("name", name);
		logger.debug("looking for run " + name);
		List<Run> l = runDao.findByMap(m);
		
		Run result;
		if (l.size() > 1) {
			logger.warn("Run name " + name + " is not unique! Returning only the newest run.");
			result = l.get(0);
			
			for (int x = 1; x <= l.size(); x++) {
				Run r = l.get(x);
				if (r.getId() > result.getId()) result = r;
			}
		} else {
			result = l.get(0);
		}
		
		return result;
	}
	
	@Override
	public List<Run> getRunsForPlatformUnit(Sample pu) throws SampleTypeException{
		Assert.assertParameterNotNull(pu, "no platform unit provided (is null)");
		Assert.assertParameterNotNullNotZero(pu.getId(), "pu is not a valid sample");
		if (!sampleService.isPlatformUnit(pu))
			throw new SampleTypeException("Sample is not of type platformunit");
		Map<String, Integer> searchMap = new HashMap<String, Integer>();
		searchMap.put("sampleId", pu.getId());
		return runDao.findByMap(searchMap);
	}
	

	@Override
	public List<Run> getSuccessfullyCompletedRunsForPlatformUnit(Sample pu) throws SampleTypeException{
		Assert.assertParameterNotNull(pu, "no platform unit provided (is null)");
		Assert.assertParameterNotNullNotZero(pu.getId(), "pu is not a valid sample");
		if (!sampleService.isPlatformUnit(pu))
			throw new SampleTypeException("Sample is not of type platformunit");
		Map<String, Integer> searchMap = new HashMap<String, Integer>();
		searchMap.put("sampleId", pu.getId());
		List<Run> runList = new ArrayList<Run>();
		for (Run run : runDao.findByMap(searchMap)) {
			if (this.isRunSuccessfullyCompleted(run)) {
				runList.add(run);
			}
		}
		
		return runList;
	}
	
	@Override
	public void initiateRun(Run run) {
		Assert.assertParameterNotNull(run, "run cannot be null");
		Assert.assertParameterNotNull(run.getId(), "run is not valid");
		// send message to initiate job processing
		Map<String, String> jobParameters = new HashMap<String, String>();
		jobParameters.put(WaspJobParameters.RUN_ID, run.getId().toString() );
		jobParameters.put(WaspJobParameters.RUN_NAME, run.getName());
		String rcIname = run.getResourceCategory().getIName();
		logger.debug("launching Batch job for runId=" + run.getId().toString() + ", looking for GENERIC flows handling area '" + rcIname + "'");
		for (BatchJobProviding plugin : waspPluginRegistry.getPluginsHandlingArea(rcIname, BatchJobProviding.class)) {
			// TODO: check the transactional behavior of this block when
			// one job launch fails after successfully sending another
			logger.debug("Getting generic flow for plugin: '" + plugin.getPluginName() + "'");
			String flowName = plugin.getBatchJobNameByArea(BatchJobTask.GENERIC, rcIname);
			if (flowName == null){
				logger.warn("No generic flow found for plugin handling " + rcIname);
				continue;
			}
			try {
				logger.debug("Going to launch Batch job '" + flowName + "'");
				launchBatchJob(flowName, jobParameters);
			} catch (WaspMessageBuildingException e) {
				throw new MessagingException(e.getLocalizedMessage(), e);
			}
		}
	}
	
	@Override
	public void launchBatchJob(String flow, Map<String,String> jobParameters) throws WaspMessageBuildingException {
		BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate(new BatchJobLaunchContext(flow, jobParameters));
		sendOutboundMessage(batchJobLaunchMessageTemplate.build(), true);
	}
	
	@Override
	public Run updateAndInitiateRun(Run run){
		// transactional such that if message sending fails run saving will be rolled back
		Run savedRun = this.updateRun(run);
		this.initiateRun(savedRun);
		return savedRun;
	}
	
	@Override
	public Run updateRun(Run run){
		Assert.assertParameterNotNull(run, "run cannot be null");
		List<RunMeta> meta = new ArrayList<RunMeta>();
		meta.addAll(run.getRunMeta()); // effectively copies the list
		run = runDao.save(run); //since this object was pulled from the database, it is persistent, and any alterations are automatically updated; thus this line is superfluous
		try {
			runMetaDao.setMeta(meta, run.getId());
		} catch (MetadataException e) {
			throw new RuntimeException(e); // trigger rollback
		}
		return run;
	}


	@Override
	public Run getRunById(int runId) {
		return runDao.findById(runId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Run> getCurrentlyActiveRuns(){
		Set<Run> runs = new LinkedHashSet<Run>();
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> runIdStringSet = new LinkedHashSet<String>();
		runIdStringSet.add("*");
		parameterMap.put(WaspJobParameters.RUN_ID, runIdStringSet);
		List<JobExecution> jobExecutions = batchJobExplorer.getJobExecutions(parameterMap, false, BatchStatus.STARTED);
		for(JobExecution jobExecution: jobExecutions){
			try{
				Integer runId = Integer.valueOf(batchJobExplorer.getJobParameterValueByKey(jobExecution, WaspJobParameters.RUN_ID));
				runs.add(runDao.getRunByRunId(runId));
			} catch (Exception e) {
				logger.warn("unable to proccess a run Id as a parameter for job execution: "+ jobExecution.toString());
			}
		}
		return runs;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	public boolean isRunActive(Run run){
		Assert.assertParameterNotNull(run, "run cannot be null");
		Assert.assertParameterNotNull(run.getId(), "run must be defined");
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> runIdStringSet = new LinkedHashSet<String>();
		runIdStringSet.add(run.getId().toString());
		parameterMap.put(WaspJobParameters.RUN_ID, runIdStringSet);
		if (! batchJobExplorer.getJobExecutions(parameterMap, false, BatchStatus.STARTED).isEmpty())
			return true;
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Run> getSuccessfullyCompletedRuns(){
		Set<Run> runs = new LinkedHashSet<Run>();
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new LinkedHashSet<String>();
		jobIdStringSet.add("*");
		parameterMap.put(WaspJobParameters.RUN_ID, jobIdStringSet);
		List<JobExecution> jobExecutions = batchJobExplorer.getJobExecutions(parameterMap, false, ExitStatus.COMPLETED);
		for(JobExecution jobExecution: jobExecutions){
			try {
				Integer runId = Integer.valueOf(batchJobExplorer.getJobParameterValueByKey(jobExecution, WaspJobParameters.RUN_ID));
				runs.add(runDao.getRunByRunId(runId));
			} catch (Exception e) {
				logger.warn("unable to proccess a run Id as a parameter for job execution: "+ jobExecution.toString());
			}
		}
		return runs;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRunSuccessfullyCompleted(Run run){
		Assert.assertParameterNotNull(run, "run cannot be null");
		Assert.assertParameterNotNull(run.getId(), "run must be defined");
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> runIdStringSet = new LinkedHashSet<String>();
		runIdStringSet.add(run.getId().toString());
		parameterMap.put(WaspJobParameters.RUN_ID, runIdStringSet);
		if (! batchJobExplorer.getJobExecutions(parameterMap, false, ExitStatus.COMPLETED).isEmpty())
			return true;
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Run> getRunsAwaitingQc(){
		Set<Run> runsAwaitingQc = new LinkedHashSet<Run>();
		for (RunQcProviding plugin : waspPluginRegistry.getPlugins(RunQcProviding.class)){
			List<StepExecution> stepExecutions = batchJobExplorer.getStepExecutions(plugin.getRunQcStepName(), BatchStatus.STARTED);
			for(StepExecution stepExec: stepExecutions){
				try{
					Integer runId = Integer.valueOf(batchJobExplorer.getJobParameterValueByKey(stepExec, WaspJobParameters.RUN_ID));
					runsAwaitingQc.add(runDao.getRunByRunId(runId));
				} catch (Exception e) {
					logger.warn("unable to proccess a run Id as a parameter for step execution: "+ stepExec.toString());
				}
			}
		}
		return runsAwaitingQc;
	}
		
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRunsAwaitingQc(){
		for (RunQcProviding plugin : waspPluginRegistry.getPlugins(RunQcProviding.class)){
			List<StepExecution> stepExecutions = batchJobExplorer.getStepExecutions(plugin.getRunQcStepName(), BatchStatus.STARTED);
			if (stepExecutions.size() > 0)
				return true;
		}
		return false;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public Map<Sample, Job> getLibraryJobPairsOnSuccessfulRunCellsWithoutControls(Run run){
		Assert.assertParameterNotNull(run, "a run must be provided");
		Assert.assertParameterNotNullNotZero(run.getId(), "run provided is invalid or not in the database");
		Assert.assertParameterNotNull(run.getId(), "a runId must be have a valid database entry");
		Map<Sample, Job> libraryJob = new HashMap<Sample, Job>();
		try {
			for (Sample cell: sampleService.getIndexedCellsOnPlatformUnit(run.getPlatformUnit()).values()){
				try {
					if (sampleService.isCellSequencedSuccessfully(cell)){
						for (Sample library: sampleService.getLibrariesOnCellWithoutControls(cell)){
							try{
								libraryJob.put(library, sampleService.getJobOfLibraryOnCell(cell, library));
							} catch (SampleException e){
								logger.warn("Unexpected SampleException caught: " + e.getLocalizedMessage());
							}
						}
					}
				} catch (MetaAttributeNotFoundException e) {
					// do nothing as not yet set
				}
			}
		} catch (SampleTypeException e) {
			logger.warn("Unexpected SampleTypeException caught: " + e.getLocalizedMessage());
		}
		return libraryJob;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public Map<Sample, Job> getLibraryJobPairsOnSuccessfulRunCells(Run run){
		Assert.assertParameterNotNull(run, "a run must be provided");
		Assert.assertParameterNotNullNotZero(run.getId(), "run provided is invalid or not in the database");
		Assert.assertParameterNotNull(run.getId(), "a runId must be have a valid database entry");
		Map<Sample, Job> libraryJob = new HashMap<Sample, Job>();
		try {
			for (Sample cell: sampleService.getIndexedCellsOnPlatformUnit(run.getPlatformUnit()).values()){
				try {
					if (sampleService.isCellSequencedSuccessfully(cell)){
						for (Sample library: sampleService.getLibrariesOnCell(cell)){
							try{
								libraryJob.put(library, sampleService.getJobOfLibraryOnCell(cell, library));
							} catch (SampleException e){
								logger.warn("Unexpected SampleException caught: " + e.getLocalizedMessage());
							}
						}
					}
				} catch (MetaAttributeNotFoundException e) {
					// do nothing as not yet set
				}
			}
		} catch (SampleTypeException e) {
			logger.warn("Unexpected SampleTypeException caught: " + e.getLocalizedMessage());
		}
		return libraryJob;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public Set<SampleSource> getCellLibrariesOnSuccessfulRunCellsWithoutControls(Run run){
		Assert.assertParameterNotNull(run, "a run must be provided");
		Assert.assertParameterNotNullNotZero(run.getId(), "run provided is invalid or not in the database");
		Assert.assertParameterNotNull(run.getId(), "a runId must be have a valid database entry");
		Set<SampleSource> libraryCell = new LinkedHashSet<SampleSource>();
		try {
			for (Sample cell: sampleService.getIndexedCellsOnPlatformUnit(run.getPlatformUnit()).values()){
				try {
					if (sampleService.isCellSequencedSuccessfully(cell)){
						for (Sample library: sampleService.getLibrariesOnCellWithoutControls(cell)){
							try{
								libraryCell.add(sampleService.getCellLibrary(cell, library));
							} catch (SampleException e){
								logger.warn("Unexpected SampleException caught: " + e.getLocalizedMessage());
							}
						}
					}
				} catch (MetaAttributeNotFoundException e) {
					// do nothing as not yet set
				}
			}
		} catch (SampleTypeException e) {
			logger.warn("Unexpected SampleTypeException caught: " + e.getLocalizedMessage());
		}
		return libraryCell;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public List<Sample> getCellsOnSuccessfulRunCellsWithoutControlsForJob(Run run, Job job){
		Assert.assertParameterNotNull(run, "a run must be provided");
		Assert.assertParameterNotNullNotZero(run.getId(), "run provided is invalid or not in the database");
		Assert.assertParameterNotNull(run.getId(), "a runId must be have a valid database entry");
		List<Sample> cellList = new ArrayList<Sample>();
		Set<SampleSource> libraryCellSet = this.getCellLibrariesOnSuccessfulRunCellsWithoutControls(run);
		for (SampleSource lc : libraryCellSet) {
			if (sampleService.getJobOfLibraryOnCell(lc).getId()==job.getId()) {
				cellList.add(sampleService.getCell(lc));
			}
		}
		return cellList;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public Set<SampleSource> getCellLibrariesOnSuccessfulRunCells(Run run){
		Assert.assertParameterNotNull(run, "a run must be provided");
		Assert.assertParameterNotNullNotZero(run.getId(), "run provided is invalid or not in the database");
		Assert.assertParameterNotNull(run.getId(), "a runId must be have a valid database entry");
		Set<SampleSource> libraryCell = new LinkedHashSet<SampleSource>();
		try {
			for (Sample cell: sampleService.getIndexedCellsOnPlatformUnit(run.getPlatformUnit()).values()){
				try {
					if (sampleService.isCellSequencedSuccessfully(cell)){
						for (Sample library: sampleService.getLibrariesOnCell(cell)){
							try{
								libraryCell.add(sampleService.getCellLibrary(cell, library));
							} catch (SampleException e){
								logger.warn("Unexpected SampleException caught: " + e.getLocalizedMessage());
							}
						}
					}
				} catch (MetaAttributeNotFoundException e) {
					// do nothing as not yet set
				}
			}
		} catch (SampleTypeException e) {
			logger.warn("Unexpected SampleTypeException caught: " + e.getLocalizedMessage());
		}
		return libraryCell;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public Set<Sample> getLibrariesOnSuccessfulRunCellsWithoutControls(Run run){
		Assert.assertParameterNotNull(run, "a run must be provided");
		Assert.assertParameterNotNullNotZero(run.getId(), "run provided is invalid or not in the database");
		Set<Sample> librariesOnRun = new LinkedHashSet<Sample>();
		try {
			for (Sample cell: sampleService.getIndexedCellsOnPlatformUnit(run.getPlatformUnit()).values()){
				try {
					if (sampleService.isCellSequencedSuccessfully(cell))
						librariesOnRun.addAll(sampleService.getLibrariesOnCellWithoutControls(cell));
				} catch (MetaAttributeNotFoundException e) {
					// do nothing as not yet set
				}	
			}
		} catch (SampleTypeException e) {
			logger.warn("Unexpected SampleTypeException caught: " + e.getLocalizedMessage());
		}
		return librariesOnRun;
	}
	

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public Set<Sample> getLibrariesOnSuccessfulRunCells(Run run){
		Assert.assertParameterNotNull(run, "a run must be provided");
		Assert.assertParameterNotNullNotZero(run.getId(), "run provided is invalid or not in the database");
		Set<Sample> librariesOnRun = new LinkedHashSet<Sample>();
		try {
			for (Sample cell: sampleService.getIndexedCellsOnPlatformUnit(run.getPlatformUnit()).values()){
				try {
					if (sampleService.isCellSequencedSuccessfully(cell))
						librariesOnRun.addAll(sampleService.getLibrariesOnCell(cell));
				} catch (MetaAttributeNotFoundException e) {
					// do nothing as not yet set
				}	
			}
		} catch (SampleTypeException e) {
			logger.warn("Unexpected SampleTypeException caught: " + e.getLocalizedMessage());
		}
		return librariesOnRun;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(Run run){
		Assert.assertParameterNotNull(run, "Invalid run provided");
		Assert.assertParameterNotNullNotZero(run.getId(), "Invalid run provided");
		for(RunMeta runMeta : run.getRunMeta()){
			runMetaDao.remove(runMeta);
		}
		runDao.remove(run);
		return;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Run getSequenceRun(Integer runId) throws RunException {
		Assert.assertParameterNotNullNotZero(runId, "Invalid runId provided");
		Run run = runDao.getRunByRunId(runId.intValue());
		if(run == null || run.getId() == null){
			throw new RunException("Run with runId of " + runId.intValue() + " not found in database");
		}
		else if(!run.getResourceCategory().getResourceType().getIName().equals("mps")){
			throw new RunException("Run with runId of " + runId.intValue() + " does not have resourcecategory of mps");
		}
		else if(!run.getResource().getResourceType().getIName().equals("mps")){
			throw new RunException("Run with runId of " + runId.intValue() + " does not have resource whose resourcetype is mps");
		}
		else if(!run.getResource().getResourceCategory().getResourceType().getIName().equals("mps")){
			throw new RunException("Run with runId of " + runId.intValue() + " does not have resource whose resourcecategory is mps");
		}
		return run;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateRunQcStatusSetComplete(Run run) throws WaspMessageBuildingException{
		// TODO: Write test!!
		Assert.assertParameterNotNull(run, "No Run provided");
		Assert.assertParameterNotNullNotZero(run.getId(), "Invalid Run Provided");
		if (!this.isRunActive(run))
			throw new WaspMessageBuildingException("Not going to build message because run " + run.getId() + " is not active");
		RunStatusMessageTemplate messageTemplate = new RunStatusMessageTemplate(run.getId());
		messageTemplate.setUserCreatingMessageFromSession(userService);
		messageTemplate.setTask(WaspRunTask.QC);
		messageTemplate.setStatus(WaspStatus.COMPLETED); 
		try{
			sendOutboundMessage(messageTemplate.build(), false);
		} catch (MessagingException e){
			throw new WaspMessageBuildingException(e.getLocalizedMessage());
		}
	}
	

}
