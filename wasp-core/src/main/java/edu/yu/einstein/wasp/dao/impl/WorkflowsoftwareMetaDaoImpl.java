
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

import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.model.WorkflowsoftwareMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class WorkflowsoftwareMetaDaoImpl extends WaspDaoImpl<WorkflowsoftwareMeta> implements edu.yu.einstein.wasp.dao.WorkflowsoftwareMetaDao {

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
	@SuppressWarnings("unchecked")
	@Transactional
	public WorkflowsoftwareMeta getWorkflowsoftwareMetaByWorkflowsoftwareMetaId (final Integer workflowsoftwareMetaId) {
    		HashMap m = new HashMap();
		m.put("workflowsoftwareMetaId", workflowsoftwareMetaId);

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
	@SuppressWarnings("unchecked")
	@Transactional
	public WorkflowsoftwareMeta getWorkflowsoftwareMetaByWorkflowsoftwareIdK (final Integer workflowsoftwareId, final String k) {
    		HashMap m = new HashMap();
		m.put("workflowsoftwareId", workflowsoftwareId);
		m.put("k", k);

		List<WorkflowsoftwareMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			WorkflowsoftwareMeta rt = new WorkflowsoftwareMeta();
			return rt;
		}
		return results.get(0);
	}
	
	/**
	 * updateByWorkflowsoftwareId (final int workflowsoftwareId, final List<WorkflowsoftwareMeta> metaList)
	 *
	 * @param workflowId
	 * @param metaList
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByWorkflowsoftwareId (final int workflowsoftwareId, final List<WorkflowsoftwareMeta> metaList) {
		for (WorkflowsoftwareMeta m:metaList) {
			WorkflowsoftwareMeta currentMeta = getWorkflowsoftwareMetaByWorkflowsoftwareIdK(workflowsoftwareId, m.getK());
			if (currentMeta.getWorkflowsoftwareMetaId() == null){
				// metadata value not in database yet
				m.setWorkflowsoftwareId(workflowsoftwareId);
				entityManager.persist(m);
			} else if (!currentMeta.getV().equals(m.getV())){
				// meta exists already but value has changed
				currentMeta.setV(m.getV());
				entityManager.merge(currentMeta);
			} else{
				// no change to meta so do nothing
			}
		}
	}




}

