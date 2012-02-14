package edu.yu.einstein.wasp.batch.poller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.batch.TaskStatus;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.service.WorkflowService;

/**
 * Workflow Poller
 * polls job db for task/workflowid combos that are not final
 *
 */

@Service
public class WorkflowPoller {

	@Autowired
	StateService stateService;

	@Autowired
	TaskService taskService;

	@Autowired
	WorkflowService workflowService;
 
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
		Workflow workflow = workflowService.getWorkflowByIName(workflowIName);

		Task task = taskService.getTaskByIName(taskIName); 

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
