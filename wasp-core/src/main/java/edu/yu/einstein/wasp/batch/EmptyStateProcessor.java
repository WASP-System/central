package edu.yu.einstein.wasp.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.service.StateService;

/**
 * Empty State Processor
 * - looks up state for stateid and then does nothing... 
 * - mainly used for parentStateStep
 */

@Component
public class EmptyStateProcessor implements ItemProcessor {

	@Autowired
	StateService stateService;

	@Override
	public State process(Object stateId) throws Exception {
		
		State state = stateService.getStateByStateId(((Integer) stateId).intValue());
		return state;
	}
}

