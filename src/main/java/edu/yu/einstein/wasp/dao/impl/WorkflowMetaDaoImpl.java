
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

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class WorkflowMetaDaoImpl extends WaspDaoImpl<WorkflowMeta> implements edu.yu.einstein.wasp.dao.WorkflowMetaDao {

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
	 * getWorkflowMetaByWorkflowMetaId(final int workflowMetaId)
	 *
	 * @param final int workflowMetaId
	 *
	 * @return workflowMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public WorkflowMeta getWorkflowMetaByWorkflowMetaId (final int workflowMetaId) {
    		HashMap m = new HashMap();
		m.put("workflowMetaId", workflowMetaId);

		List<WorkflowMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			WorkflowMeta rt = new WorkflowMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getWorkflowMetaByKWorkflowId(final String k, final int workflowId)
	 *
	 * @param final String k, final int workflowId
	 *
	 * @return workflowMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public WorkflowMeta getWorkflowMetaByKWorkflowId (final String k, final int workflowId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("workflowId", workflowId);

		List<WorkflowMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			WorkflowMeta rt = new WorkflowMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * updateByWorkflowId (final string area, final int workflowId, final List<WorkflowMeta> metaList)
	 *
	 * @param workflowId
	 * @param metaList
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByWorkflowId (final String area, final int workflowId, final List<WorkflowMeta> metaList) {
		entityManager.createNativeQuery("delete from workflowmeta where workflowId=:workflowId and k like :area").setParameter("workflowId", workflowId).setParameter("area", area + ".%").executeUpdate();

		for (WorkflowMeta m:metaList) {
			m.setWorkflowId(workflowId);
			entityManager.persist(m);
		}
 	}


	/**
	 * updateByWorkflowId (final int workflowId, final List<WorkflowMeta> metaList)
	 *
	 * @param workflowId
	 * @param metaList
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByWorkflowId (final int workflowId, final List<WorkflowMeta> metaList) {
		entityManager.createNativeQuery("delete from workflowmeta where workflowId=:workflowId").setParameter("workflowId", workflowId).executeUpdate();

		for (WorkflowMeta m:metaList) {
			m.setWorkflowId(workflowId);
			entityManager.persist(m);
		}
	}



}

