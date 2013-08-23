
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

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.WorkflowsoftwareMeta;


@Transactional("entityManager")
@Repository
public class WorkflowsoftwareMetaDaoImpl extends WaspMetaDaoImpl<WorkflowsoftwareMeta> implements edu.yu.einstein.wasp.dao.WorkflowsoftwareMetaDao {

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

	@Override
	@Transactional("entityManager")
	public WorkflowsoftwareMeta getWorkflowsoftwareMetaByWorkflowsoftwareMetaId (final Integer workflowsoftwareMetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", workflowsoftwareMetaId);

		List<WorkflowsoftwareMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			WorkflowsoftwareMeta rt = new WorkflowsoftwareMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getWorkflowsoftwareMetaByWorkflowsoftwareIdK(final Integer workflowsoftwareId, final String k)
	 *
	 * @param final Integer workflowsoftwareId, final String k
	 *
	 * @return workflowsoftwareMeta
	 */

	@Override
	@Transactional("entityManager")
	public WorkflowsoftwareMeta getWorkflowsoftwareMetaByWorkflowsoftwareIdK (final Integer workflowsoftwareId, final String k) {
    		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("workflowsoftwareId", workflowsoftwareId);
		m.put("k", k);

		List<WorkflowsoftwareMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			WorkflowsoftwareMeta rt = new WorkflowsoftwareMeta();
			return rt;
		}
		return results.get(0);
	}

}

