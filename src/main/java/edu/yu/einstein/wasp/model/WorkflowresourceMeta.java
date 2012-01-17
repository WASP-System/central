
/**
 *
 * WorkflowresourceMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowresourceMeta
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
@Table(name="workflowresourcemeta")
public class WorkflowresourceMeta extends MetaBase {

	/** 
	 * workflowresourceoptionId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer workflowresourceoptionId;

	/**
	 * setWorkflowresourceoptionId(Integer workflowresourceoptionId)
	 *
	 * @param workflowresourceoptionId
	 *
	 */
	
	public void setWorkflowresourceoptionId (Integer workflowresourceoptionId) {
		this.workflowresourceoptionId = workflowresourceoptionId;
	}

	/**
	 * getWorkflowresourceoptionId()
	 *
	 * @return workflowresourceoptionId
	 *
	 */
	public Integer getWorkflowresourceoptionId () {
		return this.workflowresourceoptionId;
	}




	/** 
	 * workflowresourceId
	 *
	 */
	@Column(name="workflowresourceid")
	protected Integer workflowresourceId;

	/**
	 * setWorkflowresourceId(Integer workflowresourceId)
	 *
	 * @param workflowresourceId
	 *
	 */
	
	public void setWorkflowresourceId (Integer workflowresourceId) {
		this.workflowresourceId = workflowresourceId;
	}

	/**
	 * getWorkflowresourceId()
	 *
	 * @return workflowresourceId
	 *
	 */
	public Integer getWorkflowresourceId () {
		return this.workflowresourceId;
	}




	/**
	 * workflowresource
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="workflowresourceid", insertable=false, updatable=false)
	protected Workflowresource workflowresource;

	/**
	 * setWorkflowresource (Workflowresource workflowresource)
	 *
	 * @param workflowresource
	 *
	 */
	public void setWorkflowresource (Workflowresource workflowresource) {
		this.workflowresource = workflowresource;
		this.workflowresourceId = workflowresource.workflowresourceId;
	}

	/**
	 * getWorkflowresource ()
	 *
	 * @return workflowresource
	 *
	 */
	
	public Workflowresource getWorkflowresource () {
		return this.workflowresource;
	}


}
