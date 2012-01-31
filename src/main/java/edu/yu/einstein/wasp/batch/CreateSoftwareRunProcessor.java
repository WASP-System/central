package edu.yu.einstein.wasp.batch;

import java.util.Date;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.model.*;
import edu.yu.einstein.wasp.service.*;

/**
 * Create Software Run Processor
 *
 * creates a new run w/ for the sample/ job.software combination
 *	 - default values for name is job.name + software.name
 * creates a corresponding state for it w/ this state as a parent
 *	 that state will have jobid/sampleid and the new runid linked to it
 *	 state will have a task of 'runWrapTask' and status of 'CREATED'
 * 
 */

@Component
public class CreateSoftwareRunProcessor implements ItemProcessor {

	@Autowired
	StateService stateService;

	@Autowired
	TaskService taskService;

	@Autowired
	StatejobService statejobService;
	@Autowired
	StatesampleService statesampleService;
	@Autowired
	StaterunService staterunService;

	@Autowired
	RunService runService;

	@Autowired
	SoftwareService softwareService;

	protected final String targetTask = "runWrapTask"; 
	protected final String targetStatus = "CREATED"; 

	protected String softwareIName; 
	public void setSoftwareIName(String s) { this.softwareIName = s; }

	@Override
	public State process(Object stateId) throws Exception {

		
		State state = stateService.getStateByStateId(((Integer) stateId).intValue());

		List <JobSoftware> jobSoftwares = state.getStatejob().get(0).getJob().getJobSoftware();

		// finds the right software for typeresource
		Software software = null;
		for (JobSoftware js: jobSoftwares) {
		  if (softwareIName.equals(js.getSoftware().getTypeResource().getIName())) {
				software = js.getSoftware(); 
			}
		}

		if (software == null) {
			throw new Exception("could not find software");
		}

		Task task = taskService.getTaskByIName(targetTask);

		Sample sample = state.getStatesample().get(0).getSample(); 

		// Makes the run
		Run run = new Run();
		run.setSoftwareId(software.getSoftwareId());
		run.setName(sample.getName() + " " + software.getName());
		run.setSampleId(sample.getSampleId());	
		run.setStartts(new Date());
		run.setIsActive(1);

		runService.save(run); 


		// Makes the state and supporting tables
		State newState = new State(); 
		newState.setTaskId(task.getTaskId());
		newState.setStatus(targetStatus);
		newState.setName(sample.getName() + " " + software.getIName() + " " + task.getName());
		newState.setSourceStateId(state.stateId);
		stateService.save(newState); 

		Staterun newStaterun = new Staterun();
		newStaterun.setStateId(newState.getStateId());
		newStaterun.setRunId(run.getRunId());
		staterunService.save(newStaterun);

		Statesample newStatesample = new Statesample();
		newStatesample.setStateId(newState.getStateId());
		newStatesample.setSampleId(run.getSampleId());
		statesampleService.save(newStatesample);

		Statejob newStatejob = new Statejob();
		newStatejob.setStateId(newState.getStateId());
		newStatejob.setJobId(state.getStatejob().get(0).getJobId());
		statejobService.save(newStatejob);

		return newState;
	}
}

