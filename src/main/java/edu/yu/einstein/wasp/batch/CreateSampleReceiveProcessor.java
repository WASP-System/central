package edu.yu.einstein.wasp.batch;

import java.util.Date;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.service.StatejobService;
import edu.yu.einstein.wasp.service.StatesampleService;
import edu.yu.einstein.wasp.service.TaskService;

/**
 * Created Sample Receive Processor
 * simply creates a new State with targetTask and copies
 * the original jobid from state Job and creates a state job out of it.
 * 
 */

@Component
public class CreateSampleReceiveProcessor implements ItemProcessor {

	@Autowired
	StateService stateService;

	@Autowired
	TaskService taskService;

	@Autowired
	StatejobService statejobService;

	@Autowired
	StatesampleService statesampleService;

	String targetTask; 
	public void setTargetTask(String targetTask) {
		this.targetTask = targetTask; 
	}

	String targetStatus = "CREATED"; 

	@Override
	public State process(Object stateId) throws Exception {
		
		State state = stateService.getStateByStateId(((Integer) stateId).intValue());

		List<Statejob> stateJobs = state.getStatejob();

		Task t = taskService.getTaskByIName(targetTask); 


		for (Statejob sj: stateJobs) {
			for (Sample s: sj.getJob().getSample()) {

				State newState = new State();
				newState.setStatus(targetStatus);
				newState.setTaskId(t.getTaskId());
				newState.setName(t.getName());
				newState.setStartts(new Date());
				State newStateDb = stateService.save(newState);

				Statejob newStateJob = new Statejob(); 
				newStateJob.setStateId(newStateDb.getStateId());
				newStateJob.setJobId(sj.getJobId());

				statejobService.save(newStateJob);

				Statesample newStatesample = new Statesample(); 
				newStatesample.setStateId(newStateDb.getStateId());
				newStatesample.setSampleId(s.getSampleId());

				statesampleService.save(newStatesample);
			}

		}

		return state;
	}
}

