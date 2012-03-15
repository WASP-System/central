package edu.yu.einstein.wasp.batch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.dao.StatejobDao;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;

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
	StatejobDao statejobDao;

	@Override
	public String process(Object stateId) throws Exception {

		State state = stateDao.getStateByStateId(((Integer) stateId).intValue());
		List <Statejob> statejob = state.getStatejob();

		// TODO npe check

		Map m = new HashMap();
		m.put("jobId", statejob.get(0).getJobId());
		List<Statejob> siblingStateEntities= statejobDao.findByMap(m);

		return handleSiblings(siblingStateEntities);

	}
}

