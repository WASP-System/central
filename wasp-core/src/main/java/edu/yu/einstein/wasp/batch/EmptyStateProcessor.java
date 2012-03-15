package edu.yu.einstein.wasp.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.model.State;

/**
 * Empty State Processor
 * - looks up state for stateid and then does nothing... 
 * - mainly used for parentStateStep
 */

@Component
public class EmptyStateProcessor implements ItemProcessor {

	@Autowired
	StateDao stateDao;

	@Override
	public State process(Object stateId) throws Exception {
		
		State state = stateDao.getStateByStateId(((Integer) stateId).intValue());
		return state;
	}
}

