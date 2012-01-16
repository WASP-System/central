
/**
 *
 * Resource.java 
 * @author echeng (table2type.pl)
 *  
 * the Resource
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

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

@Entity
@Audited
@Table(name="resource")
public class Resource extends WaspModel {

	/** 
	 * resourceId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer resourceId;

	/**
	 * setResourceId(Integer resourceId)
	 *
	 * @param resourceId
	 *
	 */
	
	public void setResourceId (Integer resourceId) {
		this.resourceId = resourceId;
	}

	/**
	 * getResourceId()
	 *
	 * @return resourceId
	 *
	 */
	public Integer getResourceId () {
		return this.resourceId;
	}




	/** 
	 * platform
	 *
	 */
	@Column(name="platform")
	protected String platform;

	/**
	 * setPlatform(String platform)
	 *
	 * @param platform
	 *
	 */
	
	public void setPlatform (String platform) {
		this.platform = platform;
	}

	/**
	 * getPlatform()
	 *
	 * @return platform
	 *
	 */
	public String getPlatform () {
		return this.platform;
	}




	/** 
	 * iName
	 *
	 */
	@Column(name="iname")
	@NotEmpty
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
	 * typeResourceId
	 *
	 */
	@Column(name="typeresourceid")
	@Range(min=1)
	protected Integer typeResourceId;

	/**
	 * setTypeResourceId(Integer typeResourceId)
	 *
	 * @param typeResourceId
	 *
	 */
	
	public void setTypeResourceId (Integer typeResourceId) {
		this.typeResourceId = typeResourceId;
	}

	/**
	 * getTypeResourceId()
	 *
	 * @return typeResourceId
	 *
	 */
	public Integer getTypeResourceId () {
		return this.typeResourceId;
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
	 * resourceMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourceid", insertable=false, updatable=false)
	protected List<ResourceMeta> resourceMeta;


	/** 
	 * getResourceMeta()
	 *
	 * @return resourceMeta
	 *
	 */
	public List<ResourceMeta> getResourceMeta() {
		return this.resourceMeta;
	}


	/** 
	 * setResourceMeta
	 *
	 * @param resourceMeta
	 *
	 */
	public void setResourceMeta (List<ResourceMeta> resourceMeta) {
		this.resourceMeta = resourceMeta;
	}



	/** 
	 * jobResource
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourceid", insertable=false, updatable=false)
	protected List<JobResource> jobResource;


	/** 
	 * getJobResource()
	 *
	 * @return jobResource
	 *
	 */
	public List<JobResource> getJobResource() {
		return this.jobResource;
	}


	/** 
	 * setJobResource
	 *
	 * @param jobResource
	 *
	 */
	public void setJobResource (List<JobResource> jobResource) {
		this.jobResource = jobResource;
	}



	/** 
	 * jobDraftresource
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourceid", insertable=false, updatable=false)
	protected List<JobDraftresource> jobDraftresource;


	/** 
	 * getJobDraftresource()
	 *
	 * @return jobDraftresource
	 *
	 */
	public List<JobDraftresource> getJobDraftresource() {
		return this.jobDraftresource;
	}


	/** 
	 * setJobDraftresource
	 *
	 * @param jobDraftresource
	 *
	 */
	public void setJobDraftresource (List<JobDraftresource> jobDraftresource) {
		this.jobDraftresource = jobDraftresource;
	}



	/** 
	 * resourceBarcode
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourceid", insertable=false, updatable=false)
	protected List<ResourceBarcode> resourceBarcode;


	/** 
	 * getResourceBarcode()
	 *
	 * @return resourceBarcode
	 *
	 */
	public List<ResourceBarcode> getResourceBarcode() {
		return this.resourceBarcode;
	}


	/** 
	 * setResourceBarcode
	 *
	 * @param resourceBarcode
	 *
	 */
	public void setResourceBarcode (List<ResourceBarcode> resourceBarcode) {
		this.resourceBarcode = resourceBarcode;
	}



	/** 
	 * workflowresource
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourceid", insertable=false, updatable=false)
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



	/** 
	 * resourceLane
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourceid", insertable=false, updatable=false)
	protected List<ResourceLane> resourceLane;


	/** 
	 * getResourceLane()
	 *
	 * @return resourceLane
	 *
	 */
	public List<ResourceLane> getResourceLane() {
		return this.resourceLane;
	}


	/** 
	 * setResourceLane
	 *
	 * @param resourceLane
	 *
	 */
	public void setResourceLane (List<ResourceLane> resourceLane) {
		this.resourceLane = resourceLane;
	}



	/** 
	 * run
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourceid", insertable=false, updatable=false)
	protected List<Run> run;


	/** 
	 * getRun()
	 *
	 * @return run
	 *
	 */
	public List<Run> getRun() {
		return this.run;
	}


	/** 
	 * setRun
	 *
	 * @param run
	 *
	 */
	public void setRun (List<Run> run) {
		this.run = run;
	}



}
