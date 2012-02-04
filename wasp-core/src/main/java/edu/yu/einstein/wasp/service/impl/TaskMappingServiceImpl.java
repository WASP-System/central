
/**
 *
 * TaskMappingServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the TaskMappingService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.TaskMappingDao;
import edu.yu.einstein.wasp.model.TaskMapping;
import edu.yu.einstein.wasp.service.TaskMappingService;

@Service
public class TaskMappingServiceImpl extends WaspServiceImpl<TaskMapping> implements TaskMappingService {

	/**
	 * taskMappingDao;
	 *
	 */
	private TaskMappingDao taskMappingDao;

	/**
	 * setTaskMappingDao(TaskMappingDao taskMappingDao)
	 *
	 * @param taskMappingDao
	 *
	 */
	@Override
	@Autowired
	public void setTaskMappingDao(TaskMappingDao taskMappingDao) {
		this.taskMappingDao = taskMappingDao;
		this.setWaspDao(taskMappingDao);
	}

	/**
	 * getTaskMappingDao();
	 *
	 * @return taskMappingDao
	 *
	 */
	@Override
	public TaskMappingDao getTaskMappingDao() {
		return this.taskMappingDao;
	}


  @Override
public TaskMapping getTaskMappingByTaskMappingId (final int taskMappingId) {
    return this.getTaskMappingDao().getTaskMappingByTaskMappingId(taskMappingId);
  }

  @Override
public TaskMapping getTaskMappingByTaskIdStatus (final int taskId, final String status) {
    return this.getTaskMappingDao().getTaskMappingByTaskIdStatus(taskId, status);
  }

}

