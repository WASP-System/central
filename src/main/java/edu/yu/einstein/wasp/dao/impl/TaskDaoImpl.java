
/**
 *
 * TaskDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Task Dao Impl
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
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Task;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class TaskDaoImpl extends WaspDaoImpl<Task> implements edu.yu.einstein.wasp.dao.TaskDao {

	/**
	 * TaskDaoImpl() Constructor
	 *
	 *
	 */
	public TaskDaoImpl() {
		super();
		this.entityClass = Task.class;
	}


	/**
	 * getTaskByTaskId(final int taskId)
	 *
	 * @param final int taskId
	 *
	 * @return task
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Task getTaskByTaskId (final int taskId) {
    		HashMap m = new HashMap();
		m.put("taskId", taskId);

		List<Task> results = (List<Task>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Task rt = new Task();
			return rt;
		}
		return (Task) results.get(0);
	}



	/**
	 * getTaskByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return task
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Task getTaskByIName (final String iName) {
    		HashMap m = new HashMap();
		m.put("iName", iName);

		List<Task> results = (List<Task>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Task rt = new Task();
			return rt;
		}
		return (Task) results.get(0);
	}



}

