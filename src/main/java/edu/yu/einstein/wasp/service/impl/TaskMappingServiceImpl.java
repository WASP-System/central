
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

import edu.yu.einstein.wasp.service.TaskMappingService;
import edu.yu.einstein.wasp.dao.TaskMappingDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.TaskMapping;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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
	public TaskMappingDao getTaskMappingDao() {
		return this.taskMappingDao;
	}


  public TaskMapping getTaskMappingByTaskMappingId (final int taskMappingId) {
    return this.getTaskMappingDao().getTaskMappingByTaskMappingId(taskMappingId);
  }

  public TaskMapping getTaskMappingByTaskIdStatus (final int taskId, final String status) {
    return this.getTaskMappingDao().getTaskMappingByTaskIdStatus(taskId, status);
  }

}

