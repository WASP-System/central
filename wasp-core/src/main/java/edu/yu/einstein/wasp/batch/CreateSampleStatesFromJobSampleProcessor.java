package edu.yu.einstein.wasp.batch;

import java.util.Date;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.StatejobDao;
import edu.yu.einstein.wasp.dao.StatesampleDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.Task;

/**
 * Created State Processor for each sample in jobsample for the state.statejob
 * simply creates a new State with targetTask and copies the original stated
 * from state Sample and creates a state sample out of it.
 * 
 */
@Component
public class CreateSampleStatesFromJobSampleProcessor implements ItemProcessor<Integer,State> {

	@Autowired
	StateDao stateDao;

	@Autowired
	TaskDao taskDao;

	@Autowired
	StatesampleDao statesampleDao;

	@Autowired
	StatejobDao statejobDao;

	String targetTask;

	public void setTargetTask(String targetTask) {
		this.targetTask = targetTask;
	}

	TaskStatus targetStatus = TaskStatus.CREATED;

	@Override
	public State process(Integer stateId) throws Exception {

		State state = stateDao.getStateByStateId(stateId.intValue());
		List<Statejob> stateJobs = state.getStatejob();

		Task t = taskDao.getTaskByIName(targetTask);

		for (Statejob sj : stateJobs) {
			for (JobSample js : sj.getJob().getJobSample()) {
				State newState = new State();
				newState.setStatus(targetStatus.toString());
				newState.setTaskId(t.getTaskId());
				newState.setSourceStateId(stateId);
				newState.setName(t.getName());
				newState.setStartts(new Date());
				State newStateDb = stateDao.save(newState);

				Statejob newStateJob = new Statejob();
				newStateJob.setStateId(newStateDb.getStateId());
				newStateJob.setJobId(sj.getJobId());

				statejobDao.save(newStateJob);
				Statesample newStateSample = new Statesample();
				newStateSample.setStateId(newStateDb.getStateId());
				newStateSample.setSampleId(js.getSampleId());

				statesampleDao.save(newStateSample);
			}
		}

		return state;
	}
}
