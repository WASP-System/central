
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

import java.util.List;

import edu.yu.einstein.wasp.model.TaskMapping;


public interface TaskMappingDao extends WaspDao<TaskMapping> {

  public TaskMapping getTaskMappingByTaskMappingId (final int taskMappingId);

  public List<TaskMapping> getTaskMappingByBatchStepAndStatus (final String stepName, final String status);

  public List<TaskMapping> getTaskMappingByBatchStep(final String stepName);
  
  public TaskMapping getTaskMappingByIName (final String iName);


}

