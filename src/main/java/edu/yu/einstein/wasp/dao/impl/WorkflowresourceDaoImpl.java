
/**
 *
 * WorkflowresourceDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowresource Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Workflowresource;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class WorkflowresourceDaoImpl extends WaspDaoImpl<Workflowresource> implements edu.yu.einstein.wasp.dao.WorkflowresourceDao {

	/**
	 * WorkflowresourceDaoImpl() Constructor
	 *
	 *
	 */
	public WorkflowresourceDaoImpl() {
		super();
		this.entityClass = Workflowresource.class;
	}


	/**
	 * getWorkflowresourceByWorkflowresourceId(final int workflowresourceId)
	 *
	 * @param final int workflowresourceId
	 *
	 * @return workflowresource
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Workflowresource getWorkflowresourceByWorkflowresourceId (final int workflowresourceId) {
    		HashMap m = new HashMap();
		m.put("workflowresourceId", workflowresourceId);

		List<Workflowresource> results = (List<Workflowresource>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Workflowresource rt = new Workflowresource();
			return rt;
		}
		return (Workflowresource) results.get(0);
	}



	/**
	 * getWorkflowresourceByWorkflowIdResourceId(final int workflowId, final int resourceId)
	 *
	 * @param final int workflowId, final int resourceId
	 *
	 * @return workflowresource
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Workflowresource getWorkflowresourceByWorkflowIdResourceId (final int workflowId, final int resourceId) {
    		HashMap m = new HashMap();
		m.put("workflowId", workflowId);
		m.put("resourceId", resourceId);

		List<Workflowresource> results = (List<Workflowresource>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Workflowresource rt = new Workflowresource();
			return rt;
		}
		return (Workflowresource) results.get(0);
	}



}

