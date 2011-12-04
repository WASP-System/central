
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

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="workflow")
public class Workflow extends WaspModel {

	/** 
	 * workflowId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
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
	 * workflowsubtypesample
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="workflowid", insertable=false, updatable=false)
	protected List<Workflowsubtypesample> workflowsubtypesample;


	/** 
	 * getWorkflowsubtypesample()
	 *
	 * @return workflowsubtypesample
	 *
	 */
	public List<Workflowsubtypesample> getWorkflowsubtypesample() {
		return this.workflowsubtypesample;
	}


	/** 
	 * setWorkflowsubtypesample
	 *
	 * @param workflowsubtypesample
	 *
	 */
	public void setWorkflowsubtypesample (List<Workflowsubtypesample> workflowsubtypesample) {
		this.workflowsubtypesample = workflowsubtypesample;
	}



	/** 
	 * workflowtyperesource
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="workflowid", insertable=false, updatable=false)
	protected List<Workflowtyperesource> workflowtyperesource;


	/** 
	 * getWorkflowtyperesource()
	 *
	 * @return workflowtyperesource
	 *
	 */
	public List<Workflowtyperesource> getWorkflowtyperesource() {
		return this.workflowtyperesource;
	}


	/** 
	 * setWorkflowtyperesource
	 *
	 * @param workflowtyperesource
	 *
	 */
	public void setWorkflowtyperesource (List<Workflowtyperesource> workflowtyperesource) {
		this.workflowtyperesource = workflowtyperesource;
	}



	/** 
	 * workflowresource
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="workflowid", insertable=false, updatable=false)
	protected List<Workflowresource> workflowresource;


	/** 
	 * getWorkflowresource()
	 *
	 * @return workflowresource
	 *
	 */
	public List<Workflowresource> getWorkflowresource() {
		return this.workflowresource;
	}


	/** 
	 * setWorkflowresource
	 *
	 * @param workflowresource
	 *
	 */
	public void setWorkflowresource (List<Workflowresource> workflowresource) {
		this.workflowresource = workflowresource;
	}



}
