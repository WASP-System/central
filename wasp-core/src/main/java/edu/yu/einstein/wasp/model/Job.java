
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="job")
public class Job extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6968610621273206934L;

	/**
	 * setJobId(Integer jobId)
	 *
	 * @param jobId
	 *
	 */
	@Deprecated
	public void setJobId (Integer jobId) {
		setId(jobId);
	}

	/**
	 * getJobId()
	 *
	 * @return jobId
	 *
	 */
	@Deprecated
	public Integer getJobId () {
		return getId();
	}




	/** 
	 * labId
	 *
	 */
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
	@Deprecated
	public void setCreatets (Date createts) {
		this.createts = createts;
	}

	/**
	 * getCreatets()
	 *
	 * @return createts
	 *
	 */
	@Deprecated
	public Date getCreatets () {
		return this.createts;
	}




	/** 
	 * viewablebylab
	 *
	 */
	@Column(name="viewablebylab")
	protected Integer viewablebylab = 0;

	/**
	 * setViewablebylab(Integer viewablebylab)
	 *
	 * @param viewablebylab
	 *
	 */
	
	public void setViewablebylab (Integer viewablebylab) {
		this.viewablebylab = viewablebylab;
	}

	/**
	 * getViewablebylab()
	 *
	 * @return viewablebylab
	 *
	 */
	public Integer getViewablebylab () {
		return this.viewablebylab;
	}




	/** 
	 * isActive
	 *
	 */
	@Column(name="isactive")
	protected Integer isActive = 1;

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
	@JsonIgnore
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
	@JsonIgnore
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
	@JsonIgnore
	public List<Sample> getSample() {
		//return this.sample; //commented out 1-11-13; this line is not going through the jobSample table
		List<Sample> sampleList = new ArrayList<Sample>();
		for(JobSample js : this.getJobSample()){
			sampleList.add(js.getSample());
		}
		return sampleList;
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
	@JsonIgnore
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
	 * jobCellSelection
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobid", insertable=false, updatable=false)
	protected List<JobCellSelection> jobCellSelection;


	/** 
	 * getJobCell()
	 *
	 * @return jobCellSelection
	 *
	 */
	@JsonIgnore
	public List<JobCellSelection> getJobCellSelection() {
		return this.jobCellSelection;
	}


	/** 
	 * setJobCell
	 *
	 * @param jobCellSelection
	 *
	 */
	public void setJobCellSelection (List<JobCellSelection> jobCellSelection) {
		this.jobCellSelection = jobCellSelection;
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
	@JsonIgnore
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
	protected Set<AcctQuote> acctQuote ;


	/** 
	 * getAcctQuote()
	 *
	 * @return acctQuote
	 *
	 */
	@JsonIgnore
	public Set<AcctQuote> getAcctQuote() {
		return this.acctQuote;
	}


	/** 
	 * setAcctQuote
	 *
	 * @param acctQuote
	 *
	 */
	public void setAcctQuote (Set<AcctQuote> acctQuote) {
		this.acctQuote = acctQuote;
	}

	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="current_quote")
	private AcctQuote currentQuote;

	/**
	 * @return the current
	 */
	@JsonIgnore
	public AcctQuote getCurrentQuote() {
		return currentQuote;
	}

	/**
	 * @param current the current to set
	 */
	public void setCurrentQuote(AcctQuote currentQuote) {
		this.currentQuote = currentQuote;
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
	@JsonIgnore
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
	@JsonIgnore
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
	@JsonIgnore
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
         * jobResourcecategory
         *
         */
        @NotAudited
        @OneToMany
        @JoinColumn(name="jobid", insertable=false, updatable=false)
        protected List<JobResourcecategory> jobResourcecategory;

        /**
         * getJobResourcecategory()
         *
         * @return jobResourcecategory
         *
         */
        @JsonIgnore
        public List<JobResourcecategory> getJobResourcecategory() {
                return this.jobResourcecategory;
        }


        /**
         * setJobResourcecategory
         *
         * @param jobResourcecategory
         *
         */
        public void setJobResourcecategory (List<JobResourcecategory> jobResourcecategory) {
                this.jobResourcecategory = jobResourcecategory;
        }

        /**
         * jobSoftware
         *
         */
        @NotAudited
        @OneToMany
        @JoinColumn(name="jobid", insertable=false, updatable=false)
        protected List<JobSoftware> jobSoftware;


        /**
         * getJobSoftware()
         *
         * @return jobSoftware
         *
         */
        @JsonIgnore
        public List<JobSoftware> getJobSoftware() {
                return this.jobSoftware;
        }



        /**
         * setJobSoftware
         *
         * @param jobSoftware
         *
         */
        public void setJobSoftware (List<JobSoftware> jobSoftware) {
                this.jobSoftware = jobSoftware;
        }




}
