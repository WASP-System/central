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
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.service.StatesampleService;

/**
 * Wait for State
 * throws an retryable exception unless
 * a sibling state for that sample has hit a status of property status
 * if it does, update that sibling status w/ a new target status
 *
 * this only supports the first task of the sample, anticipated
 * rewrite to take a mapping of types, list of statuses, etc.
 *
 */

@Component
public class WaitForSiblingSampleStateProcessor extends WaspWaitForSiblingStateProcessor<Statesample> {

	@Autowired
	StatesampleService statesampleService;

	@Override
	public String process(Object stateId) throws Exception {

		State state = stateService.getStateByStateId(((Integer) stateId).intValue());
		List <Statesample> statesample = state.getStatesample();

		// TODO npe check

		Map m = new HashMap();
		m.put("sampleId", statesample.get(0).getSampleId());
		List<Statesample> siblingStateEntities= statesampleService.findByMap(m);

		return handleSiblings(siblingStateEntities);

	}
}

