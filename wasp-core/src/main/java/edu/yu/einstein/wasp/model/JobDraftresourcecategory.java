
/**
 *
 * JobDraftresourcecategory.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftresourcecategory
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="jobdraftresourcecategory")
public class JobDraftresourcecategory extends WaspModel {

	/** 
	 * jobDraftresourcecategoryId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer jobDraftresourcecategoryId;

	/**
	 * setJobDraftresourcecategoryId(Integer jobDraftresourcecategoryId)
	 *
	 * @param jobDraftresourcecategoryId
	 *
	 */
	
	public void setJobDraftresourcecategoryId (Integer jobDraftresourcecategoryId) {
		this.jobDraftresourcecategoryId = jobDraftresourcecategoryId;
	}

	/**
	 * getJobDraftresourcecategoryId()
	 *
	 * @return jobDraftresourcecategoryId
	 *
	 */
	public Integer getJobDraftresourcecategoryId () {
		return this.jobDraftresourcecategoryId;
	}




	/** 
	 * jobDraftId
	 *
	 */
	@Column(name="jobdraftid")
	protected Integer jobDraftId;

	/**
	 * setJobDraftId(Integer jobDraftId)
	 *
	 * @param jobDraftId
	 *
	 */
	
	public void setJobDraftId (Integer jobDraftId) {
		this.jobDraftId = jobDraftId;
	}

	/**
	 * getJobDraftId()
	 *
	 * @return jobDraftId
	 *
	 */
	public Integer getJobDraftId () {
		return this.jobDraftId;
	}




	/** 
	 * resourcecategoryId
	 *
	 */
	@Column(name="resourcecategoryid")
	protected Integer resourcecategoryId;

	/**
	 * setResourcecategoryId(Integer resourcecategoryId)
	 *
	 * @param resourcecategoryId
	 *
	 */
	
	public void setResourcecategoryId (Integer resourcecategoryId) {
		this.resourcecategoryId = resourcecategoryId;
	}

	/**
	 * getResourcecategoryId()
	 *
	 * @return resourcecategoryId
	 *
	 */
	public Integer getResourcecategoryId () {
		return this.resourcecategoryId;
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
		this.jobDraftId = jobDraft.jobDraftId;
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
	 * resourceCategory
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="resourcecategoryid", insertable=false, updatable=false)
	protected ResourceCategory resourceCategory;

	/**
	 * setResourceCategory (ResourceCategory resourceCategory)
	 *
	 * @param resourceCategory
	 *
	 */
	public void setResourceCategory (ResourceCategory resourceCategory) {
		this.resourceCategory = resourceCategory;
		this.resourcecategoryId = resourceCategory.resourceCategoryId;
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


}
