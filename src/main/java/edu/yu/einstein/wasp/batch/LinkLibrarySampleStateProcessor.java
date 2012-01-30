package edu.yu.einstein.wasp.batch;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.service.StatejobService;
import edu.yu.einstein.wasp.service.StatesampleService;

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
	StateService stateService;

	@Autowired
	StatesampleService statesampleService;

	@Autowired
	StatejobService statejobService;

	@Autowired
	TaskService taskService;

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
		
		State state = stateService.getStateByStateId(((Integer) stateId).intValue());
		List<Statesample> stateSamples = state.getStatesample();
		List<Statejob> stateJobs = state.getStatejob();

		Task t = taskService.getTaskByIName(targetTask);

		// should be only one, but looping for good measure
		for (Statesample ss: stateSamples) {
			// finds the derived library
			List<SampleSource> libSources =  ss.getSample().getSampleSourceViaSourceSampleId(); 
			for (SampleSource libSource : libSources) {
				Sample librarySample =  libSource.getSample(); 

				// todo, next if not library 

				State newState = new State();
				newState.setStatus(targetStatus);
				newState.setTaskId(t.getTaskId());
				newState.setName(t.getName());

				// not the sample's stateId but the job's
				// newState.setSourceStateId((Integer) stateId);

				newState.setSourceStateId(state.getSourceStateId());

				newState.setStartts(new Date());
				State newStateDb = stateService.save(newState);

				// links the state to the job
				// should be only one, but looping for good measure
				for (Statejob sj: stateJobs) {
					Statejob newStateJob = new Statejob();
					newStateJob.setStateId(newStateDb.getStateId());
					newStateJob.setJobId(sj.getJobId());
					statejobService.save(newStateJob);
				}

				// links the state to the library
			      	Statesample newStateSample = new Statesample();
				newStateSample.setStateId(newStateDb.getStateId());
				newStateSample.setSampleId(librarySample.getSampleId());
				statesampleService.save(newStateSample);
			}
		}


		return state;
	}
}

