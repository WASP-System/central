
/**
 *
 * WorkflowtyperesourceDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowtyperesource Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Workflowtyperesource;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class WorkflowtyperesourceDaoImpl extends WaspDaoImpl<Workflowtyperesource> implements edu.yu.einstein.wasp.dao.WorkflowtyperesourceDao {

	/**
	 * WorkflowtyperesourceDaoImpl() Constructor
	 *
	 *
	 */
	public WorkflowtyperesourceDaoImpl() {
		super();
		this.entityClass = Workflowtyperesource.class;
	}


	/**
	 * getWorkflowtyperesourceByWorkflowtyperesourceId(final int workflowtyperesourceId)
	 *
	 * @param final int workflowtyperesourceId
	 *
	 * @return workflowtyperesource
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Workflowtyperesource getWorkflowtyperesourceByWorkflowtyperesourceId (final int workflowtyperesourceId) {
    		HashMap m = new HashMap();
		m.put("workflowtyperesourceId", workflowtyperesourceId);

		List<Workflowtyperesource> results = this.findByMap(m);

		if (results.size() == 0) {
			Workflowtyperesource rt = new Workflowtyperesource();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getWorkflowtyperesourceByWorkflowIdTypeResourceId(final int workflowId, final int typeResourceId)
	 *
	 * @param final int workflowId, final int typeResourceId
	 *
	 * @return workflowtyperesource
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Workflowtyperesource getWorkflowtyperesourceByWorkflowIdTypeResourceId (final int workflowId, final int typeResourceId) {
    		HashMap m = new HashMap();
		m.put("workflowId", workflowId);
		m.put("typeResourceId", typeResourceId);

		List<Workflowtyperesource> results = this.findByMap(m);

		if (results.size() == 0) {
			Workflowtyperesource rt = new Workflowtyperesource();
			return rt;
		}
		return results.get(0);
	}



}

