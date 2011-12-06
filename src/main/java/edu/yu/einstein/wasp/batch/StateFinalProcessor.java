package edu.yu.einstein.wasp.batch;

import edu.yu.einstein.wasp.model.*;
import edu.yu.einstein.wasp.service.*;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.support.transaction.FlushFailedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * for a given state
 *   set its status to final
 *
 */

@Component
public class StateFinalProcessor implements ItemProcessor {

  @Autowired
  StateService stateService;

  public State process(Object stateId) throws Exception {
    State state = stateService.getStateByStateId(((Integer) stateId).intValue());
    state.setStatus("FINAL");
    stateService.save(state);

    return state;
  }
}

