package edu.yu.einstein.wasp.batch.poller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.batch.TaskStatus;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.Workflow;

/**
 * Workflow Poller
 * polls job db for task/workflowid combos that are not final
 *
 */

@Service
public class WorkflowPoller {

	@Autowired
	StateDao stateDao;

	@Autowired
	TaskDao taskDao;

	@Autowired
	WorkflowDao workflowDao;
 
	protected String workflowIName; 
	public void setWorkflowIName(String s) {
		this.workflowIName = s;
	}

	protected String taskIName; 
	public void setTaskIName(String s) {
		this.taskIName = s;
	}

 
	@Transactional
	public List<State> getStates() {
		Workflow workflow = workflowDao.getWorkflowByIName(workflowIName);

		Task task = taskDao.getTaskByIName(taskIName); 

		List<State> rt = new ArrayList<State>();

		List<State> allStates = task.getState();

                if (allStates == null) { return rt; }
                if (workflow.getWorkflowId() == null) { return rt; }

		for (State state: allStates) {
			if (state.getStatejob().isEmpty()) { continue; }
			if (state.getStatus().equals(TaskStatus.FINALIZED.toString())) { continue; }

			// should be the same so just get first, 
			// TODO check for npe
			try {
			if (state.getStatejob().get(0).getJob().getWorkflowId().intValue() != workflow.getWorkflowId().intValue()) {
				continue;
			}
			} catch (Exception e) {
				continue;
			}


			rt.add(state);
		}

		return rt; 
	}

}
