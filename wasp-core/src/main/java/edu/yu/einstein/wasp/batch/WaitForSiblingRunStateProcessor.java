package edu.yu.einstein.wasp.batch;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Staterun;
import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.service.StaterunService;

/**
 * Wait for State
 * throws an retryable exception unless
 * a sibling state for that run has hit a status of property status 
 * if it does, update that sibling status w/ a new target status
 *
 * this only supports the first task of the run, anticipated
 * rewrite to take a mapping of types, list of statuses, etc.
 * 
 */

@Component
public class WaitForSiblingRunStateProcessor implements ItemProcessor {

	@Autowired
	StateService stateService;

	@Autowired
	StaterunService staterunService;

	String task; 
	public void setTask(String task) {
		this.task = task; 
	}

	String status; 
	public void setStatus(String status) {
		this.status = status; 
	}

	String siblingTargetStatus; 
	public void setSiblingTargetStatus (String siblingTargetStatus) {
		this.siblingTargetStatus = siblingTargetStatus; 
	}


	@Override
	public State process(Object stateId) throws Exception {
		
		State state = stateService.getStateByStateId(((Integer) stateId).intValue());
		List <Staterun> staterun = state.getStaterun();

		// TODO npe check


		Map m = new HashMap(); 
		m.put("runId", staterun.get(0).getRunId());
		List <Staterun> siblingStateruns = staterunService.findByMap(m);

		for (Staterun siblingStaterun: siblingStateruns) {
			if (! siblingStaterun.getState().getTask().getIName().equals(task)) {
				continue; 
			}

			// "FINAL" status, means it has already been run
			if (siblingStaterun.getState().getStatus().equals(siblingTargetStatus != null?siblingTargetStatus:"FINAL")) {
				return state;
			}

			if (! siblingStaterun.getState().getStatus().equals(status)) {
				continue; 
			}

			if (siblingTargetStatus != null) {
				State siblingState = stateService.findById(siblingStaterun.getStateId());
				siblingState.setStatus(siblingTargetStatus);
				siblingState.setEndts(new Date());
				stateService.save(siblingState);
			}


			return state;
		}


		throw new RetryableException("No Task " + task + " yet, currently at " + status + ".");


	}
}

