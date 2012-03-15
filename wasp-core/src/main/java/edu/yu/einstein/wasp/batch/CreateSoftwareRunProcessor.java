package edu.yu.einstein.wasp.batch;

import java.util.Date;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.dao.SoftwareDao;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.StatejobDao;
import edu.yu.einstein.wasp.dao.StaterunDao;
import edu.yu.einstein.wasp.dao.StatesampleDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.model.JobSoftware;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.Staterun;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.Task;


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
	StateDao stateDao;

	@Autowired
	TaskDao taskDao;

	@Autowired
	StatejobDao statejobDao;
	@Autowired
	StatesampleDao statesampleDao;
	@Autowired
	StaterunDao staterunDao;

	@Autowired
	RunDao runDao;

	@Autowired
	SoftwareDao softwareDao;

	protected final String targetTask = "runWrapTask"; 
	protected final TaskStatus targetStatus = TaskStatus.CREATED; 

	protected String softwareIName; 
	public void setSoftwareIName(String s) { this.softwareIName = s; }

	@Override
	public State process(Object stateId) throws Exception {

		
		State state = stateDao.getStateByStateId(((Integer) stateId).intValue());

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

		Task task = taskDao.getTaskByIName(targetTask);

		Sample sample = state.getStatesample().get(0).getSample(); 

		// Makes the run
		Run run = new Run();
		run.setSoftwareId(software.getSoftwareId());
		run.setName(sample.getName() + " " + software.getName());
		run.setSampleId(sample.getSampleId());	
		run.setStartts(new Date());
		run.setIsActive(1);

		runDao.save(run); 


		// Makes the state and supporting tables
		State newState = new State(); 
		newState.setTaskId(task.getTaskId());
		newState.setStatus(targetStatus.toString());
		newState.setName(sample.getName() + " " + software.getIName() + " " + task.getName());
		newState.setSourceStateId(state.getStateId());
		stateDao.save(newState); 

		Staterun newStaterun = new Staterun();
		newStaterun.setStateId(newState.getStateId());
		newStaterun.setRunId(run.getRunId());
		staterunDao.save(newStaterun);

		Statesample newStatesample = new Statesample();
		newStatesample.setStateId(newState.getStateId());
		newStatesample.setSampleId(run.getSampleId());
		statesampleDao.save(newStatesample);

		Statejob newStatejob = new Statejob();
		newStatejob.setStateId(newState.getStateId());
		newStatejob.setJobId(state.getStatejob().get(0).getJobId());
		statejobDao.save(newStatejob);

		return newState;
	}
}

