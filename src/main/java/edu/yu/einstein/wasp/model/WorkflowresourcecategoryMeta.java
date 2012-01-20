
/**
 *
 * WorkflowresourcecategoryMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowresourcecategoryMeta
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
@Table(name="workflowresourcecategorymeta")
public class WorkflowresourcecategoryMeta extends MetaBase {

	/** 
	 * workflowresourcecategoryMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer workflowresourcecategoryMetaId;

	/**
	 * setWorkflowresourcecategoryMetaId(Integer workflowresourcecategoryMetaId)
	 *
	 * @param workflowresourcecategoryMetaId
	 *
	 */
	
	public void setWorkflowresourcecategoryMetaId (Integer workflowresourcecategoryMetaId) {
		this.workflowresourcecategoryMetaId = workflowresourcecategoryMetaId;
	}

	/**
	 * getWorkflowresourcecategoryMetaId()
	 *
	 * @return workflowresourcecategoryMetaId
	 *
	 */
	public Integer getWorkflowresourcecategoryMetaId () {
		return this.workflowresourcecategoryMetaId;
	}




	/** 
	 * workflowresourcecategoryId
	 *
	 */
	@Column(name="workflowresourcecategoryid")
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
	 * workflowresourcecategory
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="workflowresourcecategoryid", insertable=false, updatable=false)
	protected Workflowresourcecategory workflowresourcecategory;

	/**
	 * setWorkflowresourcecategory (Workflowresourcecategory workflowresourcecategory)
	 *
	 * @param workflowresourcecategory
	 *
	 */
	public void setWorkflowresourcecategory (Workflowresourcecategory workflowresourcecategory) {
		this.workflowresourcecategory = workflowresourcecategory;
		this.workflowresourcecategoryId = workflowresourcecategory.workflowresourcecategoryId;
	}

	/**
	 * getWorkflowresourcecategory ()
	 *
	 * @return workflowresourcecategory
	 *
	 */
	
	public Workflowresourcecategory getWorkflowresourcecategory () {
		return this.workflowresourcecategory;
	}


}
