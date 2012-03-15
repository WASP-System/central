package edu.yu.einstein.wasp.batch;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.StateEntity;

/**
 * Wait for State
 * throws an retryable exception unless
 * a sibling state for that Entity has hit a status of property status 
 * if it does, update that sibling status w/ a new target status
 *
 * this only supports the first task of the Entity, anticipated
 * rewrite to take a mapping of types, list of statuses, etc.
 * 
 */

@Component
public abstract class WaspWaitForSiblingStateProcessor<E extends StateEntity> implements ItemProcessor, InitializingBean { 
// , ItemProcessListener<State, String> {

	private static final Log log = LogFactory.getLog(WaspWaitForSiblingStateProcessor.class);

	@Autowired
	protected StateDao stateDao;

	protected String task; 
	public void setTask(String task) {
		this.task = task; 
	}

	protected Map<String, String> statusMap;
	public void setStatusMap(Map<String, String> statusMap) {
		this.statusMap = statusMap; 
	}

	protected Map<String, String> spelMap;
	public void setSpelMap(Map<String, String> spelMap) {
		this.spelMap = spelMap; 
	}

	String status; 
	public void setStatus(String status) {
		this.status = status; 
	}

	String siblingTargetStatus; 
	public void setSiblingTargetStatus (String siblingTargetStatus) {
		this.siblingTargetStatus = siblingTargetStatus; 
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		if (statusMap == null || statusMap.size() == 0) {
			statusMap = new HashMap<String, String>();
			statusMap.put(this.status, this.siblingTargetStatus);
		}
	}

	protected StepExecution stepExecution;

	@Override
	public String process(Object stateId) throws Exception {
		throw new Exception("Process not defined");
	}

	public String handleSiblings(List<E> siblingStateEntities) throws Exception {
		for (E siblingStateEntity: siblingStateEntities) {

			// sibling not of the right task
			if (! siblingStateEntity.getState().getTask().getIName().equals(task)) {
				continue; 
			}

			siblingTargetStatus = null;

			Map m = new HashMap();
			// State siblingState =  siblingStateEntity.getState();
			State siblingState = stateDao.findById(siblingStateEntity.getStateId());
			// m.put("state", siblingState);

			StandardEvaluationContext context = new StandardEvaluationContext();
			context.setVariable("state", siblingState);

			ExpressionParser parser = new SpelExpressionParser();

			if (spelMap != null) {
				for (String spel: spelMap.keySet()) {
					try {
						Boolean b = (Boolean) parser.parseExpression(spel).getValue(context);
						if (! b) { continue; }
						siblingTargetStatus = spelMap.get(spel);


					} catch (Exception e) {
						log.info("Spel Exeption" + spel + " / " + e);
						continue;
					}
				}
			}


			// sibling of the right task, but not in the status map
			if (siblingTargetStatus == null) {
				if (! statusMap.containsKey(siblingStateEntity.getState().getStatus())) {
					continue; 
				}

				siblingTargetStatus = statusMap.get(siblingStateEntity.getState().getStatus());
			}

			if (siblingTargetStatus != null) {
				siblingState.setStatus(siblingTargetStatus);
				siblingState.setEndts(new Date());
				stateDao.save(siblingState);

//				this.stepExecution.setExitStatus(new ExitStatus(siblingTargetStatus)); 
				return siblingTargetStatus;
			}


		}


		throw new RetryableException("No Task " + task + " yet, currently");
	}


	/**
	 * saves the execution context so an exit status can be writen to it later.
	 * (and then some)
	 *
	 */
/*
	public void beforeStep(StepExecution stepExecution) {

		System.out.println("\n\n\n\n\n BEFORE STEP \n\n\n\n");
		this.stepExecution = stepExecution;
	}

	public ExitStatus afterStep(StepExecution stepExecution) {
		System.out.println("\n\n\n\n\n AFTER STEP \n\n\n\n");
		return new ExitStatus("XXXXXX " + siblingTargetStatus);
	}

*/



/*
	public void beforeProcess(State state) {
		System.out.println("\n\n\n\n\n BEFORE PROCESS \n\n\n\n");
	}
	public void afterProcess(State state, String targetStatus) {
		System.out.println("\n\n\n\n\n AFTER PROCESS \n\n\n\n");
		this.siblingTargetStatus = targetStatus;
	}
	public void onProcessError(State state, Exception e) {
		System.out.println("\n\n\n\n\n on PROCESS Error \n\n\n\n");
	} 
*/

}


