
/**
 *
 * WorkflowsoftwareMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowsoftwareMeta Dao Impl
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

import edu.yu.einstein.wasp.model.WorkflowsoftwareMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class WorkflowsoftwareMetaDaoImpl extends WaspDaoImpl<WorkflowsoftwareMeta> implements edu.yu.einstein.wasp.dao.WorkflowsoftwareMetaDao {

	/**
	 * WorkflowsoftwareMetaDaoImpl() Constructor
	 *
	 *
	 */
	public WorkflowsoftwareMetaDaoImpl() {
		super();
		this.entityClass = WorkflowsoftwareMeta.class;
	}


	/**
	 * getWorkflowsoftwareMetaByWorkflowsoftwareMetaId(final Integer workflowsoftwareMetaId)
	 *
	 * @param final Integer workflowsoftwareMetaId
	 *
	 * @return workflowsoftwareMeta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public WorkflowsoftwareMeta getWorkflowsoftwareMetaByWorkflowsoftwareMetaId (final Integer workflowsoftwareMetaId) {
    		HashMap m = new HashMap();
		m.put("workflowsoftwareMetaId", workflowsoftwareMetaId);

		List<WorkflowsoftwareMeta> results = (List<WorkflowsoftwareMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			WorkflowsoftwareMeta rt = new WorkflowsoftwareMeta();
			return rt;
		}
		return (WorkflowsoftwareMeta) results.get(0);
	}



	/**
	 * getWorkflowsoftwareMetaByWorkflowsoftwareIdK(final Integer workflowsoftwareId, final String k)
	 *
	 * @param final Integer workflowsoftwareId, final String k
	 *
	 * @return workflowsoftwareMeta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public WorkflowsoftwareMeta getWorkflowsoftwareMetaByWorkflowsoftwareIdK (final Integer workflowsoftwareId, final String k) {
    		HashMap m = new HashMap();
		m.put("workflowsoftwareId", workflowsoftwareId);
		m.put("k", k);

		List<WorkflowsoftwareMeta> results = (List<WorkflowsoftwareMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			WorkflowsoftwareMeta rt = new WorkflowsoftwareMeta();
			return rt;
		}
		return (WorkflowsoftwareMeta) results.get(0);
	}



}

