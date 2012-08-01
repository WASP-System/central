package edu.yu.einstein.wasp.batch;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.StatejobDao;
import edu.yu.einstein.wasp.dao.StatesampleDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * LinkLibrarySampleStateProcessor
 * for DNA sample traveses the SampleSource table to find 
 * dervived library for that job. 
 *
 * - creates a sampleWrapTask state corresponding
 *  statesample and statejob entires
 * 
 * 
 */

@Component
public class LinkLibrarySampleStateProcessor implements ItemProcessor {

	@Autowired
	StateDao stateDao;

	@Autowired
	StatesampleDao statesampleDao;

	@Autowired
	StatejobDao statejobDao;

	@Autowired
	JobSampleDao jobSampleDao;

	@Autowired
	TaskDao taskDao;

	@Autowired
	SampleService sampleService;

	private final Log logger = LogFactory.getLog(getClass());
	
	protected String targetTask; 
	public void setTargetTask(String targetTask) {
		this.targetTask = targetTask; 
	}

	protected String targetStatus; 
	public void setTargetStatus(String targetStatus) {
		this.targetStatus = targetStatus; 
	}

	@Override
	public State process(Object stateId) throws Exception {
		
		logger.debug("beginning of LinkLibrarySampleStateProcessor.process()");
		
		State state = stateDao.getStateByStateId(((Integer) stateId).intValue());
		List<Statesample> stateSamples = state.getStatesample();
		List<Statejob> stateJobs = state.getStatejob();

		Task t = taskDao.getTaskByIName(targetTask);

		// should be only one, but looping for good measure
		for (Statesample ss: stateSamples) {
			// finds the derived library
			//List<SampleSource> libSources =  ss.getSample().getSourceSampleId(); 
			Sample sample = ss.getSample();//macromoleculeSample I believe
			List<Sample> librarySamples = sampleService.getFacilityGeneratedLibraries(sample);
			//for (SampleSource libSource : libSources) {
			for(Sample librarySample : librarySamples){  //THIS IS VERY DANGEROUS!!!If it's not one, your're sunk!!
				//Sample librarySample =  libSource.getSample(); 

				// todo, next if not library 

				State newState = new State();
				newState.setStatus(targetStatus);
				newState.setTaskId(t.getTaskId());
				newState.setName(t.getName());

				// not the sample's stateId but the job's
				// newState.setSourceStateId((Integer) stateId);

				newState.setSourceStateId(state.getSourceStateId());

				newState.setStartts(new Date());
				State newStateDb = stateDao.save(newState);

				// links the state to the job
				// should be only one, but looping for good measure
				for (Statejob sj: stateJobs) {
					Statejob newStateJob = new Statejob();
					newStateJob.setStateId(newStateDb.getStateId());
					newStateJob.setJobId(sj.getJobId());
					statejobDao.save(newStateJob);

					// links job to the library via new jobsample; 6/8/12 this was moved back to controller SampleDNAToLibraryController.createLibrary where it really belongs, and removed from any batch responsibility
					//JobSample newJobSample = new JobSample();
					//newJobSample.setJobId(sj.getJobId());
					//newJobSample.setSampleId(librarySample.getSampleId());
					//jobSampleDao.save(newJobSample);

				}

				// links the state to the library
				Statesample newStateSample = new Statesample();
				newStateSample.setStateId(newStateDb.getStateId());
				newStateSample.setSampleId(librarySample.getSampleId());
				statesampleDao.save(newStateSample);


			}
		}

		logger.debug("end of LinkLibrarySampleStateProcessor.process()");
		return state;
	}
}

