
/**
 *
 * WorkflowresourceMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowresourceMeta Dao Impl
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

import edu.yu.einstein.wasp.model.WorkflowresourceMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class WorkflowresourceMetaDaoImpl extends WaspDaoImpl<WorkflowresourceMeta> implements edu.yu.einstein.wasp.dao.WorkflowresourceMetaDao {

	/**
	 * WorkflowresourceMetaDaoImpl() Constructor
	 *
	 *
	 */
	public WorkflowresourceMetaDaoImpl() {
		super();
		this.entityClass = WorkflowresourceMeta.class;
	}


	/**
	 * getWorkflowresourceMetaByWorkflowresourceoptionId(final Integer workflowresourceoptionId)
	 *
	 * @param final Integer workflowresourceoptionId
	 *
	 * @return workflowresourceMeta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public WorkflowresourceMeta getWorkflowresourceMetaByWorkflowresourceoptionId (final Integer workflowresourceoptionId) {
    		HashMap m = new HashMap();
		m.put("workflowresourceoptionId", workflowresourceoptionId);

		List<WorkflowresourceMeta> results = (List<WorkflowresourceMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			WorkflowresourceMeta rt = new WorkflowresourceMeta();
			return rt;
		}
		return (WorkflowresourceMeta) results.get(0);
	}



	/**
	 * getWorkflowresourceMetaByWorkflowresourceIdK(final Integer workflowresourceId, final Integer k)
	 *
	 * @param final Integer workflowresourceId, final Integer k
	 *
	 * @return workflowresourceMeta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public WorkflowresourceMeta getWorkflowresourceMetaByWorkflowresourceIdK (final Integer workflowresourceId, final Integer k) {
    		HashMap m = new HashMap();
		m.put("workflowresourceId", workflowresourceId);
		m.put("k", k);

		List<WorkflowresourceMeta> results = (List<WorkflowresourceMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			WorkflowresourceMeta rt = new WorkflowresourceMeta();
			return rt;
		}
		return (WorkflowresourceMeta) results.get(0);
	}



}

