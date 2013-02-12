/**
 * 
 */
package edu.yu.einstein.wasp.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunCell;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
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
				if (r.getRunId() > result.getRunId()) result = r;
			}
		} else {
			result = l.get(0);
		}
		
		return result;
	}
	
	@Override
	public List<Run> getRunsForPlatformUnit(Sample pu) throws SampleTypeException{
		Assert.assertParameterNotNull(pu, "no platform unit provided (is null)");
		Assert.assertParameterNotNullNotZero(pu.getSampleId(), "pu is not a valid sample");
		if (!sampleService.isPlatformUnit(pu))
			throw new SampleTypeException("Sample is not of type platformunit");
		Map<String, Integer> searchMap = new HashMap<String, Integer>();
		searchMap.put("sampleId", pu.getSampleId());
		return runDao.findByMap(searchMap);
	}
	
	@Override
	public Run initiateRun(String runName, Resource machineInstance, Sample platformUnit, User technician, String readLength, String readType, Date dateStart ) throws SampleTypeException{
		Assert.assertParameterNotNull(runName, "runName cannot be null");
		Assert.assertParameterNotNull(machineInstance, "machineInstance cannot be null");
		Assert.assertParameterNotNull(platformUnit, "platformUnit cannot be null");
		Assert.assertParameterNotNullNotZero(platformUnit.getSampleId(), "platformUnit sampleId is zero");
		Assert.assertParameterNotNull(technician, "technician cannot be null");
		Assert.assertParameterNotNullNotZero(technician.getUserId(), "technician UserId is zero");
		Assert.assertParameterNotNull(readLength, "readLength cannot be null");
		Assert.assertParameterNotNull(readType, "readType cannot be null");
		Assert.assertParameterNotNull(dateStart, "dateStart cannot be null");
		Run newRun = new Run();
		newRun.setResource(machineInstance);
		newRun.setName(runName.trim());
		newRun.setPlatformUnit(platformUnit);//set the flow cell
		newRun.setUser(technician);
		newRun.setStartts(dateStart);
		newRun = runDao.save(newRun);
		logger.debug("-----");
		logger.debug("saved new run runid=" + newRun.getRunId().intValue());
		//runmeta : readlength and readType
		RunMeta newRunMeta = new RunMeta();
		newRunMeta.setRun(newRun);
		newRunMeta.setK("run.readlength");
		newRunMeta.setV(readLength);
		newRunMeta.setPosition(new Integer(0));//do we really use this???
		newRunMeta = runMetaDao.save(newRunMeta);
		logger.debug("saved new run Meta for readLength id=" + newRunMeta.getRunMetaId().intValue());
		newRunMeta = new RunMeta();
		newRunMeta.setRun(newRun);
		newRunMeta.setK("run.readType");
		newRunMeta.setV(readType);
		newRunMeta.setPosition(new Integer(0));//do we really use this???
		newRunMeta = runMetaDao.save(newRunMeta);
		logger.debug("saved new run Meta for readType runmetaid=" + newRunMeta.getRunMetaId().intValue());	
		//runlane
		for(Sample cell: sampleService.getIndexedCellsOnPlatformUnit(platformUnit).values()){
			RunCell runCell = new RunCell();
			runCell.setRun(newRun);//the runid
			runCell.setSample(cell);//the cellid
			runCell = runCellDao.save(runCell);
			logger.debug("saved new run cell runcellid=" + runCell.getRunCellId().intValue());
		}
		logger.debug("-----");
		
		
		// send message to initiate job processing
		Map<String, String> jobParameters = new HashMap<String, String>();
		jobParameters.put(WaspJobParameters.RUN_ID, newRun.getRunId().toString() );
		jobParameters.put(WaspJobParameters.RUN_NAME, newRun.getName());
		String rcIname = newRun.getResourceCategory().getIName();
		for (BatchJobProviding plugin : waspPluginRegistry.getPluginsHandlingArea(rcIname, BatchJobProviding.class)) {
			// TODO: check the transactional behavior of this block when
			// one job launch fails after successfully sending another
			String flowName = plugin.getBatchJobNameByArea(BatchJobTask.GENERIC, rcIname);
			try {
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
		Assert.assertParameterNotNullNotZero(run.getRunId(), "runId is zero");
		Assert.assertParameterNotNull(runName, "runName cannot be null");
		Assert.assertParameterNotNull(machineInstance, "machineInstance cannot be null");
		Assert.assertParameterNotNull(platformUnit, "platformUnit cannot be null");
		Assert.assertParameterNotNullNotZero(platformUnit.getSampleId(), "platformUnit sampleId is zero");
		Assert.assertParameterNotNull(technician, "technician cannot be null");
		Assert.assertParameterNotNullNotZero(technician.getUserId(), "technician UserId is zero");
		Assert.assertParameterNotNull(readLength, "readLength cannot be null");
		Assert.assertParameterNotNull(readType, "readType cannot be null");
		Assert.assertParameterNotNull(dateStart, "dateStart cannot be null");
		run.setResource(machineInstance);
		run.setName(runName.trim());
		run.setPlatformUnit(platformUnit);
		run.setUser(technician);
		run.setStartts(dateStart);

		run = runDao.save(run);//since this object was pulled from the database, it is persistent, and any alterations are automatically updated; thus this line is superfluous
		
		List<RunMeta> runMetaList = run.getRunMeta();
		boolean readLengthIsSet = false;
		boolean readTypeIsSet = false;
		for(RunMeta rm : runMetaList){
			if(rm.getK().indexOf("readlength") > -1){
				rm.setV(readLength);
				runMetaDao.save(rm);//probably superfluous
				readLengthIsSet = true;
			}
			if(rm.getK().indexOf("readType") > -1){
				rm.setV(readType);
				runMetaDao.save(rm);//probably superfluous
				readTypeIsSet = true;
			}
		}
		if(readLengthIsSet == false){
			RunMeta newRunMeta = new RunMeta();
			newRunMeta.setRun(run);
			newRunMeta.setK("run.readlength");
			newRunMeta.setV(readLength);
			newRunMeta.setPosition(new Integer(0));//do we really use this???
			newRunMeta = runMetaDao.save(newRunMeta);
		}
		if(readTypeIsSet == false){
			RunMeta newRunMeta = new RunMeta();
			newRunMeta.setRun(run);
			newRunMeta.setK("run.readType");
			newRunMeta.setV(readType);
			newRunMeta.setPosition(new Integer(0));//do we really use this???
			newRunMeta = runMetaDao.save(newRunMeta);
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
		Set<Run> runs = new HashSet<Run>();
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> runIdStringSet = new HashSet<String>();
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
		Assert.assertParameterNotNull(run.getRunId(), "run must be defined");
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> runIdStringSet = new HashSet<String>();
		runIdStringSet.add(run.getRunId().toString());
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
		Set<Run> runs = new HashSet<Run>();
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new HashSet<String>();
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
	public boolean isRunSucessfullyCompleted(Run run){
		Assert.assertParameterNotNull(run, "run cannot be null");
		Assert.assertParameterNotNull(run.getRunId(), "run must be defined");
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> runIdStringSet = new HashSet<String>();
		runIdStringSet.add(run.getRunId().toString());
		parameterMap.put(WaspJobParameters.RUN_ID, runIdStringSet);
		if (! batchJobExplorer.getJobExecutions(parameterMap, true, ExitStatus.COMPLETED).isEmpty())
			return true;
		return false;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public Map<Sample, Job> getLibraryJobPairsOnSuccessfulRunCellsWithoutControls(Run run){
		Assert.assertParameterNotNull(run, "a run must be provided");
		Assert.assertParameterNotNullNotZero(run.getRunId(), "run provided is invalid or not in the database");
		Assert.assertParameterNotNull(run.getRunId(), "a runId must be have a valid database entry");
		Map<Sample, Job> libraryJob = new HashMap<Sample, Job>();
		try {
			for (Sample cell: sampleService.getIndexedCellsOnPlatformUnit(run.getPlatformUnit()).values()){
				if (sampleService.isCellSequencedSuccessfully(cell)){
					for (Sample library: sampleService.getLibrariesOnCellWithoutControls(cell)){
						try{
							libraryJob.put(library, sampleService.getJobOfLibraryOnCell(cell, library));
						} catch (SampleException e){
							logger.warn("Unexpected SampleException caught: " + e.getLocalizedMessage());
						}
					}
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
		Assert.assertParameterNotNullNotZero(run.getRunId(), "run provided is invalid or not in the database");
		Assert.assertParameterNotNull(run.getRunId(), "a runId must be have a valid database entry");
		Map<Sample, Job> libraryJob = new HashMap<Sample, Job>();
		try {
			for (Sample cell: sampleService.getIndexedCellsOnPlatformUnit(run.getPlatformUnit()).values()){
				if (sampleService.isCellSequencedSuccessfully(cell)){
					for (Sample library: sampleService.getLibrariesOnCell(cell)){
						try{
							libraryJob.put(library, sampleService.getJobOfLibraryOnCell(cell, library));
						} catch (SampleException e){
							logger.warn("Unexpected SampleException caught: " + e.getLocalizedMessage());
						}
					}
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
		Assert.assertParameterNotNullNotZero(run.getRunId(), "run provided is invalid or not in the database");
		Assert.assertParameterNotNull(run.getRunId(), "a runId must be have a valid database entry");
		Set<SampleSource> libraryCell = new HashSet<SampleSource>();
		try {
			for (Sample cell: sampleService.getIndexedCellsOnPlatformUnit(run.getPlatformUnit()).values()){
				if (sampleService.isCellSequencedSuccessfully(cell)){
					for (Sample library: sampleService.getLibrariesOnCellWithoutControls(cell)){
						try{
							libraryCell.add(sampleService.getCellLibrary(cell, library));
						} catch (SampleException e){
							logger.warn("Unexpected SampleException caught: " + e.getLocalizedMessage());
						}
					}
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
	public Set<SampleSource> getLibraryCellPairsOnSuccessfulRunCells(Run run){
		Assert.assertParameterNotNull(run, "a run must be provided");
		Assert.assertParameterNotNullNotZero(run.getRunId(), "run provided is invalid or not in the database");
		Assert.assertParameterNotNull(run.getRunId(), "a runId must be have a valid database entry");
		Set<SampleSource> libraryCell = new HashSet<SampleSource>();
		try {
			for (Sample cell: sampleService.getIndexedCellsOnPlatformUnit(run.getPlatformUnit()).values()){
				if (sampleService.isCellSequencedSuccessfully(cell)){
					for (Sample library: sampleService.getLibrariesOnCell(cell)){
						try{
							libraryCell.add(sampleService.getCellLibrary(cell, library));
						} catch (SampleException e){
							logger.warn("Unexpected SampleException caught: " + e.getLocalizedMessage());
						}
					}
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
		Assert.assertParameterNotNullNotZero(run.getRunId(), "run provided is invalid or not in the database");
		Set<Sample> librariesOnRun = new HashSet<Sample>();
		try {
			for (Sample cell: sampleService.getIndexedCellsOnPlatformUnit(run.getPlatformUnit()).values()){
				if (sampleService.isCellSequencedSuccessfully(cell))
					librariesOnRun.addAll(sampleService.getLibrariesOnCellWithoutControls(cell));	
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
		Assert.assertParameterNotNullNotZero(run.getRunId(), "run provided is invalid or not in the database");
		Set<Sample> librariesOnRun = new HashSet<Sample>();
		try {
			for (Sample cell: sampleService.getIndexedCellsOnPlatformUnit(run.getPlatformUnit()).values()){
				if (sampleService.isCellSequencedSuccessfully(cell))
					librariesOnRun.addAll(sampleService.getLibrariesOnCell(cell));	
			}
		} catch (SampleTypeException e) {
			logger.warn("Unexpected SampleTypeException caught: " + e.getLocalizedMessage());
		}
		return librariesOnRun;
	}
	

	

	

}
