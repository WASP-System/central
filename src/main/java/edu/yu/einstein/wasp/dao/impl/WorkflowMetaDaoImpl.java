
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
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

	@SuppressWarnings("unchecked")
	@Transactional
	public WorkflowMeta getWorkflowMetaByWorkflowMetaId (final int workflowMetaId) {
    		HashMap m = new HashMap();
		m.put("workflowMetaId", workflowMetaId);

		List<WorkflowMeta> results = (List<WorkflowMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			WorkflowMeta rt = new WorkflowMeta();
			return rt;
		}
		return (WorkflowMeta) results.get(0);
	}



	/**
	 * getWorkflowMetaByKWorkflowId(final String k, final int workflowId)
	 *
	 * @param final String k, final int workflowId
	 *
	 * @return workflowMeta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public WorkflowMeta getWorkflowMetaByKWorkflowId (final String k, final int workflowId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("workflowId", workflowId);

		List<WorkflowMeta> results = (List<WorkflowMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			WorkflowMeta rt = new WorkflowMeta();
			return rt;
		}
		return (WorkflowMeta) results.get(0);
	}



	/**
	 * updateByWorkflowId (final int workflowId, final List<WorkflowMeta> metaList)
	 *
	 * @param workflowId
	 * @param metaList
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByWorkflowId (final int workflowId, final List<WorkflowMeta> metaList) {

		getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				em.createNativeQuery("delete from workflowMeta where workflowId=:workflowId").setParameter("workflowId", workflowId).executeUpdate();

				for (WorkflowMeta m:metaList) {
					m.setWorkflowId(workflowId);
					em.persist(m);
				}
        			return null;
			}
		});
	}



}

