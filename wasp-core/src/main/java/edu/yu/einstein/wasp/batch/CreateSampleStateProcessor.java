package edu.yu.einstein.wasp.batch;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.cli.Main;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.StatejobDao;
import edu.yu.einstein.wasp.dao.StaterunDao;
import edu.yu.einstein.wasp.dao.StatesampleDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.Staterun;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.Task;

/**
 * Created State Processor simply creates a new State with targetTask and copies
 * the original stated from state Sample and creates a state sample out of it.
 * 
 */

@Component
public class CreateSampleStateProcessor implements ItemProcessor {

	private final static Log logger = LogFactory.getLog(CreateSampleStateProcessor.class);

	@Autowired
	StateDao stateDao;

	@Autowired
	TaskDao taskDao;

	@Autowired
	StatesampleDao statesampleDao;

	@Autowired
	StatejobDao statejobDao;

	@Autowired
	StaterunDao staterunDao;

	String targetTask;

	public void setTargetTask(String targetTask) {
		this.targetTask = targetTask;
	}

	TaskStatus targetStatus = TaskStatus.CREATED;

	@Override
	public State process(Object stateId) throws Exception {

		logger.debug("debug");

		State state = stateDao.getStateByStateId(((Integer) stateId).intValue());

		List<Statesample> stateSamples = state.getStatesample();
		List<Statejob> stateJobs = state.getStatejob();
		List<Staterun> stateRuns = state.getStaterun();

		Task t = taskDao.getTaskByIName(targetTask);

		State newState = new State();
		newState.setStatus(targetStatus.toString());
		newState.setTaskId(t.getTaskId());
		newState.setName(t.getName());
		newState.setSourceStateId((Integer) stateId);
		newState.setStartts(new Date());
		State newStateDb = stateDao.save(newState);

		for (Statesample ss : stateSamples) {
			Statesample newStateSample = new Statesample();
			newStateSample.setStateId(newStateDb.getStateId());
			newStateSample.setSampleId(ss.getSampleId());

			statesampleDao.save(newStateSample);
		}

		for (Statejob sj : stateJobs) {
			Statejob newStateJob = new Statejob();
			newStateJob.setStateId(newStateDb.getStateId());
			newStateJob.setJobId(sj.getJobId());

			statejobDao.save(newStateJob);
		}

		for (Staterun sr : stateRuns) {
			Staterun newStateRun = new Staterun();
			newStateRun.setStateId(newStateDb.getStateId());
			newStateRun.setRunId(sr.getRunId());

			staterunDao.save(newStateRun);
		}

		return newState;
	}
}
