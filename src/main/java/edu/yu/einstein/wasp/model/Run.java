
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

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="run")
public class Run extends WaspModel {

	/** 
	 * runId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int runId;

	/**
	 * setRunId(int runId)
	 *
	 * @param runId
	 *
	 */
	
	public void setRunId (int runId) {
		this.runId = runId;
	}

	/**
	 * getRunId()
	 *
	 * @return runId
	 *
	 */
	public int getRunId () {
		return this.runId;
	}




	/** 
	 * resourceId
	 *
	 */
	@Column(name="resourceid")
	protected int resourceId;

	/**
	 * setResourceId(int resourceId)
	 *
	 * @param resourceId
	 *
	 */
	
	public void setResourceId (int resourceId) {
		this.resourceId = resourceId;
	}

	/**
	 * getResourceId()
	 *
	 * @return resourceId
	 *
	 */
	public int getResourceId () {
		return this.resourceId;
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
	protected int sampleId;

	/**
	 * setSampleId(int sampleId)
	 *
	 * @param sampleId
	 *
	 */
	
	public void setSampleId (int sampleId) {
		this.sampleId = sampleId;
	}

	/**
	 * getSampleId()
	 *
	 * @return sampleId
	 *
	 */
	public int getSampleId () {
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
