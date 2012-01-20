
/**
 *
 * Workflowresourcecategory.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowresourcecategory
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
@Table(name="workflowresourcecategory")
public class Workflowresourcecategory extends WaspModel {

	/** 
	 * workflowresourcecategoryId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer workflowresourcecategoryId;

	/**
	 * setWorkflowresourcecategoryId(Integer workflowresourcecategoryId)
	 *
	 * @param workflowresourcecategoryId
	 *
	 */
	
	public void setWorkflowresourcecategoryId (Integer workflowresourcecategoryId) {
		this.workflowresourcecategoryId = workflowresourcecategoryId;
	}

	/**
	 * getWorkflowresourcecategoryId()
	 *
	 * @return workflowresourcecategoryId
	 *
	 */
	public Integer getWorkflowresourcecategoryId () {
		return this.workflowresourcecategoryId;
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
	 * resourcecategoryId
	 *
	 */
	@Column(name="resourcecategoryid")
	protected Integer resourcecategoryId;

	/**
	 * setResourcecategoryId(Integer resourcecategoryId)
	 *
	 * @param resourcecategoryId
	 *
	 */
	
	public void setResourcecategoryId (Integer resourcecategoryId) {
		this.resourcecategoryId = resourcecategoryId;
	}

	/**
	 * getResourcecategoryId()
	 *
	 * @return resourcecategoryId
	 *
	 */
	public Integer getResourcecategoryId () {
		return this.resourcecategoryId;
	}




	/**
	 * resourceCategory
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="resourcecategoryid", insertable=false, updatable=false)
	protected ResourceCategory resourceCategory;

	/**
	 * setResourceCategory (ResourceCategory resourceCategory)
	 *
	 * @param resourceCategory
	 *
	 */
	public void setResourceCategory (ResourceCategory resourceCategory) {
		this.resourceCategory = resourceCategory;
		this.resourcecategoryId = resourceCategory.resourceCategoryId;
	}

	/**
	 * getResourceCategory ()
	 *
	 * @return resourceCategory
	 *
	 */
	
	public ResourceCategory getResourceCategory () {
		return this.resourceCategory;
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
	 * workflowresourcecategoryMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="workflowresourcecategoryid", insertable=false, updatable=false)
	protected List<WorkflowresourcecategoryMeta> workflowresourcecategoryMeta;


	/** 
	 * getWorkflowresourcecategoryMeta()
	 *
	 * @return workflowresourcecategoryMeta
	 *
	 */
	public List<WorkflowresourcecategoryMeta> getWorkflowresourcecategoryMeta() {
		return this.workflowresourcecategoryMeta;
	}


	/** 
	 * setWorkflowresourcecategoryMeta
	 *
	 * @param workflowresourcecategoryMeta
	 *
	 */
	public void setWorkflowresourcecategoryMeta (List<WorkflowresourcecategoryMeta> workflowresourcecategoryMeta) {
		this.workflowresourcecategoryMeta = workflowresourcecategoryMeta;
	}



}
