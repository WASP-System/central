package edu.yu.einstein.wasp.taskMapping;

import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.service.TaskService;

/**
 * 
 * @author asmclellan
 *
 */
public class FmApprovalsTaskMapping extends WaspTaskMapping {
	
	private TaskService taskService;

	@Autowired
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}
	
	public FmApprovalsTaskMapping(String localizedLabelKey, String targetLink, String permission) {
		super(localizedLabelKey, targetLink, permission);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRequirementToShowLink() throws WaspException {
		return taskService.isFmPendingTasks();
	}

}
