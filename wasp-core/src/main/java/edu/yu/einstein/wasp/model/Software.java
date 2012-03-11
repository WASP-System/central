
/**
 *
 * Software.java 
 * @author echeng (table2type.pl)
 *  
 * the Software
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="software")
public class Software extends WaspModel {

	/** 
	 * softwareId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer softwareId;

	/**
	 * setSoftwareId(Integer softwareId)
	 *
	 * @param softwareId
	 *
	 */
	
	public void setSoftwareId (Integer softwareId) {
		this.softwareId = softwareId;
	}

	/**
	 * getSoftwareId()
	 *
	 * @return softwareId
	 *
	 */
	public Integer getSoftwareId () {
		return this.softwareId;
	}




	/** 
	 * typeResourceId
	 *
	 */
	@Column(name="typeresourceid")
	protected int typeResourceId;

	/**
	 * setTypeResourceId(int typeResourceId)
	 *
	 * @param typeResourceId
	 *
	 */
	
	public void setTypeResourceId (int typeResourceId) {
		this.typeResourceId = typeResourceId;
	}

	/**
	 * getTypeResourceId()
	 *
	 * @return typeResourceId
	 *
	 */
	public int getTypeResourceId () {
		return this.typeResourceId;
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
	 * isActive
	 *
	 */
	@Column(name="isactive")
	protected Integer isActive;

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
	 * typeResource
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="typeresourceid", insertable=false, updatable=false)
	protected TypeResource typeResource;

	/**
	 * setTypeResource (TypeResource typeResource)
	 *
	 * @param typeResource
	 *
	 */
	public void setTypeResource (TypeResource typeResource) {
		this.typeResource = typeResource;
		this.typeResourceId = typeResource.typeResourceId;
	}

	/**
	 * getTypeResource ()
	 *
	 * @return typeResource
	 *
	 */
	
	public TypeResource getTypeResource () {
		return this.typeResource;
	}


	/** 
	 * softwareMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="softwareid", insertable=false, updatable=false)
	protected List<SoftwareMeta> softwareMeta;


	/** 
	 * getSoftwareMeta()
	 *
	 * @return softwareMeta
	 *
	 */
	@JsonIgnore
	public List<SoftwareMeta> getSoftwareMeta() {
		return this.softwareMeta;
	}


	/** 
	 * setSoftwareMeta
	 *
	 * @param softwareMeta
	 *
	 */
	public void setSoftwareMeta (List<SoftwareMeta> softwareMeta) {
		this.softwareMeta = softwareMeta;
	}



	/** 
	 * jobSoftware
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="softwareid", insertable=false, updatable=false)
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



	/** 
	 * jobDraftSoftware
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="softwareid", insertable=false, updatable=false)
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
	 * workflowSoftware
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="softwareid", insertable=false, updatable=false)
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
