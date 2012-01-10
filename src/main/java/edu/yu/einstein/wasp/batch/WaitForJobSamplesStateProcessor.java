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

/**
 * Wait for Child Sample State
 * 
 * when all the child sample states hit a certain status
 * advance the child sample states and the job state
 * otherwise throws an retryable exception unless
 * 
 */

@Component
public class WaitForJobSamplesStateProcessor implements ItemProcessor {

	@Autowired
	StateService stateService;

	@Autowired
	StatesampleService statesampleService;

	@Autowired
	StatejobService statejobService;

	String jobTask; 
	public void setJobTask(String jobTask) {
		this.jobTask = jobTask; 
	}
	String sampleTask; 
	public void setSampleTask(String sampleTask) {
		this.sampleTask = sampleTask; 
	}

	String jobStatus; 
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus; 
	}
	String sampleStatus; 
	public void setSampleStatus(String sampleStatus) {
		this.sampleStatus = sampleStatus; 
	}

	String targetJobStatus; 
	public void setTargetJobStatus(String targetJobStatus) {
		this.targetJobStatus = targetJobStatus; 
	}
	String targetSampleStatus; 
	public void setTargetSampleStatus(String targetSampleStatus) {
		this.targetSampleStatus = targetSampleStatus; 
	}

	public State process(Object stateId) throws Exception {	
		State state = stateService.getStateByStateId(((Integer) stateId).intValue());
		// TODO npe check

		// check job state
		if (! state.getStatus().equals(jobStatus)) {
			throw new RetryableException("Job Task not yet at " + jobStatus); 
		}

		List <Statejob> statejob = state.getStatejob();
		Map m = new HashMap(); 
		m.put("jobId", statejob.get(0).getJobId());

		// 
		List <Statejob> siblingStatejobs = statejobService.findByMap(m);

		boolean foundOther = false; 
		for (Statejob siblingStatejob: siblingStatejobs) {
			if (! siblingStatejob.getState().getTask().getIName().equals(sampleTask)) {
				continue; 
			}

			// "FINAL" status, means it has already been run
			if (! siblingStatejob.getState().getStatus().equals(sampleStatus != null?sampleStatus:"FINAL")) {
				foundOther = true;
				continue;
			}
		}

		if (foundOther)	{
			throw new RetryableException("Sample Task " + sampleTask + " not all currently at " + sampleStatus + ".");
		} 

		if (targetSampleStatus != null) {
			for (Statejob siblingStatejob: siblingStatejobs) {
				if (! siblingStatejob.getState().getTask().getIName().equals(sampleTask)) {
					continue; 
				}
				State siblingState = stateService.findById(siblingStatejob.getStateId());
				siblingState.setStatus(targetSampleStatus);
				siblingState.setEndts(new Date());
				stateService.save(siblingState);
			}
		}

		// updates the current job
		if (targetJobStatus != null) {
			state.setStatus(targetJobStatus);
			state.setEndts(new Date());
			stateService.save(state);
		}

		return state;

	}
}

