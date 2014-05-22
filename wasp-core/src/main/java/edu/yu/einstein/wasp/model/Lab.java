
/**
 *
 * Lab.java 
 * @author echeng (table2type.pl)
 *  
 * the Lab
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

@Entity
@Audited
@Table(name="lab")
public class Lab extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7721869798227853055L;

	/**
	 * setLabId(Integer labId)
	 *
	 * @param labId
	 *
	 */
	@Deprecated
	public void setLabId (Integer labId) {
		setId(labId);
	}

	/**
	 * getLabId()
	 *
	 * @return labId
	 *
	 */
	@Deprecated
	public Integer getLabId () {
		return getId();
	}




	/** 
	 * departmentId
	 *
	 */
	@Column(name="departmentid")
	@Range(min=1)
	protected Integer departmentId;

	/**
	 * setDepartmentId(Integer departmentId)
	 *
	 * @param departmentId
	 *
	 */
	
	public void setDepartmentId (Integer departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * getDepartmentId()
	 *
	 * @return departmentId
	 *
	 */
	public Integer getDepartmentId () {
		return this.departmentId;
	}




	/** 
	 * name
	 *
	 */
	@Column(name="name")
	@NotEmpty
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
	 * primaryUserId
	 *
	 */
	@Column(name="primaryuserid")
	@Range(min=1)
	protected Integer primaryUserId;

	/**
	 * setPrimaryUserId(Integer primaryUserId)
	 *
	 * @param primaryUserId
	 *
	 */
	
	public void setPrimaryUserId (Integer primaryUserId) {
		this.primaryUserId = primaryUserId;
	}

	/**
	 * getPrimaryUserId()
	 *
	 * @return primaryUserId
	 *
	 */
	public Integer getPrimaryUserId () {
		return this.primaryUserId;
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
	 * department
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="departmentid", insertable=false, updatable=false)
	protected Department department;

	/**
	 * setDepartment (Department department)
	 *
	 * @param department
	 *
	 */
	public void setDepartment (Department department) {
		this.department = department;
		this.departmentId = department.getId();
	}

	/**
	 * getDepartment ()
	 *
	 * @return department
	 *
	 */
	
	@JsonIgnore
	public Department getDepartment () {
		return this.department;
	}


	/**
	 * user
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="primaryuserid", insertable=false, updatable=false)
	protected User user;

	/**
	 * setUser (User user)
	 *
	 * @param user
	 *
	 */
	public void setUser (User user) {
		this.user = user;
		this.primaryUserId = user.getId();
	}

	/**
	 * getUser ()
	 *
	 * @return user
	 *
	 */
	
	@JsonIgnore
	public User getUser () {
		return this.user;
	}


	/** 
	 * labMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="labid", insertable=false, updatable=false)
	protected List<LabMeta> labMeta;


	/** 
	 * getLabMeta()
	 *
	 * @return labMeta
	 *
	 */
	@JsonIgnore
	public List<LabMeta> getLabMeta() {
		return this.labMeta;
	}


	/** 
	 * setLabMeta
	 *
	 * @param labMeta
	 *
	 */
	public void setLabMeta (List<LabMeta> labMeta) {
		this.labMeta = labMeta;
	}



	/** 
	 * labUser
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="labid", insertable=false, updatable=false)
	protected List<LabUser> labUser;


	/** 
	 * getLabUser()
	 *
	 * @return labUser
	 *
	 */
	@JsonIgnore
	public List<LabUser> getLabUser() {
		return this.labUser;
	}


	/** 
	 * setLabUser
	 *
	 * @param labUser
	 *
	 */
	public void setLabUser (List<LabUser> labUser) {
		this.labUser = labUser;
	}



	/** 
	 * userPending
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="labid", insertable=false, updatable=false)
	protected List<UserPending> userPending;


	/** 
	 * getUserPending()
	 *
	 * @return userPending
	 *
	 */
	@JsonIgnore
	public List<UserPending> getUserPending() {
		return this.userPending;
	}


	/** 
	 * setUserPending
	 *
	 * @param userPending
	 *
	 */
	public void setUserPending (List<UserPending> userPending) {
		this.userPending = userPending;
	}



	/** 
	 * job
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="labid", insertable=false, updatable=false)
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
	@JoinColumn(name="labid", insertable=false, updatable=false)
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
	 * project
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="labid", insertable=false, updatable=false)
	protected List<Project> project;


	/** 
	 * getProject()
	 *
	 * @return project
	 *
	 */
	@JsonIgnore
	public List<Project> getProject() {
		return this.project;
	}


	/** 
	 * setProject
	 *
	 * @param project
	 *
	 */
	public void setProject (List<Project> project) {
		this.project = project;
	}



	/** 
	 * sample
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="submitter_labid", insertable=false, updatable=false)
	protected List<Sample> sample;


	/** 
	 * getSample()
	 *
	 * @return sample
	 *
	 */
	@JsonIgnore
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
	 * sampleLab
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="labid", insertable=false, updatable=false)
	protected List<SampleLab> sampleLab;


	/** 
	 * getSampleLab()
	 *
	 * @return sampleLab
	 *
	 */
	@JsonIgnore
	public List<SampleLab> getSampleLab() {
		return this.sampleLab;
	}


	/** 
	 * setSampleLab
	 *
	 * @param sampleLab
	 *
	 */
	public void setSampleLab (List<SampleLab> sampleLab) {
		this.sampleLab = sampleLab;
	}



	/** 
	 * sampleDraft
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="labid", insertable=false, updatable=false)
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
	 * acctGrant
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="labid", insertable=false, updatable=false)
	protected List<AcctGrant> acctGrant;


	/** 
	 * getAcctGrant()
	 *
	 * @return acctGrant
	 *
	 */
	@JsonIgnore
	public List<AcctGrant> getAcctGrant() {
		return this.acctGrant;
	}


	/** 
	 * setAcctGrant
	 *
	 * @param acctGrant
	 *
	 */
	public void setAcctGrant (List<AcctGrant> acctGrant) {
		this.acctGrant = acctGrant;
	}



}
