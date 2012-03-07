
/**
 *
 * Run.java 
 * @author echeng (table2type.pl)
 *  
 * the Run
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

@Entity
@Audited
@Table(name="run")
public class Run extends WaspModel {

	/** 
	 * runId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer runId;

	/**
	 * setRunId(Integer runId)
	 *
	 * @param runId
	 *
	 */
	
	public void setRunId (Integer runId) {
		this.runId = runId;
	}

	/**
	 * getRunId()
	 *
	 * @return runId
	 *
	 */
	public Integer getRunId () {
		return this.runId;
	}




	/** 
	 * resourceId
	 *
	 */
	@Column(name="resourceid")
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
	 * resourceCategoryId
	 *
	 */
	@Column(name="resourceCategoryid")
	protected Integer resourceCategoryId;

	/**
	 * setResourceCategoryId(Integer resourceCategoryId)
	 *
	 * @param resourceCategoryId
	 *
	 */
	
	public void setResourceCategoryId (Integer resourceCategoryId) {
		this.resourceCategoryId = resourceCategoryId;
	}

	/**
	 * getResourceCategoryId()
	 *
	 * @return resourceCategoryId
	 *
	 */
	public Integer getResourceCategoryId () {
		return this.resourceCategoryId;
	}




	/** 
	 * softwareId
	 *
	 */
	@Column(name="softwareid")
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
	 * sampleId
	 *
	 */
	@Column(name="sampleid")
	protected Integer sampleId;

	/**
	 * setSampleId(Integer sampleId)
	 *
	 * @param sampleId
	 *
	 */
	
	public void setSampleId (Integer sampleId) {
		this.sampleId = sampleId;
	}

	/**
	 * getSampleId()
	 *
	 * @return sampleId
	 *
	 */
	public Integer getSampleId () {
		return this.sampleId;
	}




	/** 
	 * startts
	 *
	 */
	@Column(name="startts")
	protected Date startts;

	/**
	 * setStartts(Date startts)
	 *
	 * @param startts
	 *
	 */
	
	public void setStartts (Date startts) {
		this.startts = startts;
	}

	/**
	 * getStartts()
	 *
	 * @return startts
	 *
	 */
	public Date getStartts () {
		return this.startts;
	}




	/** 
	 * enDts
	 *
	 */
	@Column(name="endts")
	protected Date enDts;

	/**
	 * setEnDts(Date enDts)
	 *
	 * @param enDts
	 *
	 */
	
	public void setEnDts (Date enDts) {
		this.enDts = enDts;
	}

	/**
	 * getEnDts()
	 *
	 * @return enDts
	 *
	 */
	public Date getEnDts () {
		return this.enDts;
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
	 * resource
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="resourceid", insertable=false, updatable=false)
	protected Resource resource;

	/**
	 * setResource (Resource resource)
	 *
	 * @param resource
	 *
	 */
	public void setResource (Resource resource) {
		this.resource = resource;
		this.resourceId = resource.resourceId;
		this.resourceName = resource.name;
	}

	/**
	 * getResource ()
	 *
	 * @return resource
	 *
	 */
	
	public Resource getResource () {
		return this.resource;
	}


	
	/**
	 * resourceName
	 *
	 */
	@NotAudited
	protected String resourceName;

	/**
	 * setResourceNmae (String resourceName)
	 *
	 * @param resourceName
	 *
	 */
	public void setResourceName (String resourceName) {
		this.resourceName = resourceName;
	}

	/**
	 * getResourceName ()
	 *
	 * @return resourceName
	 *
	 */
	
	public String getResourceName () {
		return this.resourceName;
	}
	
	/**
	 * resourceCategory
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="resourceCategoryid", insertable=false, updatable=false)
	protected ResourceCategory resourceCategory;

	/**
	 * setResourceCategory (ResourceCategory resourceCategory)
	 *
	 * @param resourceCategory
	 *
	 */
	public void setResourceCategory (ResourceCategory resourceCategory) {
		this.resourceCategory = resourceCategory;
		this.resourceCategoryId = resourceCategory.resourceCategoryId;
	}

	/**
	 * getResourceCategory ()
	 *
	 * @return resourceCategory
	 *
	 */
	
	public ResourceCategory getResourceCategory () {
		return this.resourceCategory;
	}


	/**
	 * software
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="softwareid", insertable=false, updatable=false)
	protected Software software;

	/**
	 * setSoftware (Software software)
	 *
	 * @param software
	 *
	 */
	public void setSoftware (Software software) {
		this.software = software;
		this.softwareId = software.softwareId;
	}

	/**
	 * getSoftware ()
	 *
	 * @return software
	 *
	 */
	
	public Software getSoftware () {
		return this.software;
	}


	/**
	 * sample
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="sampleid", insertable=false, updatable=false)
	protected Sample sample;

	/**
	 * setSample (Sample sample)
	 *
	 * @param sample
	 *
	 */
	public void setSample (Sample sample) {
		this.sample = sample;
		this.sampleId = sample.sampleId;
	}

	/**
	 * getSample ()
	 *
	 * @return sample
	 *
	 */
	
	public Sample getSample () {
		return this.sample;
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
	 * runMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="runid", insertable=false, updatable=false)
	protected List<RunMeta> runMeta;


	/** 
	 * getRunMeta()
	 *
	 * @return runMeta
	 *
	 */
	public List<RunMeta> getRunMeta() {
		return this.runMeta;
	}


	/** 
	 * setRunMeta
	 *
	 * @param runMeta
	 *
	 */
	public void setRunMeta (List<RunMeta> runMeta) {
		this.runMeta = runMeta;
	}



	/** 
	 * runLane
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="runid", insertable=false, updatable=false)
	protected List<RunLane> runLane;


	/** 
	 * getRunLane()
	 *
	 * @return runLane
	 *
	 */
	public List<RunLane> getRunLane() {
		return this.runLane;
	}


	/** 
	 * setRunLane
	 *
	 * @param runLane
	 *
	 */
	public void setRunLane (List<RunLane> runLane) {
		this.runLane = runLane;
	}



	/** 
	 * runFile
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="runid", insertable=false, updatable=false)
	protected List<RunFile> runFile;


	/** 
	 * getRunFile()
	 *
	 * @return runFile
	 *
	 */
	public List<RunFile> getRunFile() {
		return this.runFile;
	}


	/** 
	 * setRunFile
	 *
	 * @param runFile
	 *
	 */
	public void setRunFile (List<RunFile> runFile) {
		this.runFile = runFile;
	}



	/** 
	 * staterun
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="runid", insertable=false, updatable=false)
	protected List<Staterun> staterun;


	/** 
	 * getStaterun()
	 *
	 * @return staterun
	 *
	 */
	public List<Staterun> getStaterun() {
		return this.staterun;
	}


	/** 
	 * setStaterun
	 *
	 * @param staterun
	 *
	 */
	public void setStaterun (List<Staterun> staterun) {
		this.staterun = staterun;
	}



}
