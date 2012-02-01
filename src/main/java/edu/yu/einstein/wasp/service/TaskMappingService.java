
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

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.TaskMappingDao;
import edu.yu.einstein.wasp.model.TaskMapping;

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

