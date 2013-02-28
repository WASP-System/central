
/**
 *
 * WorkflowSoftwareDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowSoftware Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.WorkflowSoftware;


@Transactional
@Repository
public class WorkflowSoftwareDaoImpl extends WaspDaoImpl<WorkflowSoftware> implements edu.yu.einstein.wasp.dao.WorkflowSoftwareDao {

	/**
	 * WorkflowSoftwareDaoImpl() Constructor
	 *
	 *
	 */
	public WorkflowSoftwareDaoImpl() {
		super();
		this.entityClass = WorkflowSoftware.class;
	}


	/**
	 * getWorkflowSoftwareByWorkflowSoftwareId(final Integer workflowSoftwareId)
	 *
	 * @param final Integer workflowSoftwareId
	 *
	 * @return workflowSoftware
	 */

	@Override
	@Transactional
	public WorkflowSoftware getWorkflowSoftwareByWorkflowSoftwareId (final Integer workflowSoftwareId) {
    	HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", workflowSoftwareId);

		List<WorkflowSoftware> results = this.findByMap(m);

		if (results.size() == 0) {
			WorkflowSoftware rt = new WorkflowSoftware();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getWorkflowSoftwareByWorkflowIdSoftwareId(final Integer workflowId, final Integer softwareId)
	 *
	 * @param final Integer workflowId, final Integer softwareId
	 *
	 * @return workflowSoftware
	 */

	@Override
	@Transactional
	public WorkflowSoftware getWorkflowSoftwareByWorkflowIdSoftwareId (final Integer workflowId, final Integer softwareId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("workflowId", workflowId);
		m.put("softwareId", softwareId);

		List<WorkflowSoftware> results = this.findByMap(m);

		if (results.size() == 0) {
			WorkflowSoftware rt = new WorkflowSoftware();
			return rt;
		}
		return results.get(0);
	}



}

