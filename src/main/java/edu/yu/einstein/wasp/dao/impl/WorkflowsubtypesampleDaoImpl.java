
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
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
	 * getWorkflowsubtypesampleByWorkflowsubtypesampleId(final int workflowsubtypesampleId)
	 *
	 * @param final int workflowsubtypesampleId
	 *
	 * @return workflowsubtypesample
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Workflowsubtypesample getWorkflowsubtypesampleByWorkflowsubtypesampleId (final int workflowsubtypesampleId) {
    		HashMap m = new HashMap();
		m.put("workflowsubtypesampleId", workflowsubtypesampleId);

		List<Workflowsubtypesample> results = (List<Workflowsubtypesample>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Workflowsubtypesample rt = new Workflowsubtypesample();
			return rt;
		}
		return (Workflowsubtypesample) results.get(0);
	}



	/**
	 * getWorkflowsubtypesampleByWorkflowIdSubtypeSampleId(final int workflowId, final int subtypeSampleId)
	 *
	 * @param final int workflowId, final int subtypeSampleId
	 *
	 * @return workflowsubtypesample
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Workflowsubtypesample getWorkflowsubtypesampleByWorkflowIdSubtypeSampleId (final int workflowId, final int subtypeSampleId) {
    		HashMap m = new HashMap();
		m.put("workflowId", workflowId);
		m.put("subtypeSampleId", subtypeSampleId);

		List<Workflowsubtypesample> results = (List<Workflowsubtypesample>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Workflowsubtypesample rt = new Workflowsubtypesample();
			return rt;
		}
		return (Workflowsubtypesample) results.get(0);
	}



}

