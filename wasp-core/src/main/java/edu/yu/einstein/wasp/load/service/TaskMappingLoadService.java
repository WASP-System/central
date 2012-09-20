package edu.yu.einstein.wasp.load.service;

import java.util.List;

import edu.yu.einstein.wasp.model.TaskMapping;

public interface TaskMappingLoadService extends WaspLoadService {
	
	public void update(String iname, List<TaskMapping> taskMapping);

}
