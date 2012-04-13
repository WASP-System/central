
/**
 *
 * WorkflowSampleSubtype.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowSampleSubtype
 *
 *
 */

package edu.yu.einstein.wasp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="workflowsamplesubtype")
public class WorkflowSampleSubtype extends WaspModel {

	/** 
	 * workflowsamplesubtypeId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer workflowsamplesubtypeId;

	/**
	 * setWorkflowsamplesubtypeId(Integer workflowsamplesubtypeId)
	 *
	 * @param workflowsamplesubtypeId
	 *
	 */
	
	public void setWorkflowsamplesubtypeId (Integer workflowsamplesubtypeId) {
		this.workflowsamplesubtypeId = workflowsamplesubtypeId;
	}

	/**
	 * getWorkflowsamplesubtypeId()
	 *
	 * @return workflowsamplesubtypeId
	 *
	 */
	public Integer getWorkflowsamplesubtypeId () {
		return this.workflowsamplesubtypeId;
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
	 * sampleSubtypeId
	 *
	 */
	@Column(name="samplesubtypeid")
	protected Integer sampleSubtypeId;

	/**
	 * setSampleSubtypeId(Integer sampleSubtypeId)
	 *
	 * @param sampleSubtypeId
	 *
	 */
	
	public void setSampleSubtypeId (Integer sampleSubtypeId) {
		this.sampleSubtypeId = sampleSubtypeId;
	}

	/**
	 * getSampleSubtypeId()
	 *
	 * @return sampleSubtypeId
	 *
	 */
	public Integer getSampleSubtypeId () {
		return this.sampleSubtypeId;
	}




	/**
	 * sampleSubtype
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="samplesubtypeid", insertable=false, updatable=false)
	protected SampleSubtype sampleSubtype;

	/**
	 * setSampleSubtype (SampleSubtype sampleSubtype)
	 *
	 * @param sampleSubtype
	 *
	 */
	public void setSampleSubtype (SampleSubtype sampleSubtype) {
		this.sampleSubtype = sampleSubtype;
		this.sampleSubtypeId = sampleSubtype.sampleSubtypeId;
	}

	/**
	 * getSampleSubtype ()
	 *
	 * @return sampleSubtype
	 *
	 */
	
	public SampleSubtype getSampleSubtype () {
		return this.sampleSubtype;
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