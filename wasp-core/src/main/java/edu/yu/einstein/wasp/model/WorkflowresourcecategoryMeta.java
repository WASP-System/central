
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="workflowresourcecategorymeta")
public class WorkflowresourcecategoryMeta extends MetaBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8413076598054141252L;

	/**
	 * setWorkflowresourcecategoryMetaId(Integer workflowresourcecategoryMetaId)
	 *
	 * @param workflowresourcecategoryMetaId
	 *
	 */
	@Deprecated
	public void setWorkflowresourcecategoryMetaId (Integer workflowresourcecategoryMetaId) {
		setId(workflowresourcecategoryMetaId);
	}

	/**
	 * getWorkflowresourcecategoryMetaId()
	 *
	 * @return workflowresourcecategoryMetaId
	 *
	 */
	@Deprecated
	public Integer getWorkflowresourcecategoryMetaId () {
		return getId();
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
		this.workflowresourcecategoryId = workflowresourcecategory.getId();
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
