
/**
 *
 * WorkflowDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflow Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Workflow;


@Transactional
@Repository
public class WorkflowDaoImpl extends WaspDaoImpl<Workflow> implements edu.yu.einstein.wasp.dao.WorkflowDao {

	/**
	 * WorkflowDaoImpl() Constructor
	 *
	 *
	 */
	public WorkflowDaoImpl() {
		super();
		this.entityClass = Workflow.class;
	}


	/**
	 * getWorkflowByWorkflowId(final Integer workflowId)
	 *
	 * @param final Integer workflowId
	 *
	 * @return workflow
	 */

	@Override
	@Transactional
	public Workflow getWorkflowByWorkflowId (final Integer workflowId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("workflowId", workflowId);

		List<Workflow> results = this.findByMap(m);

		if (results.size() == 0) {
			Workflow rt = new Workflow();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getWorkflowByIName(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return workflow
	 */

	@Override
	@Transactional
	public Workflow getWorkflowByIName (final String iName) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("iName", iName);

		List<Workflow> results = this.findByMap(m);

		if (results.size() == 0) {
			Workflow rt = new Workflow();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getWorkflowByName(final String name)
	 *
	 * @param final String name
	 *
	 * @return workflow
	 */

	@Override
	@Transactional
	public Workflow getWorkflowByName (final String name) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("name", name);

		List<Workflow> results = this.findByMap(m);

		if (results.size() == 0) {
			Workflow rt = new Workflow();
			return rt;
		}
		return results.get(0);
	}

	  @Override
	  public List<Workflow> getActiveWorkflows(){
		  Map<String, Integer> workflowQueryMap = new HashMap<String, Integer>();
		  workflowQueryMap.put("isActive", 1);
		  return this.findByMap(workflowQueryMap);
	  }

}

