
/**
 *
 * Workflowtyperesource.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowtyperesource
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
@Table(name="workflowtyperesource")
public class Workflowtyperesource extends WaspModel {

	/** 
	 * workflowtyperesourceId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int workflowtyperesourceId;

	/**
	 * setWorkflowtyperesourceId(int workflowtyperesourceId)
	 *
	 * @param workflowtyperesourceId
	 *
	 */
	
	public void setWorkflowtyperesourceId (int workflowtyperesourceId) {
		this.workflowtyperesourceId = workflowtyperesourceId;
	}

	/**
	 * getWorkflowtyperesourceId()
	 *
	 * @return workflowtyperesourceId
	 *
	 */
	public int getWorkflowtyperesourceId () {
		return this.workflowtyperesourceId;
	}




	/** 
	 * workflowId
	 *
	 */
	@Column(name="workflowid")
	protected int workflowId;

	/**
	 * setWorkflowId(int workflowId)
	 *
	 * @param workflowId
	 *
	 */
	
	public void setWorkflowId (int workflowId) {
		this.workflowId = workflowId;
	}

	/**
	 * getWorkflowId()
	 *
	 * @return workflowId
	 *
	 */
	public int getWorkflowId () {
		return this.workflowId;
	}




	/** 
	 * typeResourceId
	 *
	 */
	@Column(name="typeresourceid")
	protected int typeResourceId;

	/**
	 * setTypeResourceId(int typeResourceId)
	 *
	 * @param typeResourceId
	 *
	 */
	
	public void setTypeResourceId (int typeResourceId) {
		this.typeResourceId = typeResourceId;
	}

	/**
	 * getTypeResourceId()
	 *
	 * @return typeResourceId
	 *
	 */
	public int getTypeResourceId () {
		return this.typeResourceId;
	}




	/**
	 * typeResource
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="typeresourceid", insertable=false, updatable=false)
	protected TypeResource typeResource;

	/**
	 * setTypeResource (TypeResource typeResource)
	 *
	 * @param typeResource
	 *
	 */
	public void setTypeResource (TypeResource typeResource) {
		this.typeResource = typeResource;
		this.typeResourceId = typeResource.typeResourceId;
	}

	/**
	 * getTypeResource ()
	 *
	 * @return typeResource
	 *
	 */
	
	public TypeResource getTypeResource () {
		return this.typeResource;
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
