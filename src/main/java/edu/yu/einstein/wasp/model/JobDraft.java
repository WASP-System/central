
/**
 *
 * JobDraft.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraft
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="jobdraft")
public class JobDraft extends WaspModel {

	/** 
	 * jobDraftId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer jobDraftId;

	/**
	 * setJobDraftId(Integer jobDraftId)
	 *
	 * @param jobDraftId
	 *
	 */
	
	public void setJobDraftId (Integer jobDraftId) {
		this.jobDraftId = jobDraftId;
	}

	/**
	 * getJobDraftId()
	 *
	 * @return jobDraftId
	 *
	 */
	public Integer getJobDraftId () {
		return this.jobDraftId;
	}




	/** 
	 * labId
	 *
	 */
	@NotNull
	@Column(name="labid")
	protected Integer labId;

	/**
	 * setLabId(Integer labId)
	 *
	 * @param labId
	 *
	 */
	
	public void setLabId (Integer labId) {
		this.labId = labId;
	}

	/**
	 * getLabId()
	 *
	 * @return labId
	 *
	 */
	public Integer getLabId () {
		return this.labId;
	}




	/** 
	 * UserId
	 *
	 */
	@Column(name="userid")
	protected Integer UserId;

	/**
	 * setUserId(Integer UserId)
	 *
	 * @param UserId
	 *
	 */
	
	public void setUserId (Integer UserId) {
		this.UserId = UserId;
	}

	/**
	 * getUserId()
	 *
	 * @return UserId
	 *
	 */
	public Integer getUserId () {
		return this.UserId;
	}




	/** 
	 * workflowId
	 *
	 */
	@NotNull
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
	 * name
	 *
	 */
	@NotEmpty
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
	 * submittedjobId
	 *
	 */
	@Column(name="submittedjobid")
	protected Integer submittedjobId;

	/**
	 * setSubmittedjobId(Integer submittedjobId)
	 *
	 * @param submittedjobId
	 *
	 */
	
	public void setSubmittedjobId (Integer submittedjobId) {
		this.submittedjobId = submittedjobId;
	}

	/**
	 * getSubmittedjobId()
	 *
	 * @return submittedjobId
	 *
	 */
	public Integer getSubmittedjobId () {
		return this.submittedjobId;
	}




	/** 
	 * status
	 *
	 */
	@Column(name="status")
	protected String status;

	/**
	 * setStatus(String status)
	 *
	 * @param status
	 *
	 */
	
	public void setStatus (String status) {
		this.status = status;
	}

	/**
	 * getStatus()
	 *
	 * @return status
	 *
	 */
	public String getStatus () {
		return this.status;
	}




	/** 
	 * lastUpdTs
	 *
	 */
	@Column(name="lastupdts")
	protected Date lastUpdTs;

	/**
	 * setLastUpdTs(Date lastUpdTs)
	 *
	 * @param lastUpdTs
	 *
	 */
	
	public void setLastUpdTs (Date lastUpdTs) {
		this.lastUpdTs = lastUpdTs;
	}

	/**
	 * getLastUpdTs()
	 *
	 * @return lastUpdTs
	 *
	 */
	public Date getLastUpdTs () {
		return this.lastUpdTs;
	}




	/** 
	 * lastUpdUser
	 *
	 */
	@Column(name="lastupduser")
	protected Integer lastUpdUser;

	/**
	 * setLastUpdUser(Integer lastUpdUser)
	 *
	 * @param lastUpdUser
	 *
	 */
	
	public void setLastUpdUser (Integer lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	/**
	 * getLastUpdUser()
	 *
	 * @return lastUpdUser
	 *
	 */
	public Integer getLastUpdUser () {
		return this.lastUpdUser;
	}




	/**
	 * lab
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="labid", insertable=false, updatable=false)
	protected Lab lab;

	/**
	 * setLab (Lab lab)
	 *
	 * @param lab
	 *
	 */
	public void setLab (Lab lab) {
		this.lab = lab;
		this.labId = lab.labId;
	}

	/**
	 * getLab ()
	 *
	 * @return lab
	 *
	 */
	
	public Lab getLab () {
		return this.lab;
	}


	/**
	 * user
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="userid", insertable=false, updatable=false)
	protected User user;

	/**
	 * setUser (User user)
	 *
	 * @param user
	 *
	 */
	public void setUser (User user) {
		this.user = user;
		this.UserId = user.UserId;
	}

	/**
	 * getUser ()
	 *
	 * @return user
	 *
	 */
	
	public User getUser () {
		return this.user;
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
	 * job
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="submittedjobid", insertable=false, updatable=false)
	protected Job job;

	/**
	 * setJob (Job job)
	 *
	 * @param job
	 *
	 */
	public void setJob (Job job) {
		this.job = job;
		this.submittedjobId = job.jobId;
	}

	/**
	 * getJob ()
	 *
	 * @return job
	 *
	 */
	
	public Job getJob () {
		return this.job;
	}


	/** 
	 * jobDraftMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobdraftid", insertable=false, updatable=false)
	protected List<JobDraftMeta> jobDraftMeta;


	/** 
	 * getJobDraftMeta()
	 *
	 * @return jobDraftMeta
	 *
	 */
	public List<JobDraftMeta> getJobDraftMeta() {
		return this.jobDraftMeta;
	}


	/** 
	 * setJobDraftMeta
	 *
	 * @param jobDraftMeta
	 *
	 */
	public void setJobDraftMeta (List<JobDraftMeta> jobDraftMeta) {
		this.jobDraftMeta = jobDraftMeta;
	}



	/** 
	 * jobDraftresourcecategory
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobdraftid", insertable=false, updatable=false)
	protected List<JobDraftresourcecategory> jobDraftresourcecategory;


	/** 
	 * getJobDraftresourcecategory()
	 *
	 * @return jobDraftresourcecategory
	 *
	 */
	public List<JobDraftresourcecategory> getJobDraftresourcecategory() {
		return this.jobDraftresourcecategory;
	}


	/** 
	 * setJobDraftresourcecategory
	 *
	 * @param jobDraftresourcecategory
	 *
	 */
	public void setJobDraftresourcecategory (List<JobDraftresourcecategory> jobDraftresourcecategory) {
		this.jobDraftresourcecategory = jobDraftresourcecategory;
	}



	/** 
	 * jobDraftSoftware
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobdraftid", insertable=false, updatable=false)
	protected List<JobDraftSoftware> jobDraftSoftware;


	/** 
	 * getJobDraftSoftware()
	 *
	 * @return jobDraftSoftware
	 *
	 */
	public List<JobDraftSoftware> getJobDraftSoftware() {
		return this.jobDraftSoftware;
	}


	/** 
	 * setJobDraftSoftware
	 *
	 * @param jobDraftSoftware
	 *
	 */
	public void setJobDraftSoftware (List<JobDraftSoftware> jobDraftSoftware) {
		this.jobDraftSoftware = jobDraftSoftware;
	}



	/** 
	 * sampleDraft
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobdraftid", insertable=false, updatable=false)
	protected List<SampleDraft> sampleDraft;


	/** 
	 * getSampleDraft()
	 *
	 * @return sampleDraft
	 *
	 */
	public List<SampleDraft> getSampleDraft() {
		return this.sampleDraft;
	}


	/** 
	 * setSampleDraft
	 *
	 * @param sampleDraft
	 *
	 */
	public void setSampleDraft (List<SampleDraft> sampleDraft) {
		this.sampleDraft = sampleDraft;
	}



	/** 
	 * jobDraftCell
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobdraftid", insertable=false, updatable=false)
	protected List<JobDraftCell> jobDraftCell;


	/** 
	 * getJobDraftCell()
	 *
	 * @return jobDraftCell
	 *
	 */
	public List<JobDraftCell> getJobDraftCell() {
		return this.jobDraftCell;
	}


	/** 
	 * setJobDraftCell
	 *
	 * @param jobDraftCell
	 *
	 */
	public void setJobDraftCell (List<JobDraftCell> jobDraftCell) {
		this.jobDraftCell = jobDraftCell;
	}



}
