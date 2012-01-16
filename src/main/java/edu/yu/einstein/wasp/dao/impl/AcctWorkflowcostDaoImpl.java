
/**
 *
 * AcctWorkflowcostDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctWorkflowcost Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AcctWorkflowcost;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AcctWorkflowcostDaoImpl extends WaspDaoImpl<AcctWorkflowcost> implements edu.yu.einstein.wasp.dao.AcctWorkflowcostDao {

	/**
	 * AcctWorkflowcostDaoImpl() Constructor
	 *
	 *
	 */
	public AcctWorkflowcostDaoImpl() {
		super();
		this.entityClass = AcctWorkflowcost.class;
	}


	/**
	 * getAcctWorkflowcostByWorkflowId(final int workflowId)
	 *
	 * @param final int workflowId
	 *
	 * @return acctWorkflowcost
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public AcctWorkflowcost getAcctWorkflowcostByWorkflowId (final int workflowId) {
    		HashMap m = new HashMap();
		m.put("workflowId", workflowId);

		List<AcctWorkflowcost> results = this.findByMap(m);

		if (results.size() == 0) {
			AcctWorkflowcost rt = new AcctWorkflowcost();
			return rt;
		}
		return results.get(0);
	}



}

