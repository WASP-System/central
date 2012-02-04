
/**
 *
 * TaskMappingDao.java 
 * @author echeng (table2type.pl)
 *  
 * the TaskMapping Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.TaskMapping;


public interface TaskMappingDao extends WaspDao<TaskMapping> {

  public TaskMapping getTaskMappingByTaskMappingId (final int taskMappingId);

  public TaskMapping getTaskMappingByTaskIdStatus (final int taskId, final String status);


}

