
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
	 * getTaskMappingByTaskIdStatus(final int taskId, final String status)
	 *
	 * @param final int taskId, final String status
	 *
	 * @return taskMapping
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public TaskMapping getTaskMappingByTaskIdStatus (final int taskId, final String status) {
    		HashMap m = new HashMap();
		m.put("taskId", taskId);
		m.put("status", status);

		List<TaskMapping> results = this.findByMap(m);

		if (results.size() == 0) {
			TaskMapping rt = new TaskMapping();
			return rt;
		}
		return results.get(0);
	}



}

