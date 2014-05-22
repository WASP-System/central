
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

import javax.persistence.Column;
import javax.persistence.Entity;
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
	 * 
	 */
	private static final long serialVersionUID = 5178604899408106578L;

	/**
	 * setJobDraftresourcecategoryId(Integer jobDraftresourcecategoryId)
	 *
	 * @param jobDraftresourcecategoryId
	 *
	 */
	@Deprecated
	public void setJobDraftresourcecategoryId (Integer jobDraftresourcecategoryId) {
		setId(jobDraftresourcecategoryId);
	}

	/**
	 * getJobDraftresourcecategoryId()
	 *
	 * @return jobDraftresourcecategoryId
	 *
	 */
	@Deprecated
	public Integer getJobDraftresourcecategoryId () {
		return getId();
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
		this.jobDraftId = jobDraft.getId();
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
		this.resourcecategoryId = resourceCategory.getId();
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
