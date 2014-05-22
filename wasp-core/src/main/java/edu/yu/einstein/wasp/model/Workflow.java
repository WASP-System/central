
/**
 *
 * Workflow.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflow
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="workflow")
public class Workflow extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -600658140048389849L;

	/**
	 * setWorkflowId(Integer workflowId)
	 *
	 * @param workflowId
	 *
	 */
	@Deprecated
	public void setWorkflowId (Integer workflowId) {
		setId(workflowId);
	}

	/**
	 * getWorkflowId()
	 *
	 * @return workflowId
	 *
	 */
	@Deprecated
	public Integer getWorkflowId () {
		return getId();
	}




	/** 
	 * iName
	 *
	 */
	@Column(name="iname")
	protected String iName;

	/**
	 * setIName(String iName)
	 *
	 * @param iName
	 *
	 */
	
	public void setIName (String iName) {
		this.iName = iName;
	}

	/**
	 * getIName()
	 *
	 * @return iName
	 *
	 */
	public String getIName () {
		return this.iName;
	}




	/** 
	 * name
	 *
	 */
	@Column(name="name")
	protected String name;

	/**
	 * setName(String name)
	 *
	 * @param name
	 *
	 */
	
	public void setName (String name) {
		this.name = name;
	}

	/**
	 * getName()
	 *
	 * @return name
	 *
	 */
	public String getName () {
		return this.name;
	}




	/** 
	 * createts
	 *
	 */
	@Column(name="createts")
	protected Date createts;

	/**
	 * setCreatets(Date createts)
	 *
	 * @param createts
	 *
	 */
	
	public void setCreatets (Date createts) {
		this.createts = createts;
	}

	/**
	 * getCreatets()
	 *
	 * @return createts
	 *
	 */
	public Date getCreatets () {
		return this.createts;
	}




	/** 
	 * isActive
	 *
	 */
	@Column(name="isactive")
	protected Integer isActive = 0;

	/**
	 * setIsActive(Integer isActive)
	 *
	 * @param isActive
	 *
	 */
	
	public void setIsActive (Integer isActive) {
		this.isActive = isActive;
	}

	/**
	 * getIsActive()
	 *
	 * @return isActive
	 *
	 */
	public Integer getIsActive () {
		return this.isActive;
	}

	/** 
	 * workflowMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="workflowid", insertable=false, updatable=false)
	protected List<WorkflowMeta> workflowMeta;


	/** 
	 * getWorkflowMeta()
	 *
	 * @return workflowMeta
	 *
	 */
	@JsonIgnore
	public List<WorkflowMeta> getWorkflowMeta() {
		return this.workflowMeta;
	}


	/** 
	 * setWorkflowMeta
	 *
	 * @param workflowMeta
	 *
	 */
	public void setWorkflowMeta (List<WorkflowMeta> workflowMeta) {
		this.workflowMeta = workflowMeta;
	}



	/** 
	 * job
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="workflowid", insertable=false, updatable=false)
	protected List<Job> job;


	/** 
	 * getJob()
	 *
	 * @return job
	 *
	 */
	@JsonIgnore
	public List<Job> getJob() {
		return this.job;
	}


	/** 
	 * setJob
	 *
	 * @param job
	 *
	 */
	public void setJob (List<Job> job) {
		this.job = job;
	}



	/** 
	 * jobDraft
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="workflowid", insertable=false, updatable=false)
	protected List<JobDraft> jobDraft;


	/** 
	 * getJobDraft()
	 *
	 * @return jobDraft
	 *
	 */
	@JsonIgnore
	public List<JobDraft> getJobDraft() {
		return this.jobDraft;
	}


	/** 
	 * setJobDraft
	 *
	 * @param jobDraft
	 *
	 */
	public void setJobDraft (List<JobDraft> jobDraft) {
		this.jobDraft = jobDraft;
	}



	/** 
	 * workflowSampleSubtype
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="workflowid", insertable=false, updatable=false)
	protected List<WorkflowSampleSubtype> workflowSampleSubtype;


	/** 
	 * getWorkflowsamplesubtype()
	 *
	 * @return workflowSampleSubtype
	 *
	 */
	@JsonIgnore
	public List<WorkflowSampleSubtype> getWorkflowSampleSubtype() {
		return this.workflowSampleSubtype;
	}


	/** 
	 * setWorkflowsamplesubtype
	 *
	 * @param workflowSampleSubtype
	 *
	 */
	public void setWorkflowSampleSubtype (List<WorkflowSampleSubtype> workflowSampleSubtype) {
		this.workflowSampleSubtype = workflowSampleSubtype;
	}



	/** 
	 * workflowResourceType
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="workflowid", insertable=false, updatable=false)
	protected List<WorkflowResourceType> workflowResourceType;


	/** 
	 * getWorkflowresourcetype()
	 *
	 * @return workflowResourceType
	 *
	 */
	@JsonIgnore
	public List<WorkflowResourceType> getWorkflowResourceType() {
		return this.workflowResourceType;
	}


	/** 
	 * setWorkflowresourcetype
	 *
	 * @param workflowResourceType
	 *
	 */
	public void setWorkflowResourceType (List<WorkflowResourceType> workflowResourceType) {
		this.workflowResourceType = workflowResourceType;
	}



	/** 
	 * workflowresourcecategory
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="workflowid", insertable=false, updatable=false)
	protected List<Workflowresourcecategory> workflowresourcecategory;


	/** 
	 * getWorkflowresourcecategory()
	 *
	 * @return workflowresourcecategory
	 *
	 */
	@JsonIgnore
	public List<Workflowresourcecategory> getWorkflowresourcecategory() {
		return this.workflowresourcecategory;
	}


	/** 
	 * setWorkflowresourcecategory
	 *
	 * @param workflowresourcecategory
	 *
	 */
	public void setWorkflowresourcecategory (List<Workflowresourcecategory> workflowresourcecategory) {
		this.workflowresourcecategory = workflowresourcecategory;
	}



	/** 
	 * workflowSoftware
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="workflowid", insertable=false, updatable=false)
	protected List<WorkflowSoftware> workflowSoftware;


	/** 
	 * getWorkflowSoftware()
	 *
	 * @return workflowSoftware
	 *
	 */
	@JsonIgnore
	public List<WorkflowSoftware> getWorkflowSoftware() {
		return this.workflowSoftware;
	}


	/** 
	 * setWorkflowSoftware
	 *
	 * @param workflowSoftware
	 *
	 */
	public void setWorkflowSoftware (List<WorkflowSoftware> workflowSoftware) {
		this.workflowSoftware = workflowSoftware;
	}




}
