
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
	public WorkflowtyperesourceDao getWorkflowtyperesourceDao() {
		return this.workflowtyperesourceDao;
	}


  public Workflowtyperesource getWorkflowtyperesourceByWorkflowtyperesourceId (final int workflowtyperesourceId) {
    return this.getWorkflowtyperesourceDao().getWorkflowtyperesourceByWorkflowtyperesourceId(workflowtyperesourceId);
  }

  public Workflowtyperesource getWorkflowtyperesourceByWorkflowIdTypeResourceId (final int workflowId, final int typeResourceId) {
    return this.getWorkflowtyperesourceDao().getWorkflowtyperesourceByWorkflowIdTypeResourceId(workflowId, typeResourceId);
  }

}

