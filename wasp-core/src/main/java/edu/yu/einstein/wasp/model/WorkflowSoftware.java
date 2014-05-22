
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

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="workflowsoftware")
public class WorkflowSoftware extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8796895488617847611L;
	

	/**
	 * setWorkflowSoftwareId(Integer workflowSoftwareId)
	 *
	 * @param workflowSoftwareId
	 *
	 */
	@Deprecated
	public void setWorkflowSoftwareId (Integer workflowSoftwareId) {
		setId(workflowSoftwareId);
	}

	/**
	 * getWorkflowSoftwareId()
	 *
	 * @return workflowSoftwareId
	 *
	 */
	@Deprecated
	public Integer getWorkflowSoftwareId () {
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
		this.softwareId = software.getId();
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
	@JsonIgnore
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
