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
			Map<String, Object> tmQuery = new HashMap<String, Object>();
			tmQuery.put("iName", tm.getIName());
			tmQuery.put("stepName", tm.getStepName());
			tmQuery.put("status", tm.getStatus());
			tmQuery.put("permission", tm.getPermission());
			List<TaskMapping> matchingTM = taskMappingDao.findByMap(tmQuery);
			if (matchingTM==null || matchingTM.isEmpty()){
				// is new
				tm.setIsActive(1);
				taskMappingDao.save(tm);
			} else {
				// exists so update if changed
				TaskMapping dbTm = matchingTM.get(0);
				if (!dbTm.getIName().equals(tm.getIName()))
					dbTm.setIName(tm.getIName()); 
				if (!dbTm.getStepName().equals(tm.getStepName()))
					dbTm.setStepName(tm.getStepName()); 
				if (!dbTm.getStatus().equals(tm.getStatus()))
					dbTm.setStatus(tm.getStatus()); 
				if (!dbTm.getPermission().equals(tm.getPermission()))
					dbTm.setPermission(tm.getPermission()); 
				if (!dbTm.getListMap().equals(tm.getListMap()))
					dbTm.setListMap(tm.getListMap()); 
				if (!dbTm.getDashboardSortOrder().equals(tm.getDashboardSortOrder()))
					dbTm.setDashboardSortOrder(tm.getDashboardSortOrder());
				dbTm.setIsActive(1);
			}
		}
	}

	
}
