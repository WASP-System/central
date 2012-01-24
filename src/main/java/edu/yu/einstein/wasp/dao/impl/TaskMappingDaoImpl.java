
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

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

	@SuppressWarnings("unchecked")
	@Transactional
	public TaskMapping getTaskMappingByTaskMappingId (final int taskMappingId) {
    		HashMap m = new HashMap();
		m.put("taskMappingId", taskMappingId);

		List<TaskMapping> results = (List<TaskMapping>) this.findByMap((Map) m);

		if (results.size() == 0) {
			TaskMapping rt = new TaskMapping();
			return rt;
		}
		return (TaskMapping) results.get(0);
	}



	/**
	 * getTaskMappingByTaskIdStatus(final int taskId, final String status)
	 *
	 * @param final int taskId, final String status
	 *
	 * @return taskMapping
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public TaskMapping getTaskMappingByTaskIdStatus (final int taskId, final String status) {
    		HashMap m = new HashMap();
		m.put("taskId", taskId);
		m.put("status", status);

		List<TaskMapping> results = (List<TaskMapping>) this.findByMap((Map) m);

		if (results.size() == 0) {
			TaskMapping rt = new TaskMapping();
			return rt;
		}
		return (TaskMapping) results.get(0);
	}



}

