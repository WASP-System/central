/**
 * 
 */
package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
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
import edu.yu.einstein.wasp.exception.MetaAttributeNotFoundException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunCell;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.sequence.SequenceReadProperties;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public void createUpdateSequenceRun(Run runInstance, List<RunMeta> runMetaList, Integer platformUnitId, Integer resourceId)throws Exception{
		
		//first check compatibility, then perform create or update
		
		Sample platformUnit = null;
		Resource sequencingMachineInstance = null;
		ResourceCategory resourceCategory = null;
		Run run = null;
		
		if(runInstance==null || runMetaList==null || platformUnitId == null || platformUnitId.intValue()<=0 || resourceId==null || resourceId.intValue()<=0){
			throw new Exception("parameter error in sampleservice.createUpdateSequenceRun");
		}
		
		//database create or update
		try{//regular (rather than runtime) exceptions
			if(runInstance.getId()!=null && runInstance.getId().intValue()>0){
				run = sampleService.getSequenceRun(runInstance.getId());//throws an exception if problem
			}
			else{
				run = new Run();
			}
			platformUnit = sampleService.getPlatformUnit(platformUnitId);//throws exception if not found in db or if not a platformUnit
			sequencingMachineInstance = sampleService.getSequencingMachineByResourceId(resourceId);//throws exception if not found in db or if not for massively-parallel seq.
			resourceCategory = sequencingMachineInstance.getResourceCategory();
			if(resourceCategory==null || resourceCategory.getId()==null || resourceCategory.getId().intValue()<=0){
				throw new Exception("Problem with resourcecategory in sampleservice.createUpdateSequenceRun");
			}
			if(!sampleService.isPlatformUnitCompatibleWithSequencingMachine(platformUnit, sequencingMachineInstance)){
				throw new Exception("platformUnit (ID: " + platformUnit.getId().toString() + ") is not compatible with sequencing machine (ID: " + sequencingMachineInstance.getId().toString()+").");
			}			
		}catch (Exception e){ throw e; }
		
		try{//runtime exceptions
			run.setName(runInstance.getName());//set by system
			run.setUserId(runInstance.getUserId());
			//run.setStartts(new Date());//THIS MUST CHANGE so that it's gotten from param or the runInstance object
			run.setStarted(runInstance.getStarted());
			
			run.setResourceId(sequencingMachineInstance.getId());
			run.setResourceCategoryId(resourceCategory.getId());
			run.setSampleId(platformUnitId);
			
			Run runDB = runDao.save(run);
			if(runDB==null || runDB.getId()==null || runDB.getId().intValue()<=0){
				throw new SampleException("new run unexpectedly not saved");
			}
			runMetaDao.setMeta(runMetaList, runDB.getId()); // persist the metadata; no way to check as this returns void
			
		}catch (Exception e){	throw new RuntimeException(e.getMessage());	}
		return;
	}
	
	@Override
	public Run initiateRun(String runName, Resource machineInstance, Sample platformUnit, User technician, String readLength, String readType, Date dateStart ) throws SampleTypeException{
		Assert.assertParameterNotNull(runName, "runName cannot be null");
		Assert.assertParameterNotNull(machineInstance, "machineInstance cannot be null");
		Assert.assertParameterNotNull(platformUnit, "platformUnit cannot be null");
		Assert.assertParameterNotNullNotZero(platformUnit.getId(), "platformUnit sampleId is zero");
		Assert.assertParameterNotNull(technician, "technician cannot be null");
		Assert.assertParameterNotNullNotZero(technician.getId(), "technician UserId is zero");
		Assert.assertParameterNotNull(readLength, "readLength cannot be null");
		Assert.assertParameterNotNull(readType, "readType cannot be null");
		Assert.assertParameterNotNull(dateStart, "dateStart cannot be null");
		Run newRun = new Run();
		newRun.setResource(machineInstance);
		newRun.setName(runName.trim());
		newRun.setPlatformUnit(platformUnit);//set the flow cell
		newRun.setUser(technician);
		newRun.setStarted(dateStart);
		newRun = runDao.save(newRun);
		logger.debug("-----");
		logger.debug("saved new run runid=" + newRun.getId().intValue());
		try {
			SequenceReadProperties.setSequenceReadProperties(new SequenceReadProperties(readType, Integer.parseInt(readLength)), newRun, runMetaDao, RunMeta.class);
		} catch (MetadataException e1) {
			logger.warn("failed to save readLength and readtype for run with id=" + newRun.getId().intValue());
		}
		logger.debug("saved readLength and readtype for run with id=" + newRun.getId().intValue());
		//runcell
		for(Sample cell: sampleService.getIndexedCellsOnPlatformUnit(platformUnit).values()){
			RunCell runCell = new RunCell();
			runCell.setRun(newRun);//the runid
			runCell.setSample(cell);//the cellid
			runCell = runCellDao.save(runCell);
			logger.debug("saved new run cell runcellid=" + runCell.getId().intValue());
		}
		logger.debug("-----");
		
		
		// send message to initiate job processing
		Map<String, String> jobParameters = new HashMap<String, String>();
		jobParameters.put(WaspJobParameters.RUN_ID, newRun.getId().toString() );
		jobParameters.put(WaspJobParameters.RUN_NAME, newRun.getName());
		String rcIname = newRun.getResourceCategory().getIName();
		logger.debug("launching Batch job for runId=" + newRun.getId().toString() + ", looking for GENERIC flows handling area '" + rcIname + "'");
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
		return newRun;
	}
	
	@Override
	public void launchBatchJob(String flow, Map<String,String> jobParameters) throws WaspMessageBuildingException {
		BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate(new BatchJobLaunchContext(flow, jobParameters));
		sendOutboundMessage(batchJobLaunchMessageTemplate.build(), true);
	}
	
	@Override
	public Run updateRun(Run run, String runName, Resource machineInstance, Sample platformUnit, User technician, String readLength, String readType, Date dateStart){
		Assert.assertParameterNotNull(run, "run cannot be null");
		Assert.assertParameterNotNullNotZero(run.getId(), "runId is zero");
		Assert.assertParameterNotNull(runName, "runName cannot be null");
		Assert.assertParameterNotNull(machineInstance, "machineInstance cannot be null");
		Assert.assertParameterNotNull(platformUnit, "platformUnit cannot be null");
		Assert.assertParameterNotNullNotZero(platformUnit.getId(), "platformUnit sampleId is zero");
		Assert.assertParameterNotNull(technician, "technician cannot be null");
		Assert.assertParameterNotNullNotZero(technician.getId(), "technician UserId is zero");
		Assert.assertParameterNotNull(readLength, "readLength cannot be null");
		Assert.assertParameterNotNull(readType, "readType cannot be null");
		Assert.assertParameterNotNull(dateStart, "dateStart cannot be null");
		run.setResource(machineInstance);
		run.setName(runName.trim());
		run.setPlatformUnit(platformUnit);
		run.setUser(technician);
		run.setStarted(dateStart);

		run = runDao.save(run);//since this object was pulled from the database, it is persistent, and any alterations are automatically updated; thus this line is superfluous
		
		try {
			SequenceReadProperties.setSequenceReadProperties(new SequenceReadProperties(readType, Integer.parseInt(readLength)), run, runMetaDao, RunMeta.class);
		} catch (MetadataException e1) {
			logger.warn("failed to save readLength and readtype for run with id=" + run.getId().intValue());
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
		List<JobExecution> jobExecutions = batchJobExplorer.getJobExecutions(parameterMap, true, BatchStatus.STARTED);
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
		if (! batchJobExplorer.getJobExecutions(parameterMap, true, BatchStatus.STARTED).isEmpty())
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
		List<JobExecution> jobExecutions = batchJobExplorer.getJobExecutions(parameterMap, true, ExitStatus.COMPLETED);
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
		if (! batchJobExplorer.getJobExecutions(parameterMap, true, ExitStatus.COMPLETED).isEmpty())
			return true;
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Run> getRunsAwaitingQc(){
		Set<Run> runsAwaitingQc = new LinkedHashSet<Run>();
		for (Run completedRun : this.getSuccessfullyCompletedRuns()){
			try{
				Sample cell1 = sampleService.getIndexedCellsOnPlatformUnit(completedRun.getPlatformUnit()).get(1);
				sampleService.isCellSequencedSuccessfully(cell1); // do nothing with this, only interested if not set
			} catch(MetaAttributeNotFoundException e) { 
				runsAwaitingQc.add(completedRun); // not yet QC
			} catch(SampleTypeException e1){
				logger.warn("caught unexpected sample type exception: " + e1.getLocalizedMessage());
			}
		}
		return runsAwaitingQc;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRunsAwaitingQc(){
		for (Run completedRun : this.getSuccessfullyCompletedRuns()){
			try{
				Sample cell1 = sampleService.getIndexedCellsOnPlatformUnit(completedRun.getPlatformUnit()).get(1);
				sampleService.isCellSequencedSuccessfully(cell1); // do nothing with this, only interested if not set
			} catch(MetaAttributeNotFoundException e) { 
				return true; // not yet QC
			} catch(SampleTypeException e1){
				logger.warn("caught unexpected sample type exception: " + e1.getLocalizedMessage());
			}
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
	public Set<SampleSource> getLibraryCellPairsOnSuccessfulRunCellsWithoutControls(Run run){
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
		Set<SampleSource> libraryCellSet = this.getLibraryCellPairsOnSuccessfulRunCellsWithoutControls(run);
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
	public Set<SampleSource> getLibraryCellPairsOnSuccessfulRunCells(Run run){
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
	

	

	

}
