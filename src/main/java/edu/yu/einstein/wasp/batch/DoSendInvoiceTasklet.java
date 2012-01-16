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
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import util.spring.PostInitialize;

/**
 * DoSendInvoiceStateProcessor
 * sends an invoice to proper party
 *
 * ** **  currently test to echo hello world via System.exec
 *
 * throws an retryable exception unless
 * a sibling state for that job has hit a status of property status 
 * if it does, update that sibling status w/ a new target status
 *
 * this only supports the first task of the job, anticipated
 * rewrite to take a mapping of types, list of statuses, etc.
 * 
 */

@Component
public class DoSendInvoiceTasklet extends org.springframework.batch.core.step.tasklet.SystemCommandTasklet implements ItemProcessor {

	@Autowired
	StateService stateService;

	@Autowired
	StatejobService statejobService;

	List<String> params;
	public void setParams(List<String> s) { 
		this.params = s; 
		this.setCommand(org.springframework.util.StringUtils.collectionToDelimitedString(params, " "));
	}
	public List<String> getParams() { return this.params;}

	// afterPropertySet to reassemble the properties
	@PostInitialize
	void postInitialize() {
		this.setCommand(org.springframework.util.StringUtils.collectionToDelimitedString(params, " "));

	}


	public State process(Object stateId) throws Exception {
		
		State state = stateService.getStateByStateId(((Integer) stateId).intValue());
		return state;

	}

/*

	String task; 
	public void setTask(String task) {
		this.task = task; 
	}

	String status; 
	public void setStatus(String status) {
		this.status = status; 
	}

	String siblingTargetStatus; 
	public void setSiblingTargetStatus (String siblingTargetStatus) {
		this.siblingTargetStatus = siblingTargetStatus; 
	}


	public State process(Object stateId) throws Exception {
		
		State state = stateService.getStateByStateId(((Integer) stateId).intValue());
		List <Statejob> statejob = state.getStatejob();

		// TODO npe check


		Map m = new HashMap(); 
		m.put("jobId", statejob.get(0).getJobId());
		List <Statejob> siblingStatejobs = statejobService.findByMap(m);

		for (Statejob siblingStatejob: siblingStatejobs) {
			if (! siblingStatejob.getState().getTask().getIName().equals(task)) {
				continue; 
			}

			// "FINAL" status, means it has already been run
			if (siblingStatejob.getState().getStatus().equals(siblingTargetStatus != null?siblingTargetStatus:"FINAL")) {
				return state;
			}

			if (! siblingStatejob.getState().getStatus().equals(status)) {
				continue; 
			}


			if (siblingTargetStatus != null) {
				State siblingState = stateService.findById(siblingStatejob.getStateId());
				siblingState.setStatus(siblingTargetStatus);
				siblingState.setEndts(new Date());
				stateService.save(siblingState);
			}


			return state;
		}


		throw new RetryableException("No Task " + task + " yet, currently at " + status + ".");


	}
*/
}

