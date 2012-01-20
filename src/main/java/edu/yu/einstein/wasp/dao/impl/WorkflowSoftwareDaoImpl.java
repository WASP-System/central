
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.WorkflowSoftware;

@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	@Transactional
	public WorkflowSoftware getWorkflowSoftwareByWorkflowSoftwareId (final Integer workflowSoftwareId) {
    		HashMap m = new HashMap();
		m.put("workflowSoftwareId", workflowSoftwareId);

		List<WorkflowSoftware> results = (List<WorkflowSoftware>) this.findByMap((Map) m);

		if (results.size() == 0) {
			WorkflowSoftware rt = new WorkflowSoftware();
			return rt;
		}
		return (WorkflowSoftware) results.get(0);
	}



	/**
	 * getWorkflowSoftwareByWorkflowIdSoftwareId(final Integer workflowId, final Integer softwareId)
	 *
	 * @param final Integer workflowId, final Integer softwareId
	 *
	 * @return workflowSoftware
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public WorkflowSoftware getWorkflowSoftwareByWorkflowIdSoftwareId (final Integer workflowId, final Integer softwareId) {
    		HashMap m = new HashMap();
		m.put("workflowId", workflowId);
		m.put("softwareId", softwareId);

		List<WorkflowSoftware> results = (List<WorkflowSoftware>) this.findByMap((Map) m);

		if (results.size() == 0) {
			WorkflowSoftware rt = new WorkflowSoftware();
			return rt;
		}
		return (WorkflowSoftware) results.get(0);
	}



}
