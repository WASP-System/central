/**
 * 
 */
package edu.yu.einstein.wasp.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.dao.RunCellDao;
import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.dao.RunMetaDao;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunCell;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.WorkflowService;

/**
 * @author calder
 *
 */
@Service
@Transactional
public class RunServiceImpl extends WaspMessageHandlingServiceImpl implements RunService {
	
	private static Logger logger = LoggerFactory.getLogger(RunServiceImpl.class);
	
	private RunDao runDao;
	
	@Autowired
	SampleService sampleService;
	
	@Autowired
	WorkflowService workflowService;
	

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

	

	
	
	@PostConstruct
	@Override
	protected void initialize() {
		// need to initialize the message channels
		super.initialize();
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
	public Run initiateRun(String runName, Resource machineInstance, Sample platformUnit, User technician, String readLength, String readType, Date dateStart ) throws SampleTypeException, WaspMessageBuildingException{
		Assert.assertParameterNotNull(runName, "runName cannot be null");
		Assert.assertParameterNotNull(machineInstance, "machineInstance cannot be null");
		Assert.assertParameterNotNull(platformUnit, "platformUnit cannot be null");
		Assert.assertParameterNotNullNotZero(platformUnit.getSampleId(), "platformUnit sampleId is zero");
		Assert.assertParameterNotNull(technician, "technician cannot be null");
		Assert.assertParameterNotNullNotZero(technician.getUserId(), "technician userId is zero");
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
		jobParameters.put(WaspJobParameters.PLATFORM_UNIT_ID, platformUnit.getSampleId().toString());
		BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( new BatchJobLaunchContext("wasp.run.jobflow", jobParameters) );
		sendOutboundMessage(batchJobLaunchMessageTemplate.build());
		return newRun;
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
		Assert.assertParameterNotNullNotZero(technician.getUserId(), "technician userId is zero");
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

}
