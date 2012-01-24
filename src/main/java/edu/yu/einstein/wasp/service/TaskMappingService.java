
/**
 *
 * TaskMappingService.java 
 * @author echeng (table2type.pl)
 *  
 * the TaskMappingService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.TaskMappingDao;
import edu.yu.einstein.wasp.model.TaskMapping;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface TaskMappingService extends WaspService<TaskMapping> {

	/**
	 * setTaskMappingDao(TaskMappingDao taskMappingDao)
	 *
	 * @param taskMappingDao
	 *
	 */
	public void setTaskMappingDao(TaskMappingDao taskMappingDao);

	/**
	 * getTaskMappingDao();
	 *
	 * @return taskMappingDao
	 *
	 */
	public TaskMappingDao getTaskMappingDao();

  public TaskMapping getTaskMappingByTaskMappingId (final int taskMappingId);

  public TaskMapping getTaskMappingByTaskIdStatus (final int taskId, final String status);


}

