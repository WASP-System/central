
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

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.TaskMapping;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class TaskDaoImpl extends WaspDaoImpl<Task> implements edu.yu.einstein.wasp.dao.TaskDao {

	@Autowired
	StateDao stateDao;

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

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Task getTaskByTaskId (final int taskId) {
    		HashMap m = new HashMap();
		m.put("taskId", taskId);

		List<Task> results = this.findByMap(m);

		if (results.size() == 0) {
			Task rt = new Task();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getTaskByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return task
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Task getTaskByIName (final String iName) {
    		HashMap m = new HashMap();
		m.put("iName", iName);

		List<Task> results = this.findByMap(m);

		if (results.size() == 0) {
			Task rt = new Task();
			return rt;
		}
		return results.get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<State> getStatesByTaskIName (final String iName, final String status) {
		Task t = this.getTaskByIName(iName); 

    		Map m = new HashMap();
		m.put("taskId", t.getTaskId());
		m.put("status", status);
		List<State> rt = stateDao.findByMap(m);
		return rt;
	}

	@Override
	public List<TaskMapping> getTaskMappings() {
		List<TaskMapping> list=entityManager.createQuery("select t from TaskMapping t").getResultList();
		
		Map<Integer,TaskMapping> map = new HashMap<Integer,TaskMapping>();
		
		for(TaskMapping m:list) {
			 map.put(m.getTaskMappingId(),m);	
		}
		
		String sql=
			"select tm.taskmappingid,count(s.stateid)\n"+
			"from taskmapping tm\n"+
			"join task t on tm.taskid\n"+
			"join state s on s.taskid=t.taskid\n"+
			"and s.status=tm.status\n"+
			"where tm.dashboardsortorder is not null\n"+
			"group by t.name,tm.status\n"+
			"having count(s.stateid)>0\n";
		
		List<Object[]> counts=entityManager.createNativeQuery(sql).getResultList();
		for(Object[] o:counts) {
			TaskMapping tm=map.get(o[0]);
			if (tm!=null) tm.setStateCount(((BigInteger)o[1]).intValue());
		}
		
		return list;
	}


}

