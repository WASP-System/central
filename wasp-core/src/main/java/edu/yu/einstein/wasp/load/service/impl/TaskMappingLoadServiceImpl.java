package edu.yu.einstein.wasp.load.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.TaskMappingDao;
import edu.yu.einstein.wasp.load.service.TaskMappingLoadService;
import edu.yu.einstein.wasp.model.TaskMapping;

@Service
@Transactional
public class TaskMappingLoadServiceImpl extends WaspLoadServiceImpl implements	TaskMappingLoadService {
	
	@Autowired
	private TaskMappingDao taskMappingDao;
	
	@Override
	public void update(String iname, List<TaskMapping> taskMapping){
		// no taskmapping in property
		if (taskMapping == null) { taskMapping = new ArrayList<TaskMapping>(); }

		// checks if already exists
		for (TaskMapping tm: taskMapping) {
			if (tm.getDashboardSortOrder()==null)
				tm.setDashboardSortOrder(1); // default to 1
			if (tm.getIName() == null)
				tm.setIName(iname);
			TaskMapping dbTm = taskMappingDao.getTaskMappingByIName(tm.getIName());
			if (dbTm==null || dbTm.getTaskMappingId() == 0){
				// is new
				tm.setIsActive(1);
				taskMappingDao.save(tm);
			} else {
				// exists so update if changed
				if ( (dbTm.getName() == null && tm.getName() != null) || !dbTm.getName().equals(tm.getName()))
					dbTm.setName(tm.getName()); 
				if ( (dbTm.getStepName() == null && tm.getStepName() != null) || !dbTm.getStepName().equals(tm.getStepName()))
					dbTm.setStepName(tm.getStepName()); 
				if ( (dbTm.getStatus() == null && tm.getStatus() != null) || !dbTm.getStatus().equals(tm.getStatus()))
					dbTm.setStatus(tm.getStatus()); 
				if ( (dbTm.getPermission() == null && tm.getPermission() != null) || !dbTm.getPermission().equals(tm.getPermission()))
					dbTm.setPermission(tm.getPermission()); 
				if ( (dbTm.getListMap() == null && tm.getListMap() != null) || !dbTm.getListMap().equals(tm.getListMap()))
					dbTm.setListMap(tm.getListMap()); 
				if ( (dbTm.getDashboardSortOrder() == null && tm.getDashboardSortOrder() != null) || !dbTm.getDashboardSortOrder().equals(tm.getDashboardSortOrder()))
					dbTm.setDashboardSortOrder(tm.getDashboardSortOrder());
				dbTm.setIsActive(1);
			}
		}
	}

	
}
