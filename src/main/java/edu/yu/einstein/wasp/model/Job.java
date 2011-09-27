
/**
 *
 * Job.java 
 * @author echeng (table2type.pl)
 *  
 * the Job
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
@Table(name="job")
public class Job extends WaspModel {

	/** 
	 * jobId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int jobId;

	/**
	 * setJobId(int jobId)
	 *
	 * @param jobId
	 *
	 */
	
	public void setJobId (int jobId) {
		this.jobId = jobId;
	}

	/**
	 * getJobId()
	 *
	 * @return jobId
	 *
	 */
	public int getJobId () {
		return this.jobId;
	}




	/** 
	 * labId
	 *
	 */
	@Column(name="labid")
	protected int labId;

	/**
	 * setLabId(int labId)
	 *
	 * @param labId
	 *
	 */
	
	public void setLabId (int labId) {
		this.labId = labId;
	}

	/**
	 * getLabId()
	 *
	 * @return labId
	 *
	 */
	public int getLabId () {
		return this.labId;
	}




	/** 
	 * UserId
	 *
	 */
	@Column(name="userid")
	protected int UserId;

	/**
	 * setUserId(int UserId)
	 *
	 * @param UserId
	 *
	 */
	
	public void setUserId (int UserId) {
		this.UserId = UserId;
	}

	/**
	 * getUserId()
	 *
	 * @return UserId
	 *
	 */
	public int getUserId () {
		return this.UserId;
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
	 * viewablebylab
	 *
	 */
	@Column(name="viewablebylab")
	protected int viewablebylab;

	/**
	 * setViewablebylab(int viewablebylab)
	 *
	 * @param viewablebylab
	 *
	 */
	
	public void setViewablebylab (int viewablebylab) {
		this.viewablebylab = viewablebylab;
	}

	/**
	 * getViewablebylab()
	 *
	 * @return viewablebylab
	 *
	 */
	public int getViewablebylab () {
		return this.viewablebylab;
	}




	/** 
	 * isActive
	 *
	 */
	@Column(name="isactive")
	protected int isActive;

	/**
	 * setIsActive(int isActive)
	 *
	 * @param isActive
	 *
	 */
	
	public void setIsActive (int isActive) {
		this.isActive = isActive;
	}

	/**
	 * getIsActive()
	 *
	 * @return isActive
	 *
	 */
	public int getIsActive () {
		return this.isActive;
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
	protected int lastUpdUser;

	/**
	 * setLastUpdUser(int lastUpdUser)
	 *
	 * @param lastUpdUser
	 *
	 */
	
	public void setLastUpdUser (int lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	/**
	 * getLastUpdUser()
	 *
	 * @return lastUpdUser
	 *
	 */
	public int getLastUpdUser () {
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
	 * jobMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobid", insertable=false, updatable=false)
	protected List<JobMeta> jobMeta;


	/** 
	 * getJobMeta()
	 *
	 * @return jobMeta
	 *
	 */
	public List<JobMeta> getJobMeta() {
		return this.jobMeta;
	}


	/** 
	 * setJobMeta
	 *
	 * @param jobMeta
	 *
	 */
	public void setJobMeta (List<JobMeta> jobMeta) {
		this.jobMeta = jobMeta;
	}



	/** 
	 * jobUser
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobid", insertable=false, updatable=false)
	protected List<JobUser> jobUser;


	/** 
	 * getJobUser()
	 *
	 * @return jobUser
	 *
	 */
	public List<JobUser> getJobUser() {
		return this.jobUser;
	}


	/** 
	 * setJobUser
	 *
	 * @param jobUser
	 *
	 */
	public void setJobUser (List<JobUser> jobUser) {
		this.jobUser = jobUser;
	}



	/** 
	 * jobDraft
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="submittedjobid", insertable=false, updatable=false)
	protected List<JobDraft> jobDraft;


	/** 
	 * getJobDraft()
	 *
	 * @return jobDraft
	 *
	 */
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
	 * sample
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="submitter_jobid", insertable=false, updatable=false)
	protected List<Sample> sample;


	/** 
	 * getSample()
	 *
	 * @return sample
	 *
	 */
	public List<Sample> getSample() {
		return this.sample;
	}


	/** 
	 * setSample
	 *
	 * @param sample
	 *
	 */
	public void setSample (List<Sample> sample) {
		this.sample = sample;
	}



	/** 
	 * jobSample
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobid", insertable=false, updatable=false)
	protected List<JobSample> jobSample;


	/** 
	 * getJobSample()
	 *
	 * @return jobSample
	 *
	 */
	public List<JobSample> getJobSample() {
		return this.jobSample;
	}


	/** 
	 * setJobSample
	 *
	 * @param jobSample
	 *
	 */
	public void setJobSample (List<JobSample> jobSample) {
		this.jobSample = jobSample;
	}



	/** 
	 * jobCell
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobid", insertable=false, updatable=false)
	protected List<JobCell> jobCell;


	/** 
	 * getJobCell()
	 *
	 * @return jobCell
	 *
	 */
	public List<JobCell> getJobCell() {
		return this.jobCell;
	}


	/** 
	 * setJobCell
	 *
	 * @param jobCell
	 *
	 */
	public void setJobCell (List<JobCell> jobCell) {
		this.jobCell = jobCell;
	}



	/** 
	 * acctWorkflowcost
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="workflowid", insertable=false, updatable=false)
	protected List<AcctWorkflowcost> acctWorkflowcost;


	/** 
	 * getAcctWorkflowcost()
	 *
	 * @return acctWorkflowcost
	 *
	 */
	public List<AcctWorkflowcost> getAcctWorkflowcost() {
		return this.acctWorkflowcost;
	}


	/** 
	 * setAcctWorkflowcost
	 *
	 * @param acctWorkflowcost
	 *
	 */
	public void setAcctWorkflowcost (List<AcctWorkflowcost> acctWorkflowcost) {
		this.acctWorkflowcost = acctWorkflowcost;
	}



	/** 
	 * acctQuote
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobid", insertable=false, updatable=false)
	protected List<AcctQuote> acctQuote;


	/** 
	 * getAcctQuote()
	 *
	 * @return acctQuote
	 *
	 */
	public List<AcctQuote> getAcctQuote() {
		return this.acctQuote;
	}


	/** 
	 * setAcctQuote
	 *
	 * @param acctQuote
	 *
	 */
	public void setAcctQuote (List<AcctQuote> acctQuote) {
		this.acctQuote = acctQuote;
	}



	/** 
	 * acctJobquotecurrent
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobid", insertable=false, updatable=false)
	protected List<AcctJobquotecurrent> acctJobquotecurrent;


	/** 
	 * getAcctJobquotecurrent()
	 *
	 * @return acctJobquotecurrent
	 *
	 */
	public List<AcctJobquotecurrent> getAcctJobquotecurrent() {
		return this.acctJobquotecurrent;
	}


	/** 
	 * setAcctJobquotecurrent
	 *
	 * @param acctJobquotecurrent
	 *
	 */
	public void setAcctJobquotecurrent (List<AcctJobquotecurrent> acctJobquotecurrent) {
		this.acctJobquotecurrent = acctJobquotecurrent;
	}



	/** 
	 * acctInvoice
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobid", insertable=false, updatable=false)
	protected List<AcctInvoice> acctInvoice;


	/** 
	 * getAcctInvoice()
	 *
	 * @return acctInvoice
	 *
	 */
	public List<AcctInvoice> getAcctInvoice() {
		return this.acctInvoice;
	}


	/** 
	 * setAcctInvoice
	 *
	 * @param acctInvoice
	 *
	 */
	public void setAcctInvoice (List<AcctInvoice> acctInvoice) {
		this.acctInvoice = acctInvoice;
	}



	/** 
	 * acctLedger
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobid", insertable=false, updatable=false)
	protected List<AcctLedger> acctLedger;


	/** 
	 * getAcctLedger()
	 *
	 * @return acctLedger
	 *
	 */
	public List<AcctLedger> getAcctLedger() {
		return this.acctLedger;
	}


	/** 
	 * setAcctLedger
	 *
	 * @param acctLedger
	 *
	 */
	public void setAcctLedger (List<AcctLedger> acctLedger) {
		this.acctLedger = acctLedger;
	}



	/** 
	 * jobFile
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobid", insertable=false, updatable=false)
	protected List<JobFile> jobFile;


	/** 
	 * getJobFile()
	 *
	 * @return jobFile
	 *
	 */
	public List<JobFile> getJobFile() {
		return this.jobFile;
	}


	/** 
	 * setJobFile
	 *
	 * @param jobFile
	 *
	 */
	public void setJobFile (List<JobFile> jobFile) {
		this.jobFile = jobFile;
	}



	/** 
	 * statejob
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobid", insertable=false, updatable=false)
	protected List<Statejob> statejob;


	/** 
	 * getStatejob()
	 *
	 * @return statejob
	 *
	 */
	public List<Statejob> getStatejob() {
		return this.statejob;
	}


	/** 
	 * setStatejob
	 *
	 * @param statejob
	 *
	 */
	public void setStatejob (List<Statejob> statejob) {
		this.statejob = statejob;
	}



}
