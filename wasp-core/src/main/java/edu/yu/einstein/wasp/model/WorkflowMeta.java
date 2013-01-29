
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	 * workflowMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer workflowMetaId;

	/**
	 * setWorkflowMetaId(Integer workflowMetaId)
	 *
	 * @param workflowMetaId
	 *
	 */
	
	public void setWorkflowMetaId (Integer workflowMetaId) {
		this.workflowMetaId = workflowMetaId;
	}

	/**
	 * getWorkflowMetaId()
	 *
	 * @return workflowMetaId
	 *
	 */
	public Integer getWorkflowMetaId () {
		return this.workflowMetaId;
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
		this.workflowId = workflow.workflowId;
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
