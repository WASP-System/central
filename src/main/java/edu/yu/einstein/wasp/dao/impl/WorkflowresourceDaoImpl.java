
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
	 * getWorkflowresourceByWorkflowresourceId(final Integer workflowresourceId)
	 *
	 * @param final Integer workflowresourceId
	 *
	 * @return workflowresource
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Workflowresource getWorkflowresourceByWorkflowresourceId (final Integer workflowresourceId) {
    		HashMap m = new HashMap();
		m.put("workflowresourceId", workflowresourceId);

		List<Workflowresource> results = this.findByMap(m);

		if (results.size() == 0) {
			Workflowresource rt = new Workflowresource();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getWorkflowresourceByWorkflowIdResourceId(final Integer workflowId, final Integer resourceId)
	 *
	 * @param final Integer workflowId, final Integer resourceId
	 *
	 * @return workflowresource
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Workflowresource getWorkflowresourceByWorkflowIdResourceId (final Integer workflowId, final Integer resourceId) {
    		HashMap m = new HashMap();
		m.put("workflowId", workflowId);
		m.put("resourceId", resourceId);

		List<Workflowresource> results = this.findByMap(m);

		if (results.size() == 0) {
			Workflowresource rt = new Workflowresource();
			return rt;
		}
		return results.get(0);
	}



}

