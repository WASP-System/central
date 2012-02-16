package edu.yu.einstein.wasp.batch;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ExitStatus;

import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.service.StatejobService;

/**
 * Wait for State
 * throws an retryable exception unless
 * a sibling state for that job has hit a status of property status
 * if it does, update that sibling status w/ a new target status
 *
 * this only supports the first task of the job, anticipated
 * rewrite to take a mapping of types, list of statuses, etc.
 *
 */

@Component
public class WaitForSiblingJobStateProcessor extends WaspWaitForSiblingStateProcessor<Statejob> {

	@Autowired
	StatejobService statejobService;

	@Override
	public String process(Object stateId) throws Exception {

		State state = stateService.getStateByStateId(((Integer) stateId).intValue());
		List <Statejob> statejob = state.getStatejob();

		// TODO npe check

		Map m = new HashMap();
		m.put("jobId", statejob.get(0).getJobId());
		List<Statejob> siblingStateEntities= statejobService.findByMap(m);

		return handleSiblings(siblingStateEntities);

	}
}

