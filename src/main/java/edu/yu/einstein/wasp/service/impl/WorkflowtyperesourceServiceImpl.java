
/**
 *
 * WorkflowtyperesourceServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowtyperesourceService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.WorkflowtyperesourceDao;
import edu.yu.einstein.wasp.model.Workflowtyperesource;
import edu.yu.einstein.wasp.service.WorkflowtyperesourceService;

@Service
public class WorkflowtyperesourceServiceImpl extends WaspServiceImpl<Workflowtyperesource> implements WorkflowtyperesourceService {

	/**
	 * workflowtyperesourceDao;
	 *
	 */
	private WorkflowtyperesourceDao workflowtyperesourceDao;

	/**
	 * setWorkflowtyperesourceDao(WorkflowtyperesourceDao workflowtyperesourceDao)
	 *
	 * @param workflowtyperesourceDao
	 *
	 */
	@Override
	@Autowired
	public void setWorkflowtyperesourceDao(WorkflowtyperesourceDao workflowtyperesourceDao) {
		this.workflowtyperesourceDao = workflowtyperesourceDao;
		this.setWaspDao(workflowtyperesourceDao);
	}

	/**
	 * getWorkflowtyperesourceDao();
	 *
	 * @return workflowtyperesourceDao
	 *
	 */
	@Override
	public WorkflowtyperesourceDao getWorkflowtyperesourceDao() {
		return this.workflowtyperesourceDao;
	}


  @Override
public Workflowtyperesource getWorkflowtyperesourceByWorkflowtyperesourceId (final Integer workflowtyperesourceId) {
    return this.getWorkflowtyperesourceDao().getWorkflowtyperesourceByWorkflowtyperesourceId(workflowtyperesourceId);
  }

  @Override
public Workflowtyperesource getWorkflowtyperesourceByWorkflowIdTypeResourceId (final Integer workflowId, final Integer typeResourceId) {
    return this.getWorkflowtyperesourceDao().getWorkflowtyperesourceByWorkflowIdTypeResourceId(workflowId, typeResourceId);
  }

}

