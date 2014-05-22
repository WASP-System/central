
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
@Table(name="workflowresourcecategory")
public class Workflowresourcecategory extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4091500561560541120L;
	

	/**
	 * setWorkflowresourcecategoryId(Integer workflowresourcecategoryId)
	 *
	 * @param workflowresourcecategoryId
	 *
	 */
	@Deprecated
	public void setWorkflowresourcecategoryId (Integer workflowresourcecategoryId) {
		setId(workflowresourcecategoryId);
	}

	/**
	 * getWorkflowresourcecategoryId()
	 *
	 * @return workflowresourcecategoryId
	 *
	 */
	@Deprecated
	public Integer getWorkflowresourcecategoryId () {
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
		this.resourcecategoryId = resourceCategory.getId();
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
	@JsonIgnore
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
