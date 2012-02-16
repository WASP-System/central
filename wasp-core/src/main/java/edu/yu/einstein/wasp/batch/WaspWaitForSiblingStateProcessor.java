package edu.yu.einstein.wasp.batch;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ExitStatus;

import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.StateEntity;
import edu.yu.einstein.wasp.service.StateService;

import org.springframework.beans.factory.InitializingBean;

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
public class WaspWaitForSiblingStateProcessor<E extends StateEntity> implements ItemProcessor, InitializingBean { 
// , ItemProcessListener<State, String> {

	@Autowired
	protected StateService stateService;

	protected String task; 
	public void setTask(String task) {
		this.task = task; 
	}

	protected Map<String, String> statusMap;
	public void setStatusMap(Map<String, String> statusMap) {
		this.statusMap = statusMap; 
	}

	String status; 
	public void setStatus(String status) {
		this.status = status; 
	}

	String siblingTargetStatus; 
	public void setSiblingTargetStatus (String siblingTargetStatus) {
		this.siblingTargetStatus = siblingTargetStatus; 
	}


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


			// sibling of the right task, but not in the status map
			if (! statusMap.containsKey(siblingStateEntity.getState().getStatus())) {
				continue; 
			}

			siblingTargetStatus = statusMap.get(siblingStateEntity.getState().getStatus());

//		if (siblingTargetStatus != null) {
				State siblingState = stateService.findById(siblingStateEntity.getStateId());
				siblingState.setStatus(siblingTargetStatus);
				siblingState.setEndts(new Date());
				stateService.save(siblingState);

//			this.stepExecution.setExitStatus(new ExitStatus(siblingTargetStatus)); 
//		}


			return siblingTargetStatus;
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


