
/**
 *
 * JobDraftresource.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftresource
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
@Table(name="jobdraftresource")
public class JobDraftresource extends WaspModel {

	/** 
	 * jobDraftresourceId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int jobDraftresourceId;

	/**
	 * setJobDraftresourceId(int jobDraftresourceId)
	 *
	 * @param jobDraftresourceId
	 *
	 */
	
	public void setJobDraftresourceId (int jobDraftresourceId) {
		this.jobDraftresourceId = jobDraftresourceId;
	}

	/**
	 * getJobDraftresourceId()
	 *
	 * @return jobDraftresourceId
	 *
	 */
	public int getJobDraftresourceId () {
		return this.jobDraftresourceId;
	}




	/** 
	 * jobdraftId
	 *
	 */
	@Column(name="jobdraftid")
	protected int jobdraftId;

	/**
	 * setJobdraftId(int jobdraftId)
	 *
	 * @param jobdraftId
	 *
	 */
	
	public void setJobdraftId (int jobdraftId) {
		this.jobdraftId = jobdraftId;
	}

	/**
	 * getJobdraftId()
	 *
	 * @return jobdraftId
	 *
	 */
	public int getJobdraftId () {
		return this.jobdraftId;
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
	 * jobDraft
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="jobdraftid", insertable=false, updatable=false)
	protected JobDraft jobDraft;

	/**
	 * setJobDraft (JobDraft jobDraft)
	 *
	 * @param jobDraft
	 *
	 */
	public void setJobDraft (JobDraft jobDraft) {
		this.jobDraft = jobDraft;
		this.jobdraftId = jobDraft.jobDraftId;
	}

	/**
	 * getJobDraft ()
	 *
	 * @return jobDraft
	 *
	 */
	
	public JobDraft getJobDraft () {
		return this.jobDraft;
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


}
