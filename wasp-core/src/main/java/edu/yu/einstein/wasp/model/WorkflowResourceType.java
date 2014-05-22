
/**
 *
 * WorkflowResourceType.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowResourceType
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
@Table(name="workflowresourcetype")
public class WorkflowResourceType extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1722076911299522161L;
	
	/**
	 * setWorkflowresourcetypeId(Integer workflowresourcetypeId)
	 *
	 * @param workflowresourcetypeId
	 *
	 */
	@Deprecated
	public void setWorkflowresourcetypeId (Integer workflowresourcetypeId) {
		setId(workflowresourcetypeId);
	}

	/**
	 * getWorkflowresourcetypeId()
	 *
	 * @return workflowresourcetypeId
	 *
	 */
	@Deprecated
	public Integer getWorkflowresourcetypeId () {
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
	 * resourceTypeId
	 *
	 */
	@Column(name="resourcetypeid")
	protected Integer resourceTypeId;

	/**
	 * setResourceTypeId(Integer resourceTypeId)
	 *
	 * @param resourceTypeId
	 *
	 */
	
	public void setResourceTypeId (Integer resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	/**
	 * getResourceTypeId()
	 *
	 * @return resourceTypeId
	 *
	 */
	public Integer getResourceTypeId () {
		return this.resourceTypeId;
	}




	/**
	 * resourceType
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="resourcetypeid", insertable=false, updatable=false)
	protected ResourceType resourceType;

	/**
	 * setResourceType (ResourceType resourceType)
	 *
	 * @param resourceType
	 *
	 */
	public void setResourceType (ResourceType resourceType) {
		this.resourceType = resourceType;
		this.resourceTypeId = resourceType.getId();
	}

	/**
	 * getResourceType ()
	 *
	 * @return resourceType
	 *
	 */
	
	public ResourceType getResourceType () {
		return this.resourceType;
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
