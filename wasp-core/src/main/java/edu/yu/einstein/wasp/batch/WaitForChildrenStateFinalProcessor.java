package edu.yu.einstein.wasp.batch;

import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.model.State;

/**
 * Wait for Wait For Sample File Processor
 * 
 * polls state.sample.getFile() 
 * if a file matches the filenameRegex progress, 
 * otherwise throw an exception
 * 
 */

@Component
public class WaitForChildrenStateFinalProcessor implements ItemProcessor {

	@Autowired
	StateDao stateDao;

	@Override
	public State process(Object stateId) throws Exception {	
		State state = stateDao.getStateByStateId(((Integer) stateId).intValue());
		// TODO npe check
		List<State> childrenState = state.getStateViaSourceStateId();
		for (State childState: childrenState) {
			if (! childState.getStatus().equals(TaskStatus.FINALIZED.toString())) {
				throw new RetryableException("sub tasks not done yet" + childState.getStateId() + " " + childState.getName());
			}
		}
		return state;
	}
}

