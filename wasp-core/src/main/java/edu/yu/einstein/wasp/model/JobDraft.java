
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Audited
@Table(name="jobdraft")
public class JobDraft extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2823765071375908605L;

	/**
	 * setJobDraftId(Integer jobDraftId)
	 *
	 * @param jobDraftId
	 *
	 */
	@Deprecated
	public void setJobDraftId (Integer jobDraftId) {
		setId(jobDraftId);
	}

	/**
	 * getJobDraftId()
	 *
	 * @return jobDraftId
	 *
	 */
	@Deprecated
	public Integer getJobDraftId () {
		return getId();
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
	protected Integer userId;

	/**
	 * setUserId(Integer UserId)
	 *
	 * @param UserId
	 *
	 */
	
	public void setUserId (Integer userId) {
		this.userId = userId;
	}

	/**
	 * getUserId()
	 *
	 * @return UserId
	 *
	 */
	public Integer getUserId () {
		return this.userId;
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
	 * Default value set to 1
	 * status
	 *
	 */
	@Column(name="status", length=50)
	protected String status = "1";

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
		this.labId = lab.getId();
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
		this.userId = user.getId();
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
		this.submittedjobId = job.getId();
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
	@JsonIgnore
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
	@JsonIgnore
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
	@JsonIgnore
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
	@JsonIgnore
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
	 * jobDraftCellSelection
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobdraftid", insertable=false, updatable=false)
	protected List<JobDraftCellSelection> jobDraftCellSelection;


	/** 
	 * getJobDraftCell()
	 *
	 * @return jobDraftCellSelection
	 *
	 */
	@JsonIgnore
	public List<JobDraftCellSelection> getJobDraftCellSelection() {
		return this.jobDraftCellSelection;
	}


	/** 
	 * setJobDraftCell
	 *
	 * @param jobDraftCellSelection
	 *
	 */
	public void setJobDraftCellSelection (List<JobDraftCellSelection> jobDraftCellSelection) {
		this.jobDraftCellSelection = jobDraftCellSelection;
	}

	/** 
	 * jobDraftFile
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobdraftid", insertable=false, updatable=false)
	protected List<JobDraftFile> jobDraftFile;


	/** 
	 * getJobDraftFile()
	 *
	 * @return jobDraftFile
	 *
	 */
	@JsonIgnore
	public List<JobDraftFile> getJobDraftFile() {
		return this.jobDraftFile;
	}


	/** 
	 * setJobDraftFile
	 *
	 * @param jobDraftFile
	 *
	 */
	public void setJobDraftFile (List<JobDraftFile> jobDraftFile) {
		this.jobDraftFile = jobDraftFile;
	}

}
