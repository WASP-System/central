
/**
 *
 * WorkflowMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowMeta
 *
 *
 */

package edu.yu.einstein.wasp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="workflowmeta")
public class WorkflowMeta extends MetaBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7886471727843314897L;

	/**
	 * setWorkflowMetaId(Integer workflowMetaId)
	 *
	 * @param workflowMetaId
	 *
	 */
	@Deprecated
	public void setWorkflowMetaId (Integer workflowMetaId) {
		setId(workflowMetaId);
	}

	/**
	 * getWorkflowMetaId()
	 *
	 * @return workflowMetaId
	 *
	 */
	@Deprecated
	public Integer getWorkflowMetaId () {
		return getId();
	}




	/** 
	 * workflowId
	 *
	 */
	@Column(name="workflowid")
	protected Integer workflowId;

	/**
	 * setWorkflowId(Integer workflowId)
	 *
	 * @param workflowId
	 *
	 */
	
	public void setWorkflowId (Integer workflowId) {
		this.workflowId = workflowId;
	}

	/**
	 * getWorkflowId()
	 *
	 * @return workflowId
	 *
	 */
	public Integer getWorkflowId () {
		return this.workflowId;
	}




	/**
	 * workflow
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="workflowid", insertable=false, updatable=false)
	protected Workflow workflow;

	/**
	 * setWorkflow (Workflow workflow)
	 *
	 * @param workflow
	 *
	 */
	public void setWorkflow (Workflow workflow) {
		this.workflow = workflow;
		this.workflowId = workflow.getId();
	}

	/**
	 * getWorkflow ()
	 *
	 * @return workflow
	 *
	 */
	
	public Workflow getWorkflow () {
		return this.workflow;
	}


}
