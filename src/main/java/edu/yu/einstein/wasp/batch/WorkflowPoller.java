package edu.yu.einstein.wasp.batch;


import edu.yu.einstein.wasp.model.*;
import edu.yu.einstein.wasp.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList; 
import java.util.List; 
import java.util.Map; 
import java.util.HashMap; 

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

		List<State> rt = new ArrayList();

		List<State> allStates = task.getState();


		for (State state: allStates) {
			if (state.getStatejob().isEmpty()) { continue; }
			if (state.getStatus().equals("FINAL")) { continue; }

			// should be the same so just get first, 
			// TODO check for npe
			if (state.getStatejob().get(0).getJob().getWorkflowId() != workflow.getWorkflowId()) {
				continue;
			}


			rt.add(state);
		}

		return rt; 
	}

}
