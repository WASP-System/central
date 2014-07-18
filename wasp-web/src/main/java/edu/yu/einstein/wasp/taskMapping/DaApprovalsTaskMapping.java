package edu.yu.einstein.wasp.taskMapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.service.TaskService;

/**
 * 
 * @author asmclellan
 *
 */
public class DaApprovalsTaskMapping extends WaspTaskMapping {
	
	private TaskService taskService;

	@Autowired
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}
	
	public DaApprovalsTaskMapping(String localizedLabelKey, String targetLink, String permission) {
		super(localizedLabelKey, targetLink, permission);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRequirementToShowLink(Object o) throws WaspException {
		@SuppressWarnings("unchecked")
		List<Job> jobList = (List<Job>) o;
		return taskService.isDepartmentAdminPendingTasks(jobList);
	}

}
