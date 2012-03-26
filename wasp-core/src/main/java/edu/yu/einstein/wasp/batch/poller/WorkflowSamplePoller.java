package edu.yu.einstein.wasp.batch.poller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.batch.TaskStatus;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.Task;

/**
 * Workflow Poller
 * polls job db for task/samplesubtype combos that are not final
 *
 */

@Service
public class WorkflowSamplePoller {

	@Autowired
	StateDao stateDao;

	@Autowired
	TaskDao taskDao;

	@Autowired
	SampleSubtypeDao sampleSubtypeDao;
 
	protected String sampleSubtypeIName; 
	public void setSampleSubtypeIName(String s) {
		this.sampleSubtypeIName = s;
	}

	protected String taskIName; 
	public void setTaskIName(String s) {
		this.taskIName = s;
	}

 
	@Transactional
	public List<State> getStates() {
		SampleSubtype sampleSubtype = sampleSubtypeDao.getSampleSubtypeByIName(sampleSubtypeIName); 
		Task task = taskDao.getTaskByIName(taskIName); 

		List<State> rt = new ArrayList();
		List<State> allStates = task.getState();

		if (allStates == null) { return rt; }

		for (State state: allStates) {
			if (state.getStatesample().isEmpty()) { continue; }
			if (state.getStatus().equals(TaskStatus.FINALIZED.toString())) { continue; }

			// should be the same so just get first, 
			// TODO check for npe
			if (state.getStatesample().get(0).getSample().getSampleSubtypeId() != sampleSubtype.getSampleSubtypeId()) {
				continue;
			}

			rt.add(state);
		}

		return rt; 
	}

}
