
/**
 *
 * TaskMappingDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the TaskMapping Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.batch.core.BatchStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.TaskMapping;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class TaskMappingDaoImpl extends WaspDaoImpl<TaskMapping> implements edu.yu.einstein.wasp.dao.TaskMappingDao {

	/**
	 * TaskMappingDaoImpl() Constructor
	 *
	 *
	 */
	public TaskMappingDaoImpl() {
		super();
		this.entityClass = TaskMapping.class;
	}


	/**
	 * getTaskMappingByTaskMappingId(final int taskMappingId)
	 *
	 * @param final int taskMappingId
	 *
	 * @return taskMapping
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public TaskMapping getTaskMappingByTaskMappingId (final int taskMappingId) {
    		HashMap m = new HashMap();
		m.put("taskMappingId", taskMappingId);

		List<TaskMapping> results = this.findByMap(m);

		if (results.size() == 0) {
			TaskMapping rt = new TaskMapping();
			return rt;
		}
		return results.get(0);
	}
	
	/**
	 * getTaskMappingByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return taskMapping
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public TaskMapping getTaskMappingByIName (final String iName) {
    		HashMap m = new HashMap();
		m.put("iName", iName);

		List<TaskMapping> results = this.findByMap(m);

		if (results.size() == 0) {
			TaskMapping rt = new TaskMapping();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getTaskMappingByStepAndStatus(final String stepName, final String status)
	 *
	 * @param final int taskId, final String status
	 *
	 * @return taskMapping
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<TaskMapping> getTaskMappingByBatchStepAndStatus (final String stepName, final String status) {
    		HashMap m = new HashMap();
		m.put("stepName", stepName);
		m.put("status", status);

		List<TaskMapping> results = this.findByMap(m);

		if (results.size() == 0) {
			return null;
		}
		return results;
	}
	
	/**
	 * getTaskMappingByTaskId(final int taskId)
	 *
	 * @param final int taskId
	 *
	 * @return taskMapping
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<TaskMapping> getTaskMappingByBatchStep (final String stepName) {
    		HashMap m = new HashMap();
		m.put("stepName", stepName);

		List<TaskMapping> results = this.findByMap(m);

		if (results.size() == 0) {
			return null;
		}
		return results;
	}



}

