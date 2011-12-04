
/**
 *
 * WorkflowtyperesourceService.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowtyperesourceService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.WorkflowtyperesourceDao;
import edu.yu.einstein.wasp.model.Workflowtyperesource;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface WorkflowtyperesourceService extends WaspService<Workflowtyperesource> {

	/**
	 * setWorkflowtyperesourceDao(WorkflowtyperesourceDao workflowtyperesourceDao)
	 *
	 * @param workflowtyperesourceDao
	 *
	 */
	public void setWorkflowtyperesourceDao(WorkflowtyperesourceDao workflowtyperesourceDao);

	/**
	 * getWorkflowtyperesourceDao();
	 *
	 * @return workflowtyperesourceDao
	 *
	 */
	public WorkflowtyperesourceDao getWorkflowtyperesourceDao();

  public Workflowtyperesource getWorkflowtyperesourceByWorkflowtyperesourceId (final int workflowtyperesourceId);

  public Workflowtyperesource getWorkflowtyperesourceByWorkflowIdTypeResourceId (final int workflowId, final int typeResourceId);


}

