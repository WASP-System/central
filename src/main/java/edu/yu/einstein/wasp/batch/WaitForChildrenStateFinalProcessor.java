package edu.yu.einstein.wasp.batch;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.SampleFile;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.service.StateService;

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
	StateService stateService;

	@Override
	public State process(Object stateId) throws Exception {	
		State state = stateService.getStateByStateId(((Integer) stateId).intValue());
		// TODO npe check
		List<State> childrenState = state.getStateViaSourceStateId();
		for (State childState: childrenState) {
			if (! childState.getStatus().equals("FINAL")) {
				throw new RetryableException("sub tasks not done yet" + childState.getStateId() + " " + childState.getName());
			}
		}
		return state;
	}
}

