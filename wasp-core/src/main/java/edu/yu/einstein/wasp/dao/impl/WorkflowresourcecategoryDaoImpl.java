
/**
 *
 * WorkflowresourcecategoryDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowresourcecategory Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Workflowresourcecategory;


@Transactional
@Repository
public class WorkflowresourcecategoryDaoImpl extends WaspDaoImpl<Workflowresourcecategory> implements edu.yu.einstein.wasp.dao.WorkflowresourcecategoryDao {

	/**
	 * WorkflowresourcecategoryDaoImpl() Constructor
	 *
	 *
	 */
	public WorkflowresourcecategoryDaoImpl() {
		super();
		this.entityClass = Workflowresourcecategory.class;
	}


	/**
	 * getWorkflowresourcecategoryByWorkflowresourcecategoryId(final Integer workflowresourcecategoryId)
	 *
	 * @param final Integer workflowresourcecategoryId
	 *
	 * @return workflowresourcecategory
	 */

	@Override
	@Transactional
	public Workflowresourcecategory getWorkflowresourcecategoryByWorkflowresourcecategoryId (final Integer workflowresourcecategoryId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("workflowresourcecategoryId", workflowresourcecategoryId);

		List<Workflowresourcecategory> results = this.findByMap(m);

		if (results.size() == 0) {
			Workflowresourcecategory rt = new Workflowresourcecategory();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getWorkflowresourcecategoryByWorkflowIdResourcecategoryId(final Integer workflowId, final Integer resourcecategoryId)
	 *
	 * @param final Integer workflowId, final Integer resourcecategoryId
	 *
	 * @return workflowresourcecategory
	 */

	@Override
	@Transactional
	public Workflowresourcecategory getWorkflowresourcecategoryByWorkflowIdResourcecategoryId (final Integer workflowId, final Integer resourcecategoryId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("workflowId", workflowId);
		m.put("resourcecategoryId", resourcecategoryId);

		List<Workflowresourcecategory> results = this.findByMap(m);

		if (results.size() == 0) {
			Workflowresourcecategory rt = new Workflowresourcecategory();
			return rt;
		}
		return results.get(0);
	}



}

