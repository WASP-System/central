
/**
 *
 * JobDraftSoftware.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftSoftware
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
@Table(name="jobdraftsoftware")
public class JobDraftSoftware extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6728114013935600776L;
	/** 
	 * jobDraftSoftwareId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer jobDraftSoftwareId;

	/**
	 * setJobDraftSoftwareId(Integer jobDraftSoftwareId)
	 *
	 * @param jobDraftSoftwareId
	 *
	 */
	
	public void setJobDraftSoftwareId (Integer jobDraftSoftwareId) {
		this.jobDraftSoftwareId = jobDraftSoftwareId;
	}

	/**
	 * getJobDraftSoftwareId()
	 *
	 * @return jobDraftSoftwareId
	 *
	 */
	public Integer getJobDraftSoftwareId () {
		return this.jobDraftSoftwareId;
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


}
