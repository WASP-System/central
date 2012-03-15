package edu.yu.einstein.wasp.load;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import util.spring.PostInitialize;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.dao.TaskMappingDao;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.TaskMapping;


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
	private TaskDao taskDao;

	@Autowired
	private TaskMappingDao taskMappingDao;

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

		Task task = taskDao.getTaskByIName(iname); 

		// inserts or update workflow
		if (task.getTaskId() == null) { 
			task = new Task();

			task.setIName(iname);
			task.setName(name);

			taskDao.save(task); 

			// refreshes
			task = taskDao.getTaskByIName(iname); 

		} else {

			// TODO check if any data chance, if not don't update.
			task.setName(name);

			taskDao.save(task); 
		}

		// no taskmapping in property
		if (taskMapping == null) { taskMapping = new ArrayList<TaskMapping>(); }

		List<TaskMapping> dbTaskMapping = task.getTaskMapping();

		Set<String> seenStatus = new HashSet(); 

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
				taskMappingDao.remove(tm);
				taskMappingDao.flush(tm);
			}
		}
		}

		if (taskMapping != null) {
		for (TaskMapping tm: taskMapping) {
			TaskMapping dbTm = taskMappingDao.getTaskMappingByTaskIdStatus(task.getTaskId(), tm.getStatus()); 

			if (dbTm.getTaskMappingId() != null) {
				// update
				dbTm.setPermission(tm.getPermission()); 
				dbTm.setListMap(tm.getListMap()); 
				dbTm.setDetailMap(tm.getDetailMap()); 
				taskMappingDao.save(dbTm);

			} else {
				// insert
				tm.setTaskId(task.getTaskId());
				taskMappingDao.save(tm);

			}

		}
		}

		updateUiFields(); 

	}
}

