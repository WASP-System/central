
/**
 *
 * WorkflowSoftware.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowSoftware
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
@Table(name="workflowsoftware")
public class WorkflowSoftware extends WaspModel {

	/** 
	 * workflowSoftwareId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer workflowSoftwareId;

	/**
	 * setWorkflowSoftwareId(Integer workflowSoftwareId)
	 *
	 * @param workflowSoftwareId
	 *
	 */
	
	public void setWorkflowSoftwareId (Integer workflowSoftwareId) {
		this.workflowSoftwareId = workflowSoftwareId;
	}

	/**
	 * getWorkflowSoftwareId()
	 *
	 * @return workflowSoftwareId
	 *
	 */
	public Integer getWorkflowSoftwareId () {
		return this.workflowSoftwareId;
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
	 * softwareId
	 *
	 */
	@Column(name="softwareid")
	protected Integer softwareId;

	/**
	 * setSoftwareId(Integer softwareId)
	 *
	 * @param softwareId
	 *
	 */
	
	public void setSoftwareId (Integer softwareId) {
		this.softwareId = softwareId;
	}

	/**
	 * getSoftwareId()
	 *
	 * @return softwareId
	 *
	 */
	public Integer getSoftwareId () {
		return this.softwareId;
	}




	/**
	 * software
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="softwareid", insertable=false, updatable=false)
	protected Software software;

	/**
	 * setSoftware (Software software)
	 *
	 * @param software
	 *
	 */
	public void setSoftware (Software software) {
		this.software = software;
		this.softwareId = software.softwareId;
	}

	/**
	 * getSoftware ()
	 *
	 * @return software
	 *
	 */
	
	public Software getSoftware () {
		return this.software;
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


	/** 
	 * workflowsoftwareMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="workflowsoftwareid", insertable=false, updatable=false)
	protected List<WorkflowsoftwareMeta> workflowsoftwareMeta;


	/** 
	 * getWorkflowsoftwareMeta()
	 *
	 * @return workflowsoftwareMeta
	 *
	 */
	public List<WorkflowsoftwareMeta> getWorkflowsoftwareMeta() {
		return this.workflowsoftwareMeta;
	}


	/** 
	 * setWorkflowsoftwareMeta
	 *
	 * @param workflowsoftwareMeta
	 *
	 */
	public void setWorkflowsoftwareMeta (List<WorkflowsoftwareMeta> workflowsoftwareMeta) {
		this.workflowsoftwareMeta = workflowsoftwareMeta;
	}



}
