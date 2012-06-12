package edu.yu.einstein.wasp.batch;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.dao.StatesampleDao;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statesample;

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
	StatesampleDao statesampleDao;
	 
	private final Log logger = LogFactory.getLog(getClass());
	 
	@Override
	public String process(Object stateId) throws Exception {

		State state = stateDao.getStateByStateId(((Integer) stateId).intValue());
		List <Statesample> statesample = state.getStatesample();

	    java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    java.util.Date date = new Date();
		logger.debug("ROB --beginning of WaitForSiblingSampleStateProcessor.process() with state / stateId of " + state.getName() + " / " + state.getStateId().intValue() + " at: " + dateFormat.format(date));

		
		// TODO npe check

		Map m = new HashMap();
		m.put("sampleId", statesample.get(0).getSampleId());
		List<Statesample> siblingStateEntities= statesampleDao.findByMap(m);

		return handleSiblings(siblingStateEntities);

	}
}

