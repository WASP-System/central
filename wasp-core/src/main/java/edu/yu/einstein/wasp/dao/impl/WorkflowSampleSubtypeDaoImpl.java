
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


@Transactional("entityManager")
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
	@Transactional("entityManager")
	public WorkflowSampleSubtype getWorkflowSampleSubtypeByWorkflowsamplesubtypeId (final Integer workflowsamplesubtypeId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", workflowsamplesubtypeId);

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
	@Transactional("entityManager")
	public WorkflowSampleSubtype getWorkflowSampleSubtypeByWorkflowIdSampleSubtypeId (final Integer workflowId, final Integer sampleSubtypeId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
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

