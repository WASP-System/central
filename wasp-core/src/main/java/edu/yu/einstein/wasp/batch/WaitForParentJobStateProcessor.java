package edu.yu.einstein.wasp.batch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.StatejobDao;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;

/**
 * Wait for Parent Job State
 * takes in a sample based state w/ a job tied to it
 * itereates throught parent's state looking for an Task/Status
 * combination.
 *
 * other throws an retryable exception
 */

@Component
public class WaitForParentJobStateProcessor implements ItemProcessor {

	@Autowired
	StateDao stateDao;

	@Autowired
	StatejobDao statejobDao;

	String task; 
	public void setTask(String task) {
		this.task = task; 
	}

	String status; 
	public void setStatus(String status) {
		this.status = status; 
	}

	@Override
	public State process(Object stateId) throws Exception {
		
		State state = stateDao.getStateByStateId(((Integer) stateId).intValue());
		List <Statejob> statejob = state.getStatejob();

		// TODO npe check
		Map m = new HashMap(); 
		m.put("jobId", statejob.get(0).getJobId());

		// find all the states by for the job
		List <Statejob> siblingStatejobs = statejobDao.findByMap(m);

		for (Statejob stateJob: siblingStatejobs) {

			// am i the right task?
			if (! stateJob.getState().getTask().getIName().equals(task)) {
				continue; 
			}

			if (! stateJob.getState().getStatus().equals(status)) {
				continue; 
			}

			return state;
		}


		throw new RetryableException("No Task " + task + " yet, currently at " + status + ".");

	}
}

