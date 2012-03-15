package edu.yu.einstein.wasp.batch;

import java.util.Date;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.model.State;

/**
 * for a given state
 *   set its status to final
 *
 */

@Component
public class StateFinalProcessor implements ItemProcessor {

  @Autowired
  StateDao stateDao;

  @Override
public State process(Object stateId) throws Exception {
    State state = stateDao.getStateByStateId(((Integer) stateId).intValue());
    state.setStatus(TaskStatus.FINALIZED.toString());
    state.setEndts(new Date());
    stateDao.save(state);

    return state;
  }
}

