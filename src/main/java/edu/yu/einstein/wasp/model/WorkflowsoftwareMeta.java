
/**
 *
 * WorkflowsoftwareMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowsoftwareMeta
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="workflowsoftwaremeta")
public class WorkflowsoftwareMeta extends MetaBase {

	/** 
	 * workflowsoftwareMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer workflowsoftwareMetaId;

	/**
	 * setWorkflowsoftwareMetaId(Integer workflowsoftwareMetaId)
	 *
	 * @param workflowsoftwareMetaId
	 *
	 */
	
	public void setWorkflowsoftwareMetaId (Integer workflowsoftwareMetaId) {
		this.workflowsoftwareMetaId = workflowsoftwareMetaId;
	}

	/**
	 * getWorkflowsoftwareMetaId()
	 *
	 * @return workflowsoftwareMetaId
	 *
	 */
	public Integer getWorkflowsoftwareMetaId () {
		return this.workflowsoftwareMetaId;
	}




	/** 
	 * workflowsoftwareId
	 *
	 */
	@Column(name="workflowsoftwareid")
	protected Integer workflowsoftwareId;

	/**
	 * setWorkflowsoftwareId(Integer workflowsoftwareId)
	 *
	 * @param workflowsoftwareId
	 *
	 */
	
	public void setWorkflowsoftwareId (Integer workflowsoftwareId) {
		this.workflowsoftwareId = workflowsoftwareId;
	}

	/**
	 * getWorkflowsoftwareId()
	 *
	 * @return workflowsoftwareId
	 *
	 */
	public Integer getWorkflowsoftwareId () {
		return this.workflowsoftwareId;
	}




	/**
	 * workflowSoftware
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="workflowsoftwareid", insertable=false, updatable=false)
	protected WorkflowSoftware workflowSoftware;

	/**
	 * setWorkflowSoftware (WorkflowSoftware workflowSoftware)
	 *
	 * @param workflowSoftware
	 *
	 */
	public void setWorkflowSoftware (WorkflowSoftware workflowSoftware) {
		this.workflowSoftware = workflowSoftware;
		this.workflowsoftwareId = workflowSoftware.workflowSoftwareId;
	}

	/**
	 * getWorkflowSoftware ()
	 *
	 * @return workflowSoftware
	 *
	 */
	
	public WorkflowSoftware getWorkflowSoftware () {
		return this.workflowSoftware;
	}


}
