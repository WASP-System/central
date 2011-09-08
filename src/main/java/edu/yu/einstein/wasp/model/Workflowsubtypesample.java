
/**
 *
 * Workflowsubtypesample.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowsubtypesample
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
@Table(name="workflowsubtypesample")
public class Workflowsubtypesample extends WaspModel {

	/** 
	 * workflowsubtypesampleId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int workflowsubtypesampleId;

	/**
	 * setWorkflowsubtypesampleId(int workflowsubtypesampleId)
	 *
	 * @param workflowsubtypesampleId
	 *
	 */
	
	public void setWorkflowsubtypesampleId (int workflowsubtypesampleId) {
		this.workflowsubtypesampleId = workflowsubtypesampleId;
	}

	/**
	 * getWorkflowsubtypesampleId()
	 *
	 * @return workflowsubtypesampleId
	 *
	 */
	public int getWorkflowsubtypesampleId () {
		return this.workflowsubtypesampleId;
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
	 * subtypeSampleId
	 *
	 */
	@Column(name="subtypesampleid")
	protected int subtypeSampleId;

	/**
	 * setSubtypeSampleId(int subtypeSampleId)
	 *
	 * @param subtypeSampleId
	 *
	 */
	
	public void setSubtypeSampleId (int subtypeSampleId) {
		this.subtypeSampleId = subtypeSampleId;
	}

	/**
	 * getSubtypeSampleId()
	 *
	 * @return subtypeSampleId
	 *
	 */
	public int getSubtypeSampleId () {
		return this.subtypeSampleId;
	}




	/**
	 * subtypeSample
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="subtypesampleid", insertable=false, updatable=false)
	protected SubtypeSample subtypeSample;

	/**
	 * setSubtypeSample (SubtypeSample subtypeSample)
	 *
	 * @param subtypeSample
	 *
	 */
	public void setSubtypeSample (SubtypeSample subtypeSample) {
		this.subtypeSample = subtypeSample;
		this.subtypeSampleId = subtypeSample.subtypeSampleId;
	}

	/**
	 * getSubtypeSample ()
	 *
	 * @return subtypeSample
	 *
	 */
	
	public SubtypeSample getSubtypeSample () {
		return this.subtypeSample;
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
