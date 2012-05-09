package edu.yu.einstein.wasp.load;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
			task.setIName(iname);
			task.setName(name);

			taskDao.save(task); 

			// refreshes
			task = taskDao.getTaskByIName(iname); 

		} else {

			if (task.getName()==null || !task.getName().equals(name)){
				task.setName(name); // merge
			}
			
		}

		// no taskmapping in property
		if (taskMapping == null) { taskMapping = new ArrayList<TaskMapping>(); }

		List<TaskMapping> dbTaskMapping = taskMappingDao.getTaskMappingByTaskId(task.getTaskId());

		// checks if already exists
		for (TaskMapping tm: taskMapping) {
			tm.setTaskId(task.getTaskId());
			if (tm.getDashboardSortOrder()==null)
				tm.setDashboardSortOrder(1); // default to 1
			Map<String, Object> tmQuery = new HashMap<String, Object>();
			tmQuery.put("taskId", tm.getTaskId());
			tmQuery.put("status", tm.getStatus());
			tmQuery.put("permission", tm.getPermission());
			List<TaskMapping> matchingTM = taskMappingDao.findByMap(tmQuery);
			if (matchingTM==null || matchingTM.isEmpty()){
				// is new
				taskMappingDao.save(tm);
			} else {
				// exists so update if changed
				TaskMapping dbTm = matchingTM.get(0);
				if (!dbTm.getPermission().equals(tm.getPermission()))
					dbTm.setPermission(tm.getPermission()); 
				if (!dbTm.getListMap().equals(tm.getListMap()))
					dbTm.setListMap(tm.getListMap()); 
				if (!dbTm.getDetailMap().equals(tm.getDetailMap()))
					dbTm.setDetailMap(tm.getDetailMap());
				if (!dbTm.getDashboardSortOrder().equals(tm.getDashboardSortOrder()))
					dbTm.setDashboardSortOrder(tm.getDashboardSortOrder());
				dbTaskMapping.remove(dbTm);
			}
		}
		
		// cleanup old taskMappings
		if (dbTaskMapping != null){
			for (TaskMapping tm: dbTaskMapping) {
				taskMappingDao.remove(tm);
			}
		}

		updateUiFields(); 

	}
}

