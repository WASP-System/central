
/**
 *
 * WorkflowMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.WorkflowMeta;


@Transactional
@Repository
public class WorkflowMetaDaoImpl extends WaspMetaDaoImpl<WorkflowMeta> implements edu.yu.einstein.wasp.dao.WorkflowMetaDao {

	/**
	 * WorkflowMetaDaoImpl() Constructor
	 *
	 *
	 */
	public WorkflowMetaDaoImpl() {
		super();
		this.entityClass = WorkflowMeta.class;
	}


	/**
	 * getWorkflowMetaByWorkflowMetaId(final Integer workflowMetaId)
	 *
	 * @param final Integer workflowMetaId
	 *
	 * @return workflowMeta
	 */

	@Override
	@Transactional
	public WorkflowMeta getWorkflowMetaByWorkflowMetaId (final Integer workflowMetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", workflowMetaId);

		List<WorkflowMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			WorkflowMeta rt = new WorkflowMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getWorkflowMetaByKWorkflowId(final String k, final Integer workflowId)
	 *
	 * @param final String k, final Integer workflowId
	 *
	 * @return workflowMeta
	 */

	@Override
	@Transactional
	public WorkflowMeta getWorkflowMetaByKWorkflowId (final String k, final Integer workflowId) {
    		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("k", k);
		m.put("workflowId", workflowId);

		List<WorkflowMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			WorkflowMeta rt = new WorkflowMeta();
			return rt;
		}
		return results.get(0);
	}


}

