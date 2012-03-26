
/**
 *
 * WorkflowsamplesubtypeDao.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowSampleSubtype Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.WorkflowSampleSubtype;


public interface WorkflowSampleSubtypeDao extends WaspDao<WorkflowSampleSubtype> {

  public WorkflowSampleSubtype getWorkflowSampleSubtypeByWorkflowsamplesubtypeId (final Integer workflowsamplesubtypeId);

  public WorkflowSampleSubtype getWorkflowSampleSubtypeByWorkflowIdSampleSubtypeId (final Integer workflowId, final Integer sampleSubtypeId);


}

