
/**
 *
 * WorkflowsamplesubtypeDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowSampleSubtype Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.WorkflowSampleSubtype;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class WorkflowSampleSubtypeDaoImpl extends WaspDaoImpl<WorkflowSampleSubtype> implements edu.yu.einstein.wasp.dao.WorkflowSampleSubtypeDao {

	/**
	 * WorkflowsamplesubtypeDaoImpl() Constructor
	 *
	 *
	 */
	public WorkflowSampleSubtypeDaoImpl() {
		super();
		this.entityClass = WorkflowSampleSubtype.class;
	}


	/**
	 * getWorkflowsamplesubtypeByWorkflowsamplesubtypeId(final Integer workflowsamplesubtypeId)
	 *
	 * @param final Integer workflowsamplesubtypeId
	 *
	 * @return workflowSampleSubtype
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public WorkflowSampleSubtype getWorkflowSampleSubtypeByWorkflowsamplesubtypeId (final Integer workflowsamplesubtypeId) {
    		HashMap m = new HashMap();
		m.put("workflowsamplesubtypeId", workflowsamplesubtypeId);

		List<WorkflowSampleSubtype> results = this.findByMap(m);

		if (results.size() == 0) {
			WorkflowSampleSubtype rt = new WorkflowSampleSubtype();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getWorkflowsamplesubtypeByWorkflowIdSampleSubtypeId(final Integer workflowId, final Integer sampleSubtypeId)
	 *
	 * @param final Integer workflowId, final Integer sampleSubtypeId
	 *
	 * @return workflowSampleSubtype
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public WorkflowSampleSubtype getWorkflowSampleSubtypeByWorkflowIdSampleSubtypeId (final Integer workflowId, final Integer sampleSubtypeId) {
    		HashMap m = new HashMap();
		m.put("workflowId", workflowId);
		m.put("sampleSubtypeId", sampleSubtypeId);

		List<WorkflowSampleSubtype> results = this.findByMap(m);

		if (results.size() == 0) {
			WorkflowSampleSubtype rt = new WorkflowSampleSubtype();
			return rt;
		}
		return results.get(0);
	}



}

