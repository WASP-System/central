package edu.yu.einstein.wasp.batch;

import java.util.Date;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.service.StateService;

/**
 * for a given state
 *   set its status to final
 *
 */

@Component
public class StateFinalProcessor implements ItemProcessor {

  @Autowired
  StateService stateService;

  @Override
public State process(Object stateId) throws Exception {
    State state = stateService.getStateByStateId(((Integer) stateId).intValue());
    state.setStatus("FINAL");
    state.setEndts(new Date());
    stateService.save(state);

    return state;
  }
}

