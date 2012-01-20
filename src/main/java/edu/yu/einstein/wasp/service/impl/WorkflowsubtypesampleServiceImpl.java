
/**
 *
 * WorkflowsubtypesampleServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowsubtypesampleService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.WorkflowsubtypesampleService;
import edu.yu.einstein.wasp.dao.WorkflowsubtypesampleDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Workflowsubtypesample;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkflowsubtypesampleServiceImpl extends WaspServiceImpl<Workflowsubtypesample> implements WorkflowsubtypesampleService {

	/**
	 * workflowsubtypesampleDao;
	 *
	 */
	private WorkflowsubtypesampleDao workflowsubtypesampleDao;

	/**
	 * setWorkflowsubtypesampleDao(WorkflowsubtypesampleDao workflowsubtypesampleDao)
	 *
	 * @param workflowsubtypesampleDao
	 *
	 */
	@Autowired
	public void setWorkflowsubtypesampleDao(WorkflowsubtypesampleDao workflowsubtypesampleDao) {
		this.workflowsubtypesampleDao = workflowsubtypesampleDao;
		this.setWaspDao(workflowsubtypesampleDao);
	}

	/**
	 * getWorkflowsubtypesampleDao();
	 *
	 * @return workflowsubtypesampleDao
	 *
	 */
	public WorkflowsubtypesampleDao getWorkflowsubtypesampleDao() {
		return this.workflowsubtypesampleDao;
	}


  public Workflowsubtypesample getWorkflowsubtypesampleByWorkflowsubtypesampleId (final Integer workflowsubtypesampleId) {
    return this.getWorkflowsubtypesampleDao().getWorkflowsubtypesampleByWorkflowsubtypesampleId(workflowsubtypesampleId);
  }

  public Workflowsubtypesample getWorkflowsubtypesampleByWorkflowIdSubtypeSampleId (final Integer workflowId, final Integer subtypeSampleId) {
    return this.getWorkflowsubtypesampleDao().getWorkflowsubtypesampleByWorkflowIdSubtypeSampleId(workflowId, subtypeSampleId);
  }

}

