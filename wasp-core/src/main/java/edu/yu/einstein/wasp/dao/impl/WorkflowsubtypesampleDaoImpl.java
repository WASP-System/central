
/**
 *
 * WorkflowsubtypesampleDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowsubtypesample Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Workflowsubtypesample;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class WorkflowsubtypesampleDaoImpl extends WaspDaoImpl<Workflowsubtypesample> implements edu.yu.einstein.wasp.dao.WorkflowsubtypesampleDao {

	/**
	 * WorkflowsubtypesampleDaoImpl() Constructor
	 *
	 *
	 */
	public WorkflowsubtypesampleDaoImpl() {
		super();
		this.entityClass = Workflowsubtypesample.class;
	}


	/**
	 * getWorkflowsubtypesampleByWorkflowsubtypesampleId(final Integer workflowsubtypesampleId)
	 *
	 * @param final Integer workflowsubtypesampleId
	 *
	 * @return workflowsubtypesample
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Workflowsubtypesample getWorkflowsubtypesampleByWorkflowsubtypesampleId (final Integer workflowsubtypesampleId) {
    		HashMap m = new HashMap();
		m.put("workflowsubtypesampleId", workflowsubtypesampleId);

		List<Workflowsubtypesample> results = this.findByMap(m);

		if (results.size() == 0) {
			Workflowsubtypesample rt = new Workflowsubtypesample();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getWorkflowsubtypesampleByWorkflowIdSubtypeSampleId(final Integer workflowId, final Integer subtypeSampleId)
	 *
	 * @param final Integer workflowId, final Integer subtypeSampleId
	 *
	 * @return workflowsubtypesample
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Workflowsubtypesample getWorkflowsubtypesampleByWorkflowIdSubtypeSampleId (final Integer workflowId, final Integer subtypeSampleId) {
    		HashMap m = new HashMap();
		m.put("workflowId", workflowId);
		m.put("subtypeSampleId", subtypeSampleId);

		List<Workflowsubtypesample> results = this.findByMap(m);

		if (results.size() == 0) {
			Workflowsubtypesample rt = new Workflowsubtypesample();
			return rt;
		}
		return results.get(0);
	}



}

