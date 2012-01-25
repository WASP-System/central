package edu.yu.einstein.wasp.load;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import util.spring.PostInitialize;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.service.TaskService;

import java.util.List; 
import java.util.ArrayList; 
import java.util.Map; 
import java.util.HashMap; 
import java.util.Set; 
import java.util.HashSet; 

import edu.yu.einstein.wasp.model.TaskMapping;
import edu.yu.einstein.wasp.service.TaskMappingService;


/**
 * update/inserts db copy of task from bean definition
 * takes in	properties
 *	 - iname
 *	 - name
 *	 - uifields (List<UiFields)
 *
 */

@Transactional
public class TaskLoadService extends WaspLoadService {

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskMappingService taskMappingService;

	public TaskLoadService (){};

	private List<TaskMapping> taskMapping;
	public void setTaskMapping(List<TaskMapping> taskMapping) {
		this.taskMapping = taskMapping; 
	}

	@Override
	@Transactional
	@PostInitialize 
	public void postInitialize() {
		// skips component scanned	(if scanned in)
		if (name == null) { return; }

		logger.info("task loader started for  " + iname);

		Task task = taskService.getTaskByIName(iname); 

		// inserts or update workflow
		if (task.getTaskId() == null) { 
			task = new Task();

			task.setIName(iname);
			task.setName(name);

			taskService.save(task); 

			// refreshes
			task = taskService.getTaskByIName(iname); 

		} else {

			// TODO check if any data chance, if not don't update.
			task.setName(name);

			taskService.save(task); 
		}

		// no taskmapping in property
		if (taskMapping == null) { taskMapping = new ArrayList<TaskMapping>(); }

		List<TaskMapping> dbTaskMapping = task.getTaskMapping();

		Set<String> seenStatus = new HashSet(); 

		try { // TODO RWALLY FIX
		// adds to seen
		if (taskMapping != null) {
		for (TaskMapping tm: taskMapping) {
			seenStatus.add(tm.getStatus());
		}
		}

		// hard removal, todo make a set of status and check that
		if (dbTaskMapping != null) {
		for (TaskMapping tm: dbTaskMapping) {
			if (seenStatus.contains(tm.getStatus())) {
				taskMappingService.remove(tm);
				taskMappingService.flush(tm);
			}
		}
		}

		if (taskMapping != null) {
		for (TaskMapping tm: taskMapping) {
			TaskMapping dbTm = taskMappingService.getTaskMappingByTaskIdStatus(task.getTaskId(), tm.getStatus()); 

			if (dbTm.getTaskMappingId() != null) {
				// update
				dbTm.setPermission(tm.getPermission()); 
				dbTm.setListMap(tm.getListMap()); 
				dbTm.setDetailMap(tm.getDetailMap()); 
				taskMappingService.save(dbTm);

			} else {
				// insert
				tm.setTaskId(task.getTaskId());
				taskMappingService.save(tm);

			}

		}
		}

		updateUiFields(); 

	}
}

