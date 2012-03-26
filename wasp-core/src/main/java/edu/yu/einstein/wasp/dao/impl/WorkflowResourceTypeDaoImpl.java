
/**
 *
 * WorkflowresourcetypeDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowResourceType Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.WorkflowResourceType;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class WorkflowResourceTypeDaoImpl extends WaspDaoImpl<WorkflowResourceType> implements edu.yu.einstein.wasp.dao.WorkflowResourceTypeDao {

	/**
	 * WorkflowresourcetypeDaoImpl() Constructor
	 *
	 *
	 */
	public WorkflowResourceTypeDaoImpl() {
		super();
		this.entityClass = WorkflowResourceType.class;
	}


	/**
	 * getWorkflowresourcetypeByWorkflowresourcetypeId(final Integer workflowresourcetypeId)
	 *
	 * @param final Integer workflowresourcetypeId
	 *
	 * @return workflowResourceType
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public WorkflowResourceType getWorkflowResourceTypeByWorkflowresourcetypeId (final Integer workflowresourcetypeId) {
    		HashMap m = new HashMap();
		m.put("workflowresourcetypeId", workflowresourcetypeId);

		List<WorkflowResourceType> results = this.findByMap(m);

		if (results.size() == 0) {
			WorkflowResourceType rt = new WorkflowResourceType();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getWorkflowresourcetypeByWorkflowIdResourceTypeId(final Integer workflowId, final Integer resourceTypeId)
	 *
	 * @param final Integer workflowId, final Integer resourceTypeId
	 *
	 * @return workflowResourceType
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public WorkflowResourceType getWorkflowResourceTypeByWorkflowIdResourceTypeId (final Integer workflowId, final Integer resourceTypeId) {
    		HashMap m = new HashMap();
		m.put("workflowId", workflowId);
		m.put("resourceTypeId", resourceTypeId);

		List<WorkflowResourceType> results = this.findByMap(m);

		if (results.size() == 0) {
			WorkflowResourceType rt = new WorkflowResourceType();
			return rt;
		}
		return results.get(0);
	}



}

