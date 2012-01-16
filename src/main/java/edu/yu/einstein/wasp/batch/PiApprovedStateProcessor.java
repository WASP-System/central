package edu.yu.einstein.wasp.batch;

import java.util.Date;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.service.StatejobService;
import edu.yu.einstein.wasp.service.TaskService;

/**
 * Pi Approved Processor
 * simply creates a new State with targetTask and copies
 * the original jobid from state Job and creates a state job out of it.
 * 
 */

@Component
public class PiApprovedStateProcessor implements ItemProcessor {

	@Autowired
	StateService stateService;

	@Autowired
	TaskService taskService;

	@Autowired
	StatejobService statejobService;

	String otherTask; 
	public void setOtherTask(String otherTask) {
		this.otherTask = otherTask; 
	}

	String otherStatus; 
	public void setOtherStatus(String otherStatus) {
		this.otherStatus = otherStatus; 
	}

	String targetTask; 
	public void setTargetTask(String targetTask) {
		this.targetTask = targetTask; 
	}


	String targetStatus = "CREATED"; 

	@Override
	public State process(Object stateId) throws Exception {
		
		State state = stateService.getStateByStateId(((Integer) stateId).intValue());

		List<Statejob> stateJobs = state.getStatejob();

		// should be on to one.
		for (Statejob sj: stateJobs) {
			for (Statejob siblingStateJob: sj.getJob().getStatejob()) {
				State siblingState = siblingStateJob.getState();

				// looks for one of a "other task" approved
				if (! siblingState.getTask().getIName().equals(otherTask)) {
					continue; 
				}
				if (! siblingState.getStatus().equals(otherStatus)) {
					continue; 
				}

				Task t = taskService.getTaskByIName(targetTask);


				// sets this state to final
				state.setStatus("FINAL");
				state.setEndts(new Date());
				stateService.save(state);

				// sets this state to final
				siblingState.setStatus("FINAL");
				siblingState.setEndts(new Date());
				stateService.save(siblingState);


				// create a new state
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

				// break;
				return state;
			}
		}

		return state;
	}

}

